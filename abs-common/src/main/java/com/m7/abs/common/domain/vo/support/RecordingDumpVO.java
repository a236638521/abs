package com.m7.abs.common.domain.vo.support;

import com.m7.abs.common.domain.base.StorageFileBox;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author zhuhf
 */
@Data
@Builder
public class RecordingDumpVO {
    private List<StorageFileBox> storageConf;
    private String filePath;
    private String taskId;
}
