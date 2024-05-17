package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-21
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@ToString
@TableName("middle_number_cdr_report")
public class MiddleNumberCdrReportEntity {

    private static final long serialVersionUID = 1L;


    /**
     * ID
     */
    @TableId("id")
    private String id;

    /**
     * 账户ID
     */
    private String accountId;

    /**
     * 统计时间节点：格式 yyyy-MM-dd HH:mm
     * 例如：2023-04-03 14:00 或者 2023-04-03 14:30
     */
    private String dateTime;

    /**
     * 呼叫次数
     */
    private Long callCount;

    /**
     * 呼叫成功次数
     */
    private Long callComplete;

    /**
     * 呼叫失败次数
     */
    private Long callFail;

    /**
     * 主叫运营商-联通
     */
    private Long callerCarrierUnicom;

    /**
     * 被叫运营商-联通
     */
    private Long calledCarrierUnicom;

    /**
     * 主叫运营商-电信
     */
    private Long callerCarrierTelecom;

    /**
     * 被叫运营商-电信
     */
    private Long calledCarrierTelecom;

    /**
     * 主叫运营商-移动
     */
    private Long callerCarrierMobile;

    /**
     * 被叫运营商-移动
     */
    private Long calledCarrierMobile;

    /**
     * 主叫运营商-未知
     */
    private Long callerCarrierUnknown;
    /**
     * 被叫运营商-未知
     */
    private Long calledCarrierUnknown;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateTime;

    /**
     * 通话时长，实际通话时长，（单位秒）
     */
    private Long billDurationCount;

    /**
     * 计费通话时长，计费字段：60进1（单位：分钟）
     */
    private Long rateDurationCount;
}
