package com.zzz.cj2356dict.mb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class IOUtils {

    // 注意\\t之前還有一個字符，没有顯示出来，不要看漏了
    private static String blankPatn = "[﻿\\t\\n\\x0B\\f\\r]";

    public static List<String> readLines(String fileName) throws IOException {
        InputStream in = IOUtils.class.getResourceAsStream(fileName);
        List<String> result = new ArrayList<String>();
        InputStreamReader isr = null;
        BufferedReader br = null;
        String str = null;
        try {
            isr = new InputStreamReader(in, "UTF-8");
            br = new BufferedReader(isr);
            while ((str = br.readLine()) != null) {
                if (!"".equals(str)) {
                    String line = str.trim(); // 去首尾
                    line = line.replaceAll("( )\\1+", "$1"); // 中间一个空
                    line = line.replaceAll(blankPatn, ""); // 去空白字符
                    result.add(line);
                }
            }
        } catch (Exception e) {
        } finally {
            try {
                br.close();
            } catch (IOException e) {
            }
        }
        return result;
    }

    /**
     * 文件複製
     * 
     * @author t
     * @time 2016-12-18上午11:01:06
     */
    public static void copyFile(String src, String dest) throws Exception {
        InputStream is = IOUtils.class.getResourceAsStream(src);
        File destFile = new File(dest);
        if (destFile.exists()) {
            destFile.delete();
        }
        destFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(destFile);
        byte[] buffer = new byte[1024];
        int byteCount = 0;
        while ((byteCount = is.read(buffer)) != -1) {
            fos.write(buffer, 0, byteCount);
        }
        fos.flush();
        is.close();
        fos.close();
    }
}
