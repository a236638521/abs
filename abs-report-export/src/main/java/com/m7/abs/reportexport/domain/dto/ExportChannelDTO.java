package com.m7.abs.reportexport.domain.dto;

import com.m7.abs.common.domain.vo.reportexport.MidNumCdrExportRequestVO;
import com.m7.abs.reportexport.service.IExportService;
import lombok.*;

/**
 * 小号话单导出通道数据
 *
 * @author Kejie Peng
 * @date 2023年 03月22日 13:39:06
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ExportChannelDTO {
    private MidNumCdrExportRequestVO requestVO;

    private StorageInfo storage;

    private IExportService exportService;
    /**
     * 插入excel次数
     */
    private Long count = 0L;
    /**
     * 查询记录数
     */
    private Long recordCount = 0L;
    /**
     * 单文件可插入记录数
     */
    private Long eachFileRecordCount = 0L;
    /**
     * 分文件次数
     */
    private Long newFileCount = 1L;

    @Getter
    @Setter
    public static class StorageInfo {
        /**
         * 导出文件目录
         */
        private String filePath;
        /**
         * 导出文件名称
         */
        private String fileName;
        /**
         * 对象存储Key
         */
        private String key;
        /**
         * 文件名称
         */
        private String zipFileName;
    }



    public Long addCount() {
        return ++count;
    }

    public Long addRecordCount(long num) {
        recordCount = recordCount + num;
        return recordCount;
    }

    public Long consumeEachFileRecordCount(long num) {
        eachFileRecordCount = eachFileRecordCount - num;
        return recordCount;
    }

    public Long addNewFileCount() {
        return ++newFileCount;
    }

}
