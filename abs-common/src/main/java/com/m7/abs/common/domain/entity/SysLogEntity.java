package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.m7.abs.common.domain.base.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author KejiePeng
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString
@TableName("sys_log")
public class SysLogEntity extends BaseEntity<SysLogEntity> {

    private static final long serialVersionUID = 1L;


    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 用户操作
     */
    @TableField("operation")
    private String operation;

    /**
     * 请求方法
     */
    @TableField("method")
    private String method;

    /**
     * 请求参数
     */
    @TableField("params")
    private String params;

    /**
     * 执行时长(毫秒)
     */
    @TableField("time")

    private Long time;

    /**
     * IP地址
     */
    @TableField("ip")
    private String ip;


}