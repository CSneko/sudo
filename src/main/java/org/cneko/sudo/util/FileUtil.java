package org.cneko.sudo.util;

import java.nio.charset.StandardCharsets;

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
}
