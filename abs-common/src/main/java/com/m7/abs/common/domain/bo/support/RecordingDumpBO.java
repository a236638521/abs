package com.m7.abs.common.domain.bo.support;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author zhuhf
 */
@Data
public class RecordingDumpBO {

    /**
     * 账户 id
     */
    @NotBlank
    private String accountId;
    /**
     * 项目 code
     */
    @NotBlank
    private String projectCode;
    /**
     * 文件下载地址
     */
    private String fileUrl;
    /**
     * oss文件上传后名字
     */
    private String fileName;
    /**
     * 文件后缀，可选
     */
    private String fileSuffix;
    /**
     * 推送失败时的重试间隔时间，单位为秒(s)
     */
    private Long retryInterval;

    /**
     * 时间戳
     */
    @NotNull
    private Long fileTime;
    /**
     * 指定保存对象存储ID
     */
    List<String> storageIds;
}
