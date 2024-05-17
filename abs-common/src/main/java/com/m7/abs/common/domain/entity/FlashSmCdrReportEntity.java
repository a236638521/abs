package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Kejie Peng
 * @since 2023-04-10
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("flash_sm_cdr_report")
public class FlashSmCdrReportEntity {

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
    private Long totalCount;

    /**
     * 呼叫成功次数
     */
    private Long complete;

    /**
     * 呼叫成功次数
     */
    private Long fail;

    /**
     * 运营商-联通
     */
    private Long carrierUnicom;

    /**
     * 运营商-电信
     */
    private Long carrierTelecom;

    /**
     * 运营商-移动
     */
    private Long carrierMobile;

    /**
     * 运营商-未知
     */
    private Long carrierUnknown;

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
}
