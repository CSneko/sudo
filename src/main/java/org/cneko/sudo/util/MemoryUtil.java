package org.cneko.sudo.util;

public class MemoryUtil {
    public static long getUsedMemory(){
        Runtime runtime = Runtime.getRuntime();

        // 获取当前JVM的总内存
        long totalMemory = runtime.totalMemory();

        // 获取当前JVM的空闲内存
        long freeMemory = runtime.freeMemory();

        // 计算已使用的内存
        return (totalMemory - freeMemory) / 1024 / 1024;
    }

    public static long getMaxMemory(){
        Runtime runtime = Runtime.getRuntime();
        return runtime.maxMemory() / 1024 / 1024;
    }
}
