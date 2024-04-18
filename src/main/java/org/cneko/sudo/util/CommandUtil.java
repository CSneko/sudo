package org.cneko.sudo.util;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import org.cneko.sudo.api.PlayerBase;
import org.cneko.sudo.api.SudoPlayer;
import org.lwjgl.system.libc.LibCStdio;

import java.util.List;

public class CommandUtil {
    public static boolean isOptions(String opt){
        return true;
        //return opt.startsWith("_");
    }
    public static boolean hasOption(String opt,String a){
        return opt.contains(a);
        //return isOptions(opt) && opt.contains(a);
    }

    public static final SuggestionProvider<CommandSourceStack> getFilesCanWrite= (context, builder) -> {
        ServerPlayer player = context.getSource().getPlayer();
        if(SudoPlayer.canSudo(player) && SudoPlayer.getSudoLevel(player)>=5 ){
            // 获取服务器目录下的文件
            List<String> files = FileUtil.getAllFileInDic(".");
            for(String file : files){
                builder.suggest("/"+file);
            }
        }else {
            // 获取用户目录下的文件
            List<String> files = FileUtil.getAllFileInDic(PlayerBase.getHome(player));
            for(String file : files){
                builder.suggest("/"+PlayerBase.getHome(player)+"/"+file);
            }
        }

        return builder.buildFuture();
    };

}
