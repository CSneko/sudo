package org.cneko.sudo.util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class TextUtil {
    public static String getPlayerName(Player player){
        Component name = player.getName();
        String strName = name.getString();
        strName = strName.replace("literal{", "").replace("}", "");
        return strName;
    }
    public static String getWorldName(Level world){
        String name = world.toString();
        name = name.replace("[","");
        name = name.replace("]","");
        name = name.replace("ServerLevel","");
        return name;
    }
}
