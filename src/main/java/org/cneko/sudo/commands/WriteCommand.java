package org.cneko.sudo.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.core.jmx.Server;

import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.Commands.argument;
import static org.cneko.sudo.util.CommandUtil.*;

public class WriteCommand {
    public static void init(){
        CommandRegistrationCallback.EVENT.register(WriteCommand::execute);
    }

    private static void execute(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
        dispatcher.register(literal("write")
                .then(argument("file", StringArgumentType.string())
                        .suggests(getFilesCanWrite)
                        .then(argument("text", StringArgumentType.greedyString())
                                .executes(WriteCommand::writeCommand)
                        )
                )
        );
    }

    private static int writeCommand(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();

        return 1;
    }
}
