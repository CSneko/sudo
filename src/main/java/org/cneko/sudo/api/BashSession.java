package org.cneko.sudo.api;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BashSession {
    private CommandSourceStack source;
    private Process process;
    private BufferedReader outputReader;
    private BufferedReader errorReader;
    private StringBuilder globalOutput;

    public BashSession(CommandSourceStack source) {
        this.source = source;
        this.globalOutput = new StringBuilder();
    }

    public CommandSourceStack getSource() {
        return source;
    }

    public void setSource(CommandSourceStack source) {
        this.source = source;
    }

    /**
     * 执行给定的bash命令，并在每输出一条信息时调用output(String)方法。
     *
     * @param command 要执行的bash命令字符串
     * @throws Exception 如果命令执行过程中发生错误
     */
    public void execute(String command) throws Exception {
        if (process != null && !process.isAlive()) { // 检查当前进程是否已结束
            close(); // 若已结束，先关闭旧进程及相关资源
        }

        // 启动一个新的进程来执行bash命令（仅在没有活动进程或进程已结束时创建新进程）
        if (process == null || process.exitValue() != 0) {
            process = Runtime.getRuntime().exec(new String[]{"bash", "-c", command});
            outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        }

        // 读取并实时处理标准输出和错误输出
        handleOutput(outputReader);
        handleError(errorReader);
    }

    private void handleOutput(BufferedReader reader) throws Exception {
        String line;
        while ((line = reader.readLine()) != null) {
            output(line); // 实时输出当前行
            globalOutput.append(line).append(System.lineSeparator()); // 追加到全局字符串变量
        }
    }

    private void handleError(BufferedReader reader) throws Exception {
        String line;
        while ((line = reader.readLine()) != null) {
            output(line); // 实时输出当前行
            globalOutput.append(line).append(System.lineSeparator()); // 追加到全局字符串变量
        }
    }

    /**
     * 关闭与bash命令执行相关的所有资源，包括进程、输入输出流等。
     */
    public void close() {
        if (process != null) {
            process.destroy();
        }
        if (outputReader != null) {
            try {
                outputReader.close();
            } catch (Exception ignored) {
            }
        }
        if (errorReader != null) {
            try {
                errorReader.close();
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * 获取执行bash命令产生的全局输出（所有命令的标准输出与错误输出合并）。
     *
     * @return 全局输出字符串
     */
    public String getGlobalLog() {
        return globalOutput.toString();
    }

    private void readAndAppend(BufferedReader reader, StringBuilder target) throws Exception {
        String line;
        while ((line = reader.readLine()) != null) {
            target.append(line).append(System.lineSeparator());
        }
    }

    private void output(String message){
        source.sendSystemMessage(Component.literal(message));
    }

}
