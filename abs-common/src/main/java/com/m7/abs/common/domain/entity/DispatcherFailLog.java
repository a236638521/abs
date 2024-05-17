package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * dispatcher_fail_log
 * @author zhuhf
 */
@Data
@Builder
@Accessors(chain = true)
@TableName(schema = "public")
@NoArgsConstructor
@AllArgsConstructor
public class DispatcherFailLog implements Serializable {
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
     * url
     */
    private String url;

    /**
     * 数据
     */
    private String data;

    /**
     * 请求方式;POST;GET
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
