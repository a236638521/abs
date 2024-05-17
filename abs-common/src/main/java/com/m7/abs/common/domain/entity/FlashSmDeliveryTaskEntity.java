package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2022-06-02
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("flash_sm_delivery_task")
public class FlashSmDeliveryTaskEntity {

    private static final long serialVersionUID = 1L;
    @TableId("id")
    private String id;
    /**
     * 账户ID
     */
    private String accountId;

    /**
     * 关联ID;和其他通道关联的ID
     */
    private String channelTaskId;


    /**
     * 通道ID
     */
    private String channelId;

    /**
     * 回执推送地址
     */
    private String notifyUrl;

    /**
     * 模板ID
     */
    private String templateId;

    /**
     * cdr数据推送taskId
     */
    private String cdrPushTaskId;

    /**
     * 状态：等待回执，发送成功，发送失败
     */
    private String status;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 回执接收时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date receiveNotifyTime;
}
