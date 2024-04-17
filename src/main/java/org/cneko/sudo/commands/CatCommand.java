package org.cneko.sudo.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import org.cneko.sudo.api.CommandOutput;
import org.cneko.sudo.api.PlayerBase;
import org.cneko.sudo.api.SudoPlayer;
import org.cneko.sudo.util.FileUtil;

import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.Commands.argument;
import static org.cneko.sudo.util.CommandUtil.*;

public class CatCommand {
    public static void init(){
        CommandRegistrationCallback.EVENT.register(CatCommand::execute);
    }

    private static void execute(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
        dispatcher.register(literal("cat")
                .then(argument("file", StringArgumentType.string())
                        .executes(CatCommand::catCommand)
                        .then(argument("output", StringArgumentType.greedyString())
                                .executes(CatCommand::catCommand)
                        )
                )
        );
    }


    private static int catCommand(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        String file = StringArgumentType.getString(context, "file");
        String input = context.getInput();
        // 判断是否为sudo玩家且拥有4级权限
        boolean canVisitOthers = SudoPlayer.canSudo(player) && SudoPlayer.getSudoLevel(player) >= 4;
        CommandOutput.sendCommand(player, input);
        // 文件不存在时发送错误消息
        if(!FileUtil.isFileExists(file.substring(1))){
            CommandOutput.sendCommandFeedbackToPlayer(player,"command.cat.not-exist");
            return -1;
        }
        // 如果文件路径不在自己的目录下且不是sudo玩家，则发送错误消息
        if(!file.startsWith("/"+PlayerBase.getHome(player))&&!canVisitOthers){
            CommandOutput.sendCommandFeedbackToPlayer(player,"command.sudo.auth.failed");
            return -1;
        }
        // 实际的文件路径
        String realFile = FileUtil.getRealFilePath(file);
        catCommandOutput(player,FileUtil.readFile(realFile));
        return 1;
    }

    private static void catCommandOutput(ServerPlayer player,String message){
        MutableComponent component = Component.literal(message);
        // 点击事件为复制文本
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, message);
        Style style = Style.EMPTY.withClickEvent(clickEvent);
        component.setStyle(style);
        player.sendSystemMessage(component);
    }
}
