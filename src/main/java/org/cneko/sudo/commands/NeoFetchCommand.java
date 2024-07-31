package org.cneko.sudo.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import org.cneko.sudo.api.CommandOutput;
import org.cneko.sudo.util.CPUUtil;
import org.cneko.sudo.util.MemoryUtil;

import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.Commands.argument;
import static org.cneko.ctlib.common.network.HttpGet.HttpGetObject;
import static org.cneko.sudo.util.CommandUtil.*;
import static org.cneko.sudo.api.CommandOutput.varReplace;

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
        String result = varReplace(format,player);
        result = result.replace("${packages}", FabricLoader.getInstance().getAllMods().size()+" (Fabric)")
                        .replace("${mod_version}",FabricLoader.getInstance().getModContainer("sudo").get().getMetadata().getVersion().getFriendlyString())
                .replace("${cpu}", CPUUtil.getCPUInfoString())
                .replace("${memory}", MemoryUtil.getUsedMemory()+"")
                .replace("${max_memory}", MemoryUtil.getMaxMemory()+"");
        CommandOutput.sendCommand(player,input);
        CommandOutput.sendCommandFeedbackToPlayer(player,"command.neofetch",new Object[]{result});
        return 1;
    }


    public static String format = """
                §a███████§r  §a${player}§r@§a${server}
                §a███████§r  --------
                §a███████§r  §aOS§r: ${os}
                §6███████§r  §aKernel§r: Minecraft ${version}
                §6███████§r  §aPackages§r: ${packages}
                §6███████§r  §aShell§r: sudo ${mod_version}
                §6███████§r  §aCPU§r: ${cpu}
                §6███████§r  §aMemory§r: ${memory}MB / ${max_memory}MB\
            """;
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
