package org.cneko.sudo.events;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.cneko.sudo.api.CommandOutput;
import org.cneko.sudo.api.SudoPlayer;
import org.cneko.sudo.files.BashrcFile;
import org.cneko.sudo.util.DataUtil;

public class PlayerConnectionEvents {
    public static void init(){
        ServerPlayConnectionEvents.DISCONNECT.register(PlayerConnectionEvents::onPlayerLeave);
        ServerPlayConnectionEvents.JOIN.register(PlayerConnectionEvents::onPlayerJoin);
    }

    private static void onPlayerJoin(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server) {
        ServerPlayer player = handler.getPlayer();
        // 玩家不存在数据文件则初始化
        if(!DataUtil.hasDataFile(player)){
            DataUtil.setDefaultValue(player);
        }
        // 设置环境变量
        CommandOutput.setExport(player,"join_time", String.valueOf(System.currentTimeMillis()/1000));
        CommandOutput.setExport(player,"join_x", String.valueOf(player.getX()));
        CommandOutput.setExport(player,"join_y", String.valueOf(player.getY()));
        CommandOutput.setExport(player,"join_z", String.valueOf(player.getZ()));
        CommandOutput.setExport(player,"ip", player.getIpAddress());
        // 执行.bashrc文件
        new BashrcFile(player).execute();
    }

    private static void onPlayerLeave(ServerGamePacketListenerImpl handler, MinecraftServer server) {
        ServerPlayer player = handler.getPlayer();
        // 移除sudo
        SudoPlayer.setSudo(player, false);
    }

}
