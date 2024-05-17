package com.m7.abs.common.domain.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * oss_upload_fail_log
 * @author zhuhf
 */
@Data
@Accessors(chain = true)
@Builder
public class OssUploadFailLog implements Serializable {
    /**
     * ID主键
     */
    private String id;

    /**
     * requestId
     */
    private String requestId;

    /**
     * 账户ID
     */
    private String accountId;

    /**
     * 项目代码
     */
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
     * 文件后缀
     */
    private String fileSuffix;

    /**
     * oss upload key
     */
    private String ossKey;

    /**
     * 重试次数
     */
    private Integer retryTimes;

    /**
     * 最大尝试次数
     */
    private Integer maxTimes;

    /**
     * 重试结果
     */
    private String result;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date lastUpdateTime;

    /**
     * 逻辑删除;0:未删除 1:已删除
     */
    private Integer deleted;

    /**
     * 下次执行时间
     */
    private Date nextExecuteTime;

    /**
     * 执行间隔时间：单位/秒
     */
    private Long intervalTime;

    private static final long serialVersionUID = 1L;
}
