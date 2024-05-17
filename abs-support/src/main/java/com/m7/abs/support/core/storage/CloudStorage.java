package com.m7.abs.support.core.storage;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

/**
 * 对象存储服务
 *
 * @author zhuhf
 */
public interface CloudStorage {

    /**
     * 上传文件
     *
     * @param inputStream in
     * @param key         文件在 bucket 中的访问路径如：007/2100147/2022-01-10/CentOS-7-x86_64-Minimal-2009.iso
     * @throws Exception 上传错误时抛出异常
     */
    void uploadFile(InputStream inputStream, String key) throws Exception;

    /**
     * 分段上传
     *
     * @param file
     * @param key  文件在 bucket 中的访问路径如：007/2100147/2022-01-10/CentOS-7-x86_64-Minimal-2009.iso
     * @throws Exception
     */
    void multipartUploadFile(File file, String key) throws Exception;

    /**
     * 指定 key 的文件访问地址
     *
     * @param key 文件在 bucket 中的访问路径如：007/2100147/2022-01-10/CentOS-7-x86_64-Minimal-2009.iso
     * @return 文件访问地址
     */
    String fileAddress(String key);

    /**
     * 获取文件host
     *
     * @return 文件host
     */
    String getFileHost();

    /**
     * 生成可预览的外链
     *
     * @param key
     * @param expireDate
     * @return
     */
    String generatePreSignedUrl(String key, Date expireDate);

    /**
     * 停止方法
     */
    void shutdown();

    default String getId() {
        return this.getClass().getSimpleName();
    }
}
