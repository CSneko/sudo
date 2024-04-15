package org.cneko.sudo.api;

import net.minecraft.world.entity.player.Player;
import org.cneko.sudo.util.TextUtil;

public class PlayerBase {
    public static String getHome(Player player){
        String name = TextUtil.getPlayerName(player);
        return "home/"+name;
    }
    public static boolean playerCanWriteFile(Player player,String filePath){
        boolean can = filePath.startsWith("/"+getHome(player));
        can = SudoPlayer.canSudo(player) && SudoPlayer.getSudoLevel(player) >=4;
        return can;
    }
}
