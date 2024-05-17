package com.m7.abs.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Kejie Peng
 * @date 2023年 03月23日 11:23:02
 */
@Slf4j
public class ZipUtil {
    /***
     * 参数为：
     * @param zipFileName   压缩后的文件名
     * @param inputFile    需要压缩的文件
     * @throws IOException
     */
    public static void zip(String zipFileName, File inputFile) throws IOException {
        log.info("压缩中");
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFileName));
        zip(zipOutputStream, inputFile, "");
        zipOutputStream.close();
        log.info("压缩完成");
    }

    /***
     * 重载zip()方法
     * @param zipOutputStream   zip的输出流
     * @param inputFile      需要压缩的文件
     * @param base          文件名
     * @throws IOException
     */
    public static void zip(ZipOutputStream zipOutputStream, File inputFile, String base) throws IOException {
        if (inputFile.isDirectory()) {
            File[] files = inputFile.listFiles();
            if (base.length() != 0) {
                zipOutputStream.putNextEntry(new ZipEntry(base + "/"));
            }
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                zip(zipOutputStream, files[i], base + "/" + file.getName());
            }
        } else {
            zipOutputStream.putNextEntry(new ZipEntry(base));
            FileInputStream fileInputStream = new FileInputStream(inputFile);
            int b;
            log.info(base);
            while ((b = fileInputStream.read()) != -1) {
                zipOutputStream.write(b);
            }
            fileInputStream.close();
        }
    }

    public static void main(String[] args) throws IOException {
        compress();
    }


    /**
     * 8
     * 压缩文件
     *
     * @throws IOException
     */
    private static void compress() throws IOException {
        ZipUtil.zip("D:\\study\\研究生\\notebook\\java后端.zip", new File("D:\\study\\研究生\\notebook\\java后端"));
        log.info("压缩完成");
    }

}
