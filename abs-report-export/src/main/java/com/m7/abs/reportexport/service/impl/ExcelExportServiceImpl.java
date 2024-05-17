package com.m7.abs.reportexport.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.m7.abs.common.utils.ExcelUtil;
import com.m7.abs.reportexport.service.IExportService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Kejie Peng
 * @date 2023年 04月17日 11:05:21
 */
@Slf4j
public class ExcelExportServiceImpl<T> implements IExportService<T> {
    private Class<T> clazz;
    private String filePath;
    private String fileName;
    // 构建写入对象
    ExcelWriter excelWriter = null;
    private WriteSheet writeSheet = null;

    public ExcelExportServiceImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void init(String filePath, String fileName) {
        this.filePath = filePath;
        this.fileName = fileName + ".xlsx";
        writeSheet = EasyExcel.writerSheet(0, "call-sheet").head(this.clazz)
                .registerWriteHandler(ExcelUtil.getStyleStrategy())
                .build();
        excelWriter = EasyExcel.write(this.filePath + this.fileName).build();
    }

    @Override
    public void write(List<T> records) {
        excelWriter.write(records, writeSheet);
    }

    @Override
    public void stop() {
        // 写入完成
        excelWriter.finish();
    }
}
