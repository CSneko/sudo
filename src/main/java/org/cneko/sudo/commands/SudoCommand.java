package org.cneko.sudo.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.*;
import net.minecraft.server.level.ServerPlayer;
import org.cneko.sudo.api.CommandOutput;
import org.cneko.sudo.api.SudoPlayer;
import org.cneko.sudo.util.PlayerUtil;

import java.util.concurrent.CompletableFuture;

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
                        .executes(SudoCommand::sudoOptions)
                        .then(argument("command",StringArgumentType.greedyString())
                                .suggests(getCommand)
                                .executes(SudoCommand::sudoCommandWithOptions))
                )

                //.then(argument("command",StringArgumentType.greedyString())
                //       .executes(SudoCommand::sudoCommand))
        );
    }

    private static int sudoCommand(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayer();
        String input = context.getInput();
        String command = StringArgumentType.getString(context,"command");
        // 执行命令
        try {
            source.getServer().getCommands().getDispatcher().execute(command, source);
        } catch (CommandSyntaxException e) {
            System.out.println(e.getMessage());
        }
        CommandOutput.sendCommand(player, input);
        return 1;
    }

    private static int sudoOptions(CommandContext<CommandSourceStack> context) {
        String options = StringArgumentType.getString(context, "options");
        ServerPlayer player = context.getSource().getPlayer();
        String input = context.getInput();
        CommandOutput.sendCommand(player, input);
        return 1;
    }
    private static int sudoCommandWithOptions(CommandContext<CommandSourceStack> context){
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayer();
        String options = StringArgumentType.getString(context,"options"); // 选项
        String command = StringArgumentType.getString(context,"command"); // 命令
        command = CommandOutput.varReplace(command,player);
        String input = context.getInput();
        input = CommandOutput.varReplace(input,player);
        assert player != null;
        if(isOptions(options)){
            if(hasOption(options,"i")){
                input = input.replace(command,"");
                if(SudoPlayer.isSudoPlayer(player)){
                    // 判断密码是否正确
                    if(SudoPlayer.checkSudoPassword(command,player)){
                        SudoPlayer.setSudo(player,true);
                    }else {
                        CommandOutput.sendCommandFeedbackToPlayer(player,"command.sudo.option.i.auth.failed");
                    }
                }
            }else if(hasOption(options,"set")){
                if(SudoPlayer.canSudo(player)){
                    // 设置目标玩家为sudo玩家
                    SudoPlayer.setSudoPlayer(PlayerUtil.getByName(command));
                }else {
                    CommandOutput.sendCommandFeedbackToPlayer(player,"command.sudo.auth.failed");
                }
            }
        }

        // 执行命令
        try {
            source.getServer().getCommands().getDispatcher().execute(command, source);
        } catch (CommandSyntaxException ignored) {
        }

        // 发送命令
        CommandOutput.sendCommand(player,input);
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
    private static final SuggestionProvider<CommandSourceStack> getCommand = (context, builder) -> {
        // 获取所有可用命令
        CompletableFuture<Suggestions> commands = SharedSuggestionProvider.suggest(
                context.getSource().getServer().getCommands().getDispatcher()
                        .getRoot().getExamples(), builder);
        return builder.buildFuture();
    };





}
