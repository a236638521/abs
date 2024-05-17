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
@TableName("middle_number_sms_cdr")
public class MiddleNumberSmsCdrEntity {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    private String accountId;
    /**
     * 绑定记录ID
     */
    private String mappingId;
    /**
     * 第三方通道绑定记录
     */
    private String channelBindId;
    /**
     * 第三方通道通话记录ID
     */
    private String channelRecordId;
    /**
     * 主叫号码
     */
    private String caller;
    /**
     * 被叫号码
     */
    private String callee;
    /**
     * 中间号
     */
    private String telX;
    /**
     * 短信发送结果
     */
    private String result;
    /**
     * 短信发送条数
     */
    private Integer smsNumber;
    /**
     * 呼叫开始时间 格式:yyyy-MM-dd HH:mm:ss
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date smsTime;
    /**
     * cdr推送第三方taskId;
     */
    private String cdrPushTaskId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
}
