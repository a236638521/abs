package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @program: abs
 * @description: 推送失败记录
 * @author: yx
 * @create: 2021-12-23 16:58
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
@TableName("dispatcher_fail_log")
public class DispatcherFailLogEntity {

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
     * url
     */
    private String url;

    /**
     * 数据
     */
    private String data;

    /**
     * 请求方式;POST;GET	pg_catalog
     */
    private String method;

    /**
     * content_type;application/json;charset=UTF-8
     */
    private String contentType;

    /**
     * 重试次数
     */
    private Integer retryTimes;

    /**
     * 最大次数
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
    private Integer intervalTime;


}
