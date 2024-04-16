package org.cneko.sudo.api;

import net.minecraft.world.entity.player.Player;
import org.cneko.sudo.util.TextUtil;

public class PlayerBase {
    public static String getHome(Player player){
        String name = TextUtil.getPlayerName(player);
        return "home/"+name;
    }
    public static boolean playerCanWriteFile(Player player,String filePath){
        return  !filePath.contains("data.json") &&
                (filePath.startsWith("/" + getHome(player))
                    || (SudoPlayer.canSudo(player)
                        && SudoPlayer.getSudoLevel(player) >= 4)
                );

    }
}
