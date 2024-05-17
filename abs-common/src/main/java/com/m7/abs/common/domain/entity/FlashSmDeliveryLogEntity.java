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
@TableName("flash_sm_delivery_log")
public class FlashSmDeliveryLogEntity {

    private static final long serialVersionUID = 1L;
    @TableId("id")
    private String id;

    /**
     * 账户ID
     */
    private String accountId;


    /**
     * 通道ID
     */
    private String channelId;

    /**
     * 发送内容
     */
    private String content;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 发送目标
     */
    private String target;

    /**
     * 发送者
     */
    private String sender;

    /**
     * 送达情况  1:短信发送成功   2:发送失败
     */
    private String status;
    /**
     * 发送结果
     */
    private String result;
    /**
     * 三方返回的发送结果msg
     */
    private String resultMsg;
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
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

}
