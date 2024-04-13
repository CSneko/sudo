package org.cneko.sudo.util;

import net.minecraft.world.entity.player.Player;

import static org.cneko.sudo.SudoMeta.META;
public class PlayerUtil {
    public static Player getByName(String name) {
        return META.getMinecraftServer().getPlayerList().getPlayerByName(name);
    }
}
