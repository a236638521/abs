package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @program: abs
 * @description: 录音转存失败记录管理
 * @author: yx
 * @create: 2021-12-30 14:43
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
@TableName("oss_upload_fail_log")
public class OssUploadFailLogEntity{

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    /**
     * 逻辑删除:
     * 0:未删除;1:已删除;
     */
    @TableLogic
    private int deleted;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "last_update_time", fill = FieldFill.UPDATE)
    private Date lastUpdateTime;

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
     * 下次执行时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date nextExecuteTime;

    /**
     * 执行间隔时间：单位/秒
     */
    private Long intervalTime;

    /**
     * request_id
     */
    private String requestId;


}
