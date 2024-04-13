package org.cneko.sudo;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.cneko.sudo.commands.EchoCommand;
import org.cneko.sudo.commands.ExportCommand;
import org.cneko.sudo.commands.SudoCommand;
import org.cneko.sudo.events.PlayerConnectionEvents;

import static org.cneko.sudo.SudoMeta.META;

public class Sudo implements ModInitializer {
    public static final String MOD_ID = "sudo";
    @Override
    public void onInitialize() {
        // 注册命令
        SudoCommand.init();
        EchoCommand.init();
        ExportCommand.init();
        // 注册服务器启动事件监听器
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            META.setMinecraftServer(server);
            PlayerConnectionEvents.init();
        });

    }
}
