package org.cneko.sudo.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import org.cneko.sudo.api.CommandOutput;

import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.Commands.argument;
import static org.cneko.ctlib.common.network.HttpGet.HttpGetObject;
import static org.cneko.sudo.util.CommandUtil.*;

public class NeoFetchCommand {
    public static void init(){
        CommandRegistrationCallback.EVENT.register(NeoFetchCommand::execute);
    }

    private static void execute(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
        dispatcher.register(literal("neofetch")
                .executes(NeoFetchCommand::neoFetchCommand)
        );
    }

    private static int neoFetchCommand(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        String input = context.getInput();
        CommandOutput.sendCommand(player,input);
        CommandOutput.sendCommandFeedbackToPlayer(player,"command.neofetch");
        return 1;
    }


    public static String format = "    §2█§r§3█§r§2█§r§6█§r§7█§r§8█§r§8█§r§7█§r  §1${player}§r@§1{server}\n" +
            "    §3█§r§2█§r§3█§r§6█§r§7█§r§8█§r§8█§r§7█§r  --------\n" +
            "    §2█§r§3█§r§2█§r§6█§r§7█§r§8█§r§8█§r§7█§r  §1OS§1r: ${os}\n" +
            "    §6█§r§6█§r§6█§r§6█§r§6█§r§7█§r§8█§r§8█§r  §1Kernel§r: Minecraft ${version}\n" +
            "    §6█§r§6█§r§6█§r§6█§r§6█§r§7█§r§8█§r§8█§r  §1Packages§r: ${packages}\n" +
            "    §7█§r§7█§r§7█§r§7█§r§7█§r§8█§r§8█§r§8█§r  §1Shell§r: sudo ${mod_version}\n" +
            "    §8█§r§8█§r§8█§r§8█§r§8█§r§8█§r§8█§r§8█§r  §1CPU§r: ${cpu}\n" +
            "    §8█§r§8█§r§8█§r§8█§r§8█§r§8█§r§8█§r§8█§r  §1Memory§r: ${memory} / ${max_memory}";
    /*
    §2█§r§3█§r§2█§r§6█§r§7█§r§8█§r§8█§r§7█§r  §1${player}§r@§1{server}
    §3█§r§2█§r§3█§r§6█§r§7█§r§8█§r§8█§r§7█§r  --------
    §2█§r§3█§r§2█§r§6█§r§7█§r§8█§r§8█§r§7█§r  §1OS§1r: ${os}
    §6█§r§6█§r§6█§r§6█§r§6█§r§7█§r§8█§r§8█§r  §1Kernel§r: Minecraft ${version}
    §6█§r§6█§r§6█§r§6█§r§6█§r§7█§r§8█§r§8█§r  §1Packages§r: ${packages}
    §7█§r§7█§r§7█§r§7█§r§7█§r§8█§r§8█§r§8█§r  §1Shell§r: sudo ${mod_version}
    §8█§r§8█§r§8█§r§8█§r§8█§r§8█§r§8█§r§8█§r  §1CPU§r: ${cpu}
    §8█§r§8█§r§8█§r§8█§r§8█§r§8█§r§8█§r§8█§r  §1Memory§r: ${memory} / ${max_memory}


     */
}
