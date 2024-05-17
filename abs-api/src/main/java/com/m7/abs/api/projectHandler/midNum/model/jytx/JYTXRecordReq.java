package com.m7.abs.api.projectHandler.midNum.model.jytx;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JYTXRecordReq {

    /**
     * 客户 ID(固定，方便合作方平台区分)
     */
    private String appId;

    /**
     * 通话唯一标识 callId,由推送方生成保证全局唯一
     */
    private String callId;

    /**
     * 客户绑定 id。
     */
    private String bindId;

    /**
     * 回拨请求时请求携带的 requestId，原样返回
     */
    private String requestId;

    /**
     * 主叫号码(11 位手机号，号码前加 0086，如008613631686024)
     */
    private String caller;

    /**
     * 被叫号码，规则通 caller
     */
    private String callee;

    /**
     * 主叫通讯录直拨虚拟保护号码，规则通 caller
     */
    private String dstVirtualNum;

    /**
     * 虚拟保护号码平台收到呼叫时间
     */
    private String callCenterAcceptTime;

    /**
     * 被叫呼叫开始时间
     */
    private String startDstCallTime;

    /**
     * 被叫响铃开始时间
     */
    private String startDstRingTime;

    /**
     * 被叫接听时间
     */
    private String dstAcceptTime;

    /**
     * 用户挂机通话结束时间
     */
    private String endCallTime;

    /**
     * 通话最后状态： 0：未知状态 1：正常通话 2：查询呼叫转移被叫号异常 3：呼叫转移被叫未接通 4：呼叫转移被叫未接听
     */
    private String callEndStatus;

    /**
     * 主叫接通虚拟保护号码到通话结束通话时间
     */
    private String callerDuration;

    /**
     * 呼叫转接被叫接通到通话结束通话时间
     */
    private String calleeDuration;

    /**
     * 平台分配的 AYB 的绑定 Id,绑定模式为 AX+AYB 模式时,会返回此参数。
     */
    private String relateBindId;

    /**
     * true是 有录音
     */
    private Boolean callRecording;
    /**
     * 录音地址
     */
    private String recordUrl;
}
