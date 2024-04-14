package org.cneko.sudo.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import org.cneko.sudo.api.CommandOutput;
import org.cneko.sudo.util.FileUtil;

import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.Commands.argument;
import static org.cneko.sudo.util.CommandUtil.*;

public class EchoCommand {
    public static void init() {
        CommandRegistrationCallback.EVENT.register(EchoCommand::execute);
    }

    private static void execute(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
        dispatcher.register(literal("echo")
                .then(argument("message", StringArgumentType.greedyString())
                        .executes(EchoCommand::echoCommand)
                )
        );
    }

    private static int echoCommand(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        String message = StringArgumentType.getString(context, "message");
        String input = context.getInput();
        CommandOutput.sendCommand(player,input);
        CommandOutput.sendCommandOutput(player,message,input);
        return 1;
    }
}
