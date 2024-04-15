package org.cneko.sudo.util;

import net.minecraft.world.entity.player.Player;
import org.apache.commons.codec.digest.DigestUtils;
import org.cneko.ctlib.common.file.JsonConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DataUtil {
    public static @Nullable JsonConfiguration getDataFile(Player player){
        createDataFile(player);
        // 获取文件
        String folder = getDataFilePath(player)+"/data.json";
        Path path = Path.of(folder);
        try {
            return new JsonConfiguration(path);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public static String getDataFilePath(Player player){
        return "home/"+TextUtil.getPlayerName(player);
    }
public static void createDataFile(Player player) {
    String filePath = getDataFilePath(player) + "/data.json";
    Path path = Path.of(filePath);
    // Check if the file already exists
    if (!Files.exists(path)) {
        // Ensure the parent directory exists
        Path parentDir = path.getParent();
        if (!Files.exists(parentDir)) {
            try {
                Files.createDirectories(parentDir); // Create the parent directory if it doesn't exist
            } catch (IOException e) {
                System.out.println("Failed to create parent directory: " + e.getMessage());
                return; // Exit the function as we cannot proceed without creating the directory
            }
        }
        try {
            // Create the data.json file
            Files.createFile(path);
            Files.write(path, "{}".getBytes());
        } catch (IOException e) {
            System.out.println("Failed to create data.json file: " + e.getMessage());
        }
    }
}

    public static void setDefaultValue(Player player){
        createDataFile(player);
        JsonConfiguration data = getDataFile(player);
        if (data == null) return;
        data.set("sudo.level", 1);
        data.set("sudo.enable", false);
        data.set("sudo.password", sha256("null"));
        try {
            data.save();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean hasDataFile(Player player){
        String folder = getDataFilePath(player) + "/data.json";
        Path path = Path.of(folder);
        return Files.exists(path);
    }
    public static String sha256(@NotNull String str){
        return DigestUtils.sha256Hex(str);
    }
}
