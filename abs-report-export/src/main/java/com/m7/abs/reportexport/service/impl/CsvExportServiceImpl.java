package com.m7.abs.reportexport.service.impl;

import com.m7.abs.common.domain.base.CsvExportable;
import com.m7.abs.common.handler.CsvExportHandler;
import com.m7.abs.reportexport.service.IExportService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Kejie Peng
 * @date 2023年 04月17日 11:05:21
 */
@Slf4j
public class CsvExportServiceImpl<T extends CsvExportable> implements IExportService<T> {
    private String filePath;
    private String fileName;
    private CsvExportHandler<T> csvExport;

    @Override
    public void init(String filePath, String fileName) {
        this.filePath = filePath;
        this.fileName = fileName + ".csv";
        csvExport = new CsvExportHandler();
    }

    @Override
    public void write(List<T> cdrList) {
        try {
            csvExport.exportCsvFile(cdrList, this.filePath + this.fileName,false);
        } catch (Exception e) {
            log.error("写入csv文件异常", e);
        }
    }

    @Override
    public void stop() {
        csvExport.stop();
    }
}
