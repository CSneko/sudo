package org.cneko.sudo.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import org.cneko.sudo.api.BashSession;
import org.cneko.sudo.api.CommandOutput;
import org.cneko.sudo.api.SudoPlayer;
import org.cneko.sudo.util.ThreadFactories;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.Commands.argument;
import static org.cneko.sudo.util.CommandUtil.*;

public class BashCommand {
    private static ExecutorService executorService = Executors.newCachedThreadPool(new ThreadFactories.BashThreadFactory());
    public static void init(){
        CommandRegistrationCallback.EVENT.register(BashCommand::execute);
    }

    private static void execute(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
        dispatcher.register(literal("bash")
                .requires(source -> source.hasPermission(0))
                .then(argument("command", StringArgumentType.greedyString())
                        .executes(BashCommand::bashCommand)
                )
        );
    }

    private static int bashCommand(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        String input = context.getInput();
        CommandOutput.sendCommand(player,input);
        String command = StringArgumentType.getString(context, "command");
        if(SudoPlayer.canSudo(player)){
            BashSession session = new BashSession(context.getSource());
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    try{
                        session.execute(command);
                        session.close();
                    }catch (Exception e){
                        CommandOutput.sendCommandOutput(player,"§cError: "+e.getMessage());
                    }
                },executorService);
        }else {
            CommandOutput.sendCommandFeedbackToPlayer(player,"command.sudo.auth.failed");
        }
        return 1;
    }
}
