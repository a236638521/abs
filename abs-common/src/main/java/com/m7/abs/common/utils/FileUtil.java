package com.m7.abs.common.utils;


import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class FileUtil {
    public static void createFileDirs(String filePath) {
        if (StringUtils.isNotEmpty(filePath)) {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        }

    }

    public static void deleteTempFiles(String filePath) {
        if (StringUtils.isNotEmpty(filePath)) {
            File file = new File(filePath);
            FileUtil.deleteFilesAndDir(file);
        }
    }

    public static void deleteFilesAndDir(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    for (File item : files) {
                        item.delete();
                    }
                }
            }
            file.delete();
            File parentFile = file.getParentFile();
            File[] files = parentFile.listFiles();
            if (files != null && files.length == 0) {
                parentFile.delete();
            }
        }
    }
}
