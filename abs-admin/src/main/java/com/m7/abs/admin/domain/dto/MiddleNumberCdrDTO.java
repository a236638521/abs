package com.m7.abs.admin.domain.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.m7.abs.common.utils.MyStringUtils;
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
public class MiddleNumberCdrDTO {
    private String accountName;

    private String id;

    private String accountId;
    /**
     * 绑定记录ID
     */
    private String mappingId;
    /**
     * 分组ID,适用于GXB
     */
    private String groupId;
    /**
     * 第三方通道分组ID,适用于GXB
     */
    private String channelGroupId;
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
     * 通话结果
     */
    private String result;
    /**
     *
     */
    private String callDisplay;
    /**
     * 主叫显示号码
     */
    private String callerShow;
    /**
     * 被叫显示号码
     */
    private String calledShow;
    /**
     * 主叫区号
     */
    private String callerArea;
    /**
     * 被叫区号
     */
    private String calledArea;
    /**
     * 主叫运营商
     */
    private String callerCarrier;
    /**
     * 被叫运营商
     */
    private String calledCarrier;
    /**
     * 是否录音
     */
    private boolean callRecording;
    /**
     * 通话时长
     */
    private long billDuration;
    /**
     * 呼叫开始时间 格式:yyyy-MM-dd HH:mm:ss
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;
    /**
     * 振铃时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date alertingTime;
    /**
     * 接听时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date connectTime;
    /**
     * 挂断时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date releaseTime;
    /**
     * 录音文件代理地址
     */
    private String recordFileProxy;
    /**
     * 录音文件host
     */
    private String recordFileHost;
    /**
     * 录音文件路径
     */
    private String recordFilePath;

    /**
     * oss录音转存taskId;
     */
    private String ossTaskId;

    /**
     * cdr推送第三方taskId;
     */
    private String cdrPushTaskId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 挂机放:
     * 0:平台结束;
     * 1:主叫结束;
     * 2:被叫结束;
     * 3:未知;
     */
    private String releaseDir;

    public String getCaller() {
        return MyStringUtils.encryptPhoneNumber(caller);
    }

    public String getCallee() {
        return MyStringUtils.encryptPhoneNumber(callee);
    }

    public String getTelX() {
        return MyStringUtils.encryptPhoneNumber(telX);
    }
}
