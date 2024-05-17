package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-21
 */
@TableName("middle_number_bind_log_backup")
public class MiddleNumberBindLogBackupEntity {
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
     * 绑定类型 AXB;XB等
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "last_update_time", fill = FieldFill.UPDATE)
    private Date lastUpdateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "backup_time", fill = FieldFill.UPDATE)
    private Date backupTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getAssociationId() {
        return associationId;
    }

    public void setAssociationId(String associationId) {
        this.associationId = associationId;
    }

    public String getTelX() {
        return telX;
    }

    public void setTelX(String telX) {
        this.telX = telX;
    }

    public String getTelA() {
        return telA;
    }

    public void setTelA(String telA) {
        this.telA = telA;
    }

    public String getTelB() {
        return telB;
    }

    public void setTelB(String telB) {
        this.telB = telB;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    public boolean isCallRecording() {
        return callRecording;
    }

    public void setCallRecording(boolean callRecording) {
        this.callRecording = callRecording;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBindType() {
        return bindType;
    }

    public void setBindType(String bindType) {
        this.bindType = bindType;
    }

    public String getRecordReceiveUrl() {
        return recordReceiveUrl;
    }

    public void setRecordReceiveUrl(String recordReceiveUrl) {
        this.recordReceiveUrl = recordReceiveUrl;
    }

    public String getUserData() {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Date getBackupTime() {
        return backupTime;
    }

    public void setBackupTime(Date backupTime) {
        this.backupTime = backupTime;
    }
}
