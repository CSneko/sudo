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
import org.cneko.sudo.api.CommandOutput;
import org.cneko.sudo.api.SudoPlayer;

import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.Commands.argument;
import static org.cneko.sudo.util.CommandUtil.*;

public class ExportCommand {
    public static void init() {
        CommandRegistrationCallback.EVENT.register(ExportCommand::execute);
    }

    private static void execute(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
        dispatcher.register(literal("export")
                .then(argument("key", StringArgumentType.string())
                        .then(argument("value", StringArgumentType.string())
                            .executes(ExportCommand::exportCommand)
                        )
                )
        );
    }

    private static int exportCommand(CommandContext<CommandSourceStack> context) {
        String key = StringArgumentType.getString(context, "key");
        String value = StringArgumentType.getString(context, "value");
        ServerPlayer player = context.getSource().getPlayer();
        String input = context.getInput();
        if(key.startsWith("public_")){
            if(SudoPlayer.canSudo(player)) {
                CommandOutput.setPublicExport(key, value);
            }else {
                CommandOutput.sendCommandFeedbackToPlayer(player,"command.sudo.auth.failed");
            }
        }else {
            CommandOutput.setExport(player, key, value);
        }
        CommandOutput.sendCommand(player, input);
        return 1;
    }
}
