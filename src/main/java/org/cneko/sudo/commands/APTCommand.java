package org.cneko.sudo.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import org.cneko.ctlib.common.file.JsonConfiguration;
import org.cneko.sudo.SudoMeta;
import org.cneko.sudo.api.CommandOutput;
import org.cneko.sudo.api.SudoPlayer;
import org.cneko.sudo.util.ThreadFactories;

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
        );
    }

    private static int searchCommand(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        String input = context.getInput();
        CommandOutput.sendCommand(player,input);
        if(!(SudoPlayer.canSudo(player) && SudoPlayer.getSudoLevel(player)>=4)){
            CommandOutput.sendCommandFeedbackToPlayer(player,"command.sudo.auth.failed");
            return 1;
        }
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try{
            String gameVer = SudoMeta.META.getServer().getVersion();
            String query = StringArgumentType.getString(context, "package");
            HttpGetObject getObject = new HttpGetObject("https://api.modrinth.com/v2/search?query="+query);
            getObject.connect();
            JsonConfiguration res = getObject.getJson();
            for (JsonConfiguration hit : res.getJsonList("hits")){
                String title = hit.getString("title");
                String slug = hit.getString("slug");
                CompletableFuture.runAsync(() -> {
                    try{
                        HttpGetObject mod = new HttpGetObject("https://api.modrinth.com/v2/project/"+slug+"/version");
                        mod.connect();
                        JsonConfiguration modRes = mod.getJson();
                        for (JsonConfiguration version : modRes.getJsonList("")){
                            for(String gameVersion : version.getStringList("game_version")){
                                if(Objects.requireNonNull(gameVer).equalsIgnoreCase(gameVersion)){

                                }
                            }
                        }
                    }catch (Exception e){}
                },executorService);
            }
            }catch (Exception e){
                CommandOutput.sendCommandFeedbackToPlayer(player,"command.apt.network.error");
            }
        },executorService);
        return 1;
    }
}
