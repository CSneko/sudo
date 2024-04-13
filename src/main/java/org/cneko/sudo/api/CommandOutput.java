package org.cneko.sudo.api;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.cneko.sudo.util.TextUtil;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.HashMap;
import java.util.Map;

import static org.cneko.sudo.SudoMeta.META;

public class CommandOutput {
    public static Map<String,Map<String,String>> export = new HashMap<>();
    public static Map<String,String> pubExport = new HashMap<>();

    public static void sendCommand(Player player, String command) {
        // 消息格式
        String messageFormat = "§a${player}@${server}§f:§3${path}$ ${command}";
        // 路径格式
        String pathFormat = "/${world}/${location}";

        // 获取玩家所在位置
        String location =(int)(player.getX())+"."+ (int)(player.getY())+"."+ (int)(player.getZ());
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

    public static void sendCommandFeedbackToPlayer(Player player,String key){
        player.sendSystemMessage(Component.translatable(key));
    }
    public static void sendCommandOutput(Player player,String message){
        player.sendSystemMessage(Component.literal(varReplace(message,player)));
    }

    public static void setExport(Player player,String key,String value){
        String name = TextUtil.getPlayerName(player);
        if(!export.containsKey(name)){
            export.put(name,new HashMap<>());
        }
        Map<String,String> map = export.get(name);
        map.put(key,value);
        export.put(name,map);
    }
    public static void setPublicExport(String key,String value){
        pubExport.put(key,value);
    }
    public static String varReplace(String str,Player player){
        // 玩家所在位置
        String world = TextUtil.getWorldName(player.level());
        str = str.replace("${world}",world)
                .replace("${location}",(int)(player.getX())+"."+ (int)(player.getY())+"."+ (int)(player.getZ()));
        // 服务器相关信息
        str = str.replace("${server}",META.getServer().getName())
                .replace("${version}",META.getServer().getVersion())
                .replace("${motd}",META.getServer().getMotd())
                .replace("${time}",String.valueOf(System.currentTimeMillis()/1000));
        // 玩家信息
        str = str.replace("${uuid}",player.getUUID().toString())
                .replace("${player}",TextUtil.getPlayerName(player))
                .replace("${health}",String.valueOf(player.getHealth()))
                .replace("${max_health}",String.valueOf(player.getMaxHealth()))
                .replace("${level}",String.valueOf(player.experienceLevel))
                .replace("${exp}",String.valueOf(player.experienceProgress));
        // 私有变量
        if(export.containsKey(TextUtil.getPlayerName(player))){
            Map<String,String> map = export.get(TextUtil.getPlayerName(player));
            for(String key:map.keySet()){
                str = str.replace("${"+key+"}",map.get(key));
            }
        }
        // 公共变量
        if(!pubExport.isEmpty()){
            for(String key:pubExport.keySet()){
                str = str.replace("${"+key+"}",pubExport.get(key));
            }
        }
        return str;
    }
}
