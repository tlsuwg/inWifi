package com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by suwg on 2015/8/13.
 */
public class ProsRunTime {


    public static String callCmd(String cmd, String filter) {
        String result = "";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);

            // 执行命令cmd，只取结果中含有filter的这一行
            while ((line = br.readLine()) != null
                    && line.contains(filter) == false) {
            }

            result = line;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
