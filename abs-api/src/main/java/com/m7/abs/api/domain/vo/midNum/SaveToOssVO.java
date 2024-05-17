package com.m7.abs.api.domain.vo.midNum;

import lombok.*;

import java.util.List;

/**
 * @author zhuhf
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SaveToOssVO {
    private List<StorageFileBox> storageConf;
    private String filePath;
    private String taskId;
}
