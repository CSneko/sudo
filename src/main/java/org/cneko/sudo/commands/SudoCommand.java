package org.cneko.sudo.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.cneko.sudo.api.SudoPlayer;
import org.cneko.sudo.util.CommandUtil;

import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.Commands.argument;
import static org.cneko.sudo.util.CommandUtil.*;
public class SudoCommand {
    public static void init(){
        CommandRegistrationCallback.EVENT.register(SudoCommand::execute);
    }

    private static void execute(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
        dispatcher.register(literal("sudo")
                .then(argument("options", StringArgumentType.string())
                        .suggests(getOptions)
                        .executes(SudoCommand::sudoOptions))
                        .then(argument("command",StringArgumentType.greedyString())
                                .executes(SudoCommand::sudoCommandWithOptions))

        );
    }

    private static int sudoOptions(CommandContext<CommandSourceStack> context) {
        String options = StringArgumentType.getString(context, "options");
        return 1;
    }
    private static int sudoCommandWithOptions(CommandContext<CommandSourceStack> context){
        String options = StringArgumentType.getString(context,"options"); // 选项
        String command = StringArgumentType.getString(context,"command"); // 命令
        ServerPlayer player = context.getSource().getPlayer();
        if(isOptions(options)){
            if(hasOption(options,"i")){
                if(SudoPlayer.isSudoPlayer(player)){
                    // 判断密码是否正确
                    if(SudoPlayer.checkSudoPassword(command,player)){
                        SudoPlayer.setSudo(player,true);
                    }else {
                        player.sendSystemMessage(Component.translatable(""));
                    }
                }
            }
        }
        return 1;
    }


    private static final SuggestionProvider<CommandSourceStack> getOnlinePlayers = (context, builder) -> {
        for (ServerPlayer player : context.getSource().getServer().getPlayerList().getPlayers()) {
            String playerTabList = player.getName().toString();
            //替换字符
            String output = playerTabList.replace("literal{", "").replace("}", "");
            builder.suggest(output);
        }
        return builder.buildFuture();
    };

    private static final SuggestionProvider<CommandSourceStack> getOptions= (context, builder) -> {
        builder.suggest("_i");
        return builder.buildFuture();
    };





}
