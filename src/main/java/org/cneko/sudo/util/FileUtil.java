package org.cneko.sudo.util;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class FileUtil {
    // 读取文件内容
    public static String readFile(String filePath) {
        StringBuilder sb = new StringBuilder();
        try {
            java.io.FileInputStream fis = new java.io.FileInputStream(filePath);
            java.io.InputStreamReader isr = new java.io.InputStreamReader(fis, StandardCharsets.UTF_8);
            java.io.BufferedReader br = new java.io.BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            return sb.toString();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "";
        }
    }

    // 写入文件内容
    public static void writeFile(String filePath, String content) {
        try {
            // 文件不存在时创建
            java.io.File file = new java.io.File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            java.io.FileOutputStream fos = new java.io.FileOutputStream(filePath);
            java.io.OutputStreamWriter osw = new java.io.OutputStreamWriter(fos, StandardCharsets.UTF_8);
            java.io.BufferedWriter bw = new java.io.BufferedWriter(osw);
            bw.write(content);
            bw.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    // 判断文件是否存在
    public static boolean isFileExists(String filePath) {
        java.io.File file = new java.io.File(filePath);
        return file.exists();
    }

    // 获取文件夹下的所有文件
    public static List<String> getAllFileInDic(String directoryPath) {
        // 创建一个List存储数据
        List<String> list = new ArrayList<>();

        // 创建一个File对象，表示要遍历的目录
        File directory = new File(directoryPath);

        // 创建一个队列来存储待遍历的文件和目录
        Queue<File> queue = new ArrayDeque<>();
        queue.offer(directory);

        // 循环遍历队列中的文件和目录
        while (!queue.isEmpty()) {
            File file = queue.poll();

            // 检查文件是否存在
            if (file.exists()) {
                // 检查文件是否是目录
                if (file.isDirectory()) {
                    // 获取目录下的所有文件和子目录
                    File[] files = file.listFiles();
                    // 将子文件和子目录添加到队列中
                    for (File subFile : files) {
                        queue.offer(subFile);
                    }
                } else {
                    // 获取相对路径
                    String relativePath = getRelativePath(directory, file);
                    list.add(relativePath);
                }
            }
        }
        return list;
    }

    // 获取文件相对于目录的相对路径
    private static String getRelativePath(File directory, File file) {
        String directoryPath = directory.getPath();
        String filePath = file.getPath();
        if (filePath.startsWith(directoryPath)) {
            return filePath.substring(directoryPath.length() + 1);
        } else {
            return filePath;
        }
    }

    // 获取真正的文件路径
    public static String getRealFilePath(String filePath) {
        // 如果是以/home开头的，则去掉/
        if (filePath.startsWith("/home")) {
            return filePath.substring(1);
        } else {
            return filePath;
        }
    }
}
