package org.cneko.sudo.files;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.cneko.sudo.api.PlayerBase;
import org.cneko.sudo.util.FileUtil;
import org.cneko.sudo.util.TextUtil;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class BashrcFile {

    private final Player player;

    public BashrcFile(Player player){
        this.player = player;
    }

    // 获取玩家.bashrc路径
    public Path getBashrcPath(){
        return Path.of(getStringBashrcPath());
    }
    // 获取玩家.bashrc路径
    public String getStringBashrcPath(){
        return PlayerBase.getHome(player)+"/.bashrc";
    }

    // 设置命令列表
    public void setCommandList(List<String> commands){
        // 转换为String
        String commandsString = String.join(";", commands);
        // 清空文件
        FileUtil.writeFile(getStringBashrcPath(), "");
        // 写入文件
        FileUtil.writeFile(getStringBashrcPath(), commandsString);
    }

    // 获取命令列表
    public List<String> getCommandList(){
        // 判断文件是否存在
        if(!FileUtil.isFileExists(getStringBashrcPath())){
            // 写入文件
            FileUtil.writeFile(getStringBashrcPath(), "");
        }
        // 获取文件内容
        String file = FileUtil.readFile(getStringBashrcPath());
        // 分割文件
        String[] commands = file.split(";");
        return List.of(commands);
    }

    // 执行命令
    public void execute(){
        if(player instanceof ServerPlayer serverPlayer){
            // 获取命令列表
            List<String> commands = getCommandList();
            for (String command : commands) {
                try {
                    serverPlayer.getServer().getCommands().getDispatcher().execute(command, serverPlayer.createCommandSourceStack());
                } catch (CommandSyntaxException ignored) {
                }
            }
        }
    }

}
