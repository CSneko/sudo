package org.cneko.sudo.api;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.cneko.sudo.util.TextUtil;
import static org.cneko.sudo.SudoMeta.META;

public class CommandOutput {
    public static void sendCommand(Player player, String command) {
        // 消息格式
        String messageFormat = "§a${player}@{server}§f:§3${path}$ ${command}";
        // 路径格式
        String pathFormat = "/${world}/${location}";

        // 获取玩家所在位置
        String location = player.getX()+"."+player.getY()+"."+player.getZ();
        // 获取玩家所在世界名称
        String worldName = TextUtil.getWorldName(player.level());

        // 替换路径
        String path = pathFormat.replace("${world}", worldName)
                .replace("${location}", location);
        // 替换消息
        String msg = messageFormat.replace("${player}", TextUtil.getPlayerName(player))
                .replace("${server}", META.getServer().getName())
                .replace("${path}", path)
                .replace("${command}", command);

        // 发生消息
        player.sendSystemMessage(Component.literal(msg));
    }
}
