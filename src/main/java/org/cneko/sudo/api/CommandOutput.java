package org.cneko.sudo.api;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.cneko.ctlib.common.util.base.StringUtil;
import org.cneko.sudo.util.FileUtil;
import org.cneko.sudo.util.TextUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.cneko.sudo.SudoMeta.META;

public class CommandOutput {
    public static Map<String,Map<String,String>> export = new HashMap<>();
    public static Map<String,String> pubExport = new HashMap<>();

    public static void sendCommand(Player player, String command) {
        // 消息格式
        String messageFormat = "§a${player}@${server}§f:§3${path}§f$ ${command}";
        // 路径格式
        String pathFormat = "/${world}/${location}";

        // 替换路径
        String path = varReplace(pathFormat,player);
        // 替换消息
        String msg = messageFormat.replace("${path}", path)
                .replace("${command}", command);

        // 替换消息中的变量
        msg = varReplace(msg,player);

        // 发送消息
        player.sendSystemMessage(Component.literal(msg));
    }

    public static void sendCommandFeedbackToPlayer(Player player,String key){
        player.sendSystemMessage(Component.translatable(key));
    }
    public static void sendCommandOutput(Player player,String message){
        player.sendSystemMessage(Component.literal(varReplace(message,player)));
    }
    public static void sendCommandOutput(Player player,String message,String command){
        if(StringUtil.checkFormat(command,"${any} >> ${any}")){
            // 获取文件
            String[] cmd = command.split(" >> ");
            String file = cmd[1];
            file = varReplace(file,player);
            if(PlayerBase.playerCanWriteFile(player,file)) {
                // 清空文件并将消息写入文件
                FileUtil.writeFile(file, "");
                FileUtil.writeFile(file, message);
            }
        }
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
        pubExport.put(key.replace("public_",""),value);
    }
    public static String varReplace(String str,Player player){
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
                .replace("${exp}",String.valueOf(player.experienceProgress))
                .replace("${home}",PlayerBase.getHome(player));
        //其它
        str = str.replace("${c}","§");
        return str;
    }
}
