package com.m7.abs.admin.domain.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

/**
 * 小号话单
 *
 * @author Kejie Peng
 * @date 2022年 11月14日 09:59:17
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MiddleNumberBindLogDTO {
    private String accountName;

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
     * 通道编码
     */
    private String channelCode;

    /**
     * 关联ID;和其他通道关联的ID
     */
    private String associationId;

    /**
     * 中间号
     */
    private String telX;
    /**
     * 分组ID,适用于GXB GXYB 模式
     */
    private String groupId;

    /**
     * A号码
     */
    private String telA;

    /**
     * B号码
     */
    private String telB;

    /**
     * 绑定过期时间;单位/秒
     */
    private Long expiration;

    /**
     * 是否录音;true:是;false:否,默认否
     */
    private boolean callRecording = false;

    /**
     * 状态;1:绑定中;2:已解绑
     */
    private String status;

    /**
     * 绑定类型 AXB;AX等
     */
    private String bindType;

    /**
     * 话单接收地址
     */
    private String recordReceiveUrl;

    /**
     * 用户数据
     */
    private String userData;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 绑定记录最后更新日志,系统会根据lastUpdateTime和status判断是否为无效数据,无效数据将在超出指定时间之后删除
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "last_update_time", fill = FieldFill.UPDATE)
    private Date lastUpdateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireTime;
}
