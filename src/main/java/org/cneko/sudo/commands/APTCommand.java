package org.cneko.sudo.commands;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import org.cneko.ctlib.common.file.JsonConfiguration;
import org.cneko.ctlib.common.network.HttpGet;
import org.cneko.sudo.SudoMeta;
import org.cneko.sudo.api.CommandOutput;
import org.cneko.sudo.api.SudoPlayer;
import org.cneko.sudo.util.FileUtil;
import org.cneko.sudo.util.ThreadFactories;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.Commands.argument;
import static org.cneko.ctlib.common.network.HttpGet.HttpGetObject;
import static org.cneko.sudo.util.CommandUtil.*;

public class APTCommand {
    private static final ExecutorService executorService = Executors.newCachedThreadPool(new ThreadFactories.ModrinthThreadFactory());
    public static void init() {
        CommandRegistrationCallback.EVENT.register(APTCommand::execute);
    }

    private static void execute(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
        dispatcher.register(literal("apt")
                .then(literal("search")
                        .then(argument("package", StringArgumentType.string())
                                .executes(APTCommand::searchCommand)
                        )
                )
                .then(literal("install")
                        .then(argument("slug", StringArgumentType.string())
                                .then(argument("version", StringArgumentType.string())
                                        .executes(APTCommand::installCommand)
                                )
                        )
                )
        );
    }


    private static int searchCommand(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        String input = context.getInput();
        CommandOutput.sendCommand(player,input);
        if(!SudoPlayer.canSudo(player)){
            CommandOutput.sendCommandFeedbackToPlayer(player,"command.sudo.auth.failed");
            return 1;
        }
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try{
            String query = StringArgumentType.getString(context, "package");
            HttpGetObject getObject = new HttpGetObject("https://api.modrinth.com/v2/search?query="+query);
            getObject.setCookie(new HashMap<>());
            getObject.connect();
            CommandOutput.sendCommandFeedbackToPlayer(player,"apt.command.search.searching");
            JsonConfiguration res = getObject.getJson();
            CommandOutput.sendCommandFeedbackToPlayer(player,"apt.command.search.results");
            for (JsonConfiguration hit : res.getJsonList("hits")){
                String title = hit.getString("title");
                String slug = hit.getString("slug");
                CommandOutput.sendCommandOutput(player,"§l§6"+title+"  §r slug: §o§e"+slug);
            }
            }catch (Exception e){
                CommandOutput.sendCommandFeedbackToPlayer(player,"command.apt.network.error");
                System.out.println(e.getMessage());
            }
        },executorService);
        return 1;
    }

    private static int installCommand(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        String input = context.getInput();
        CommandOutput.sendCommand(player,input);
        String slug = StringArgumentType.getString(context, "slug");
        String version = StringArgumentType.getString(context, "version");
        if(!SudoPlayer.canSudo(player)){
            CommandOutput.sendCommandFeedbackToPlayer(player,"command.sudo.auth.failed");
            return 1;
        }
        CompletableFuture.runAsync(() -> {
            try{
                HttpGetObject mod = new HttpGetObject("https://api.modrinth.com/v2/project/"+slug+"/version");
                mod.setCookie(new HashMap<>());
                mod.connect();
                String modRes = mod.getResponse();
                // 创建一个 Gson 实例
                Gson gson = new Gson();
                // 使用 Gson 解析 JSON 字符串数组为 JsonArray 对象
                JsonArray jsonArray = JsonParser.parseString(modRes).getAsJsonArray();
                List<JsonConfiguration> versions = new ArrayList<>();
                // 遍历 JSON 数组，并将每个元素转换为字符串
                for (JsonElement jsonElement : jsonArray) {
                    String jsonStr = gson.toJson(jsonElement);
                    versions.add(new JsonConfiguration(jsonStr));
                }
                for(JsonConfiguration ver : versions){
                    String verNum = ver.getString("version_number");
                    String verId = ver.getString("id");
                    if(verNum.equalsIgnoreCase(version) || verNum.equalsIgnoreCase(verId)){
                        // 发送相关信息
                        CommandOutput.sendCommandFeedbackToPlayer(player,"apt.command.install.new");
                        CommandOutput.sendCommandOutput(player,ver.getString("name")+"-"+verNum);
                        // 下载并退出循环
                        List<JsonConfiguration> files = ver.getJsonList("files");
                        JsonConfiguration file = files.get(0);
                        String url = file.getString("url");
                        String fileName = file.getString("filename");
                        double fileSize = file.getDouble("size") / 1024;
                        CommandOutput.sendCommandFeedbackToPlayer(player,"apt.command.install.size", new String[]{fileSize+"Kb"});
                        // 开始下载
                        CommandOutput.sendCommandFeedbackToPlayer(player,"apt.command.install.downloading", new String[]{url});
                        // 获取当前时间
                        long startTime = System.currentTimeMillis();
                        HttpGet.SimpleHttpGet.getFile("mods/"+fileName,url,null);
                        // 结束时间
                        long endTime = System.currentTimeMillis();
                        // 下载的秒数
                        double seconds = (endTime - startTime) / 1000.0;
                        // 平均速度
                        double speed = fileSize / seconds;
                        CommandOutput.sendCommandFeedbackToPlayer(player,"apt.command.install.downloaded",new Double[]{fileSize,seconds,speed});
                        break;
                    }
                }
            }catch (Exception e){
                CommandOutput.sendCommandFeedbackToPlayer(player,"command.apt.network.error");
                System.out.println(e.getMessage());
            }
        },executorService);
        return 1;
    }
}
