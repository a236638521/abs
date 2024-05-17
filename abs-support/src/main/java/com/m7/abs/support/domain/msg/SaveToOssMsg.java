package com.m7.abs.support.domain.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author zhuhf
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveToOssMsg {

    /**
     * 任务 ID
     */
    private String taskId;

    /**
     * 请求 id
     */
    private String requestId;

    /**
     * 账户 id
     */
    private String accountId;
    /**
     * 项目 code
     */
    private String projectCode;
    /**
     * 文件地址
     */
    private String fileUrl;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件后缀，可选
     */
    private String fileSuffix;

    private String ossKey;
    /**
     * 推送失败时的重试间隔时间，单位为秒(s)
     */
    private Long retryInterval;
    /**
     * 指定保存对象存储ID
     */
    List<String> storageIds;
}
