package com.m7.abs.reportexport.service;

import java.util.List;

/**
 * @author Kejie Peng
 * @date 2023年 04月17日 11:03:00
 */
public interface IExportService<T> {
    /**
     * 初始化数据
     */
    void init(String filePath, String fileName);

    /**
     * 写入
     */
    void write(List<T> cdrList);

    /**
     * 停止
     */
    void stop();
}
