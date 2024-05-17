package com.m7.abs.common.handler;

import com.m7.abs.common.domain.base.CsvExportable;
import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * @author Kejie Peng
 * @date 2023年 04月17日 10:08:05
 */
@Slf4j
public class CsvExportHandler<T extends CsvExportable> {
    private boolean hasTitle = false;
    CSVWriter writer = null;

    /**
     * @param rowDataList : T类型对象列表
     * @param csvFilePath : 输出的CSV文件路径
     * @throws Exception : 异常发生时，抛出
     * @methodName : exportCsvFile
     * @description : 导出CSV文件
     */
    public void exportCsvFile(List<T> rowDataList, String csvFilePath, boolean autoStop)
            throws Exception {
        if (rowDataList.size() == 0) {
            //必须要有导出数据，否则创建标题列失败
            throw new Exception("无导出数据.");
        }
        //取得第一个对象
        T rowDataObj = rowDataList.get(0);
        //将数据写入csv格式文件
        writeToCsv(rowDataList, rowDataObj, csvFilePath, autoStop);
    }


    /**
     * @param dataList    : T类型对象列表
     * @param rowDataObj: T类型对象
     * @param filePath    : 输出的文件路径
     * @throws Exception : 异常发生时，抛出
     * @methodName : writeToCsv
     * @description : 将数据写入csv格式文件
     */
    private void writeToCsv(List<T> dataList, T rowDataObj, String filePath, boolean autoStop) throws Exception {
        try {

            // 创建CSV文件
            if (writer == null) {
                File csvFile = new File(filePath);
                writer = new CSVWriter(new FileWriter(csvFile));
            }

            if (!hasTitle) {
                hasTitle = true;
                writer.writeNext(rowDataObj.outputCsvTitleLine());
            }

            for (int i = 0; i < dataList.size(); i++) {
                T rowData = dataList.get(i);
                writer.writeNext(rowData.outputCsvDataLine());
            }

        } finally {
            if (autoStop) {
                this.stop();
            }
        }
    }


    public void stop() {
        //关闭流
        try {
            if (writer != null) {
                writer.close();
                writer = null;
            }
        } catch (Exception e) {
            log.error("close stream error", e);
        }
    }
}
