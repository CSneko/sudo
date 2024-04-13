package org.cneko.sudo.api;

import net.minecraft.world.entity.player.Player;
import org.cneko.ctlib.common.file.JsonConfiguration;
import org.cneko.sudo.util.DataUtil;
import org.cneko.sudo.util.TextUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SudoPlayer {
    public static Map<String, Boolean> sudoPlayers = new HashMap<>();
    /**
     * 玩家当前可否使用sudo（在退出游戏后就会移除）
     *
     * @param player 玩家
     * @return 可否使用sudo
     */
    public static boolean canSudo(Player player){
        String name = TextUtil.getPlayerName(player);
        // 如果玩家不在sudo玩家列表中，则返回false
        if(!sudoPlayers.containsKey(name)){
            return false;
        }
        return sudoPlayers.get(name);
    }

    /**
     * 设置玩家能够在当前游戏执行sudo（在退出游戏后就会移除）
     * @param player 玩家
     */
    public static void setSudo(Player player, boolean canSudo){
        String name = TextUtil.getPlayerName(player);
        sudoPlayers.put(name, canSudo);
    }

    /**
     * 玩家是否为sudo玩家
     *
     * @param player 玩家
     * @return 是否为sudo玩家
     */
    public static boolean isSudoPlayer(Player player){
        // 获取数据文件
        JsonConfiguration data = DataUtil.getDataFile(player);
        return data.getBoolean("sudo.enable", false);
    }

    /**
     * 设置玩家为sudo玩家
     * @param player 玩家
     */
    public static void setSudoPlayer(Player player){
        // 设置默认值
        DataUtil.setDefaultValue(player);
    }

    /**
     * 获取玩家的sudo等级
     *
     * @param player 玩家
     * @return sudo等级（与原版的op等级一致）
     */
    public static int getSudoLevel(Player player){
        // 获取数据文件
        JsonConfiguration data = DataUtil.getDataFile(player);
        return data.getInt("sudo.level");
    }


    /**
     * 设置玩家的sudo等级
     * @param player 玩家
     * @param level sudo等级（与原版的op等级一致）
     */
    public static void setSudoLevel(Player player, int level){
        // 获取数据文件
        JsonConfiguration data = DataUtil.getDataFile(player);
        data.set("sudo.level", level);
        try {
            data.save();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 设置sudo密码
     * @param password 密码
     * @param player 玩家
     */
    public static void setSudoPassword(String password,Player player){
        JsonConfiguration data = DataUtil.getDataFile(player);
        // 使用sha256加密
        data.set("sudo.password", DataUtil.sha256(password));
    }

    /**
     * 验证密码
     * @param password 密码
     * @param player 玩家
     * @return 是否验证通过
     */
    public static boolean checkSudoPassword(String password,Player player){
        JsonConfiguration data = DataUtil.getDataFile(player);
        // 使用sha256加密
        return data.getString("sudo.password").equals(DataUtil.sha256(password));
    }
}