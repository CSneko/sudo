package org.cneko.sudo.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CPUUtil {
    public static String getCPUInfoString() {
        String osName = System.getProperty("os.name").toLowerCase();
        String command = "";

        if (osName.contains("linux")) {
            command = "lscpu";
        } else if (osName.contains("windows")) {
            command = "wmic cpu get name,NumberOfCores,MaxClockSpeed /format:value";
        }

        if (!command.isEmpty()) {
            try {
                Process process = Runtime.getRuntime().exec(command);
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                StringBuilder output = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                reader.close();
                process.waitFor();

                // 格式化输出
                return formatCpuInfo(output.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return "Unknown";
        }
        return "Unknown";
    }

    public static String formatCpuInfo(String rawInfo) {
        String[] lines = rawInfo.split("\n");
        String modelName = "";
        int numberOfCores = 0;
        double maxClockSpeed = 0.0;

        for (String line : lines) {
            if (line.startsWith("Model name:")) {
                modelName = line.substring("Model name:".length()).trim();
            } else if (line.startsWith("CPU(s):")) {
                numberOfCores = Integer.parseInt(line.substring("CPU(s):".length()).trim());
            } else if (line.startsWith("CPU MHz:")) {
                maxClockSpeed = Double.parseDouble(line.substring("CPU MHz:".length()).trim());
            }
        }
        return modelName + " ("+ numberOfCores + ") @ " + maxClockSpeed / 1000 + "GHz";
    }
}
