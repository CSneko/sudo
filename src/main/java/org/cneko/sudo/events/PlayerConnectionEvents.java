package org.cneko.sudo.events;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.cneko.sudo.api.SudoPlayer;

public class PlayerConnectionEvents {
    public static void init(){
        ServerPlayConnectionEvents.DISCONNECT.register(PlayerConnectionEvents::onPlayerLeave);
    }


    private static void onPlayerLeave(ServerGamePacketListenerImpl handler, MinecraftServer server) {
        ServerPlayer player = handler.getPlayer();
        // 移除sudo
        SudoPlayer.setSudo(player, false);
    }

}
