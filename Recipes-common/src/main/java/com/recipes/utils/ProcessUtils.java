package com.recipes.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ProcessUtils {

    public static String runPythonScript(String scriptPath, String args) {
        StringBuilder output = new StringBuilder();
        try {
            String pythonInterpreter = "/Users/jememalum/Downloads/backendwithoutElasticSearch/venv/bin/python3"; // 修改为你的虚拟环境中的 Python 解释器路径
            ProcessBuilder pb = new ProcessBuilder(pythonInterpreter, scriptPath, args);
            Process p = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);  // 打印错误信息到控制台
            }
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.toString();
    }
}
