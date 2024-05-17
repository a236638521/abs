package com.m7.abs.api.projectHandler.midNum.model.zyhdh;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ZYHDHRecordReq {
    /**
     * 企业的应用账户id
     */
    private String appId;
    /**
     * 绑定关系ID，唯一确定一组绑定关系
     */
    private String bindId;
    /**
     * 通话ID，唯一确定一次通话
     */
    private String callId;
    /**
     * 绑定关系生成时接入商传入的订单ID，无则填空。
     */
    private String orderIds;
    /**
     * 绑定关系所属的号码集合ID。
     */
    private String groupId;
    /**
     * 主叫号码：
     * 格式遵循国际电信联盟定义的E.164标准
     * 例如：8613803111231
     */
    private String callNo;
    /**
     * 被叫号码：
     * 格式遵循国际电信联盟定义的E.164标准
     * 例如：8613803111233
     */
    private String peerNo;
    /**
     * 中间号码：
     * 格式遵循国际电信联盟定义的E.164标准
     * 例如：8613803111232
     */
    private String x;
    /**
     * 通话发生时间:
     * 14位YYYYMMDDHHMMSS格式，例如：20160906131516
     */
    private String callTime;
    /**
     * 通话开始时间：
     * 14位YYYYMMDDHHMMSS格式，例如：20160906131516
     */
    private String startTime;
    /**
     * 通话结束时间：
     * 14位YYYYMMDDHHMMSS格式，例如：20160906131816
     */
    private String finishTime;
    /**
     * 通话时长，单位秒。
     */
    private int callDuration;
    /**
     * 结束发起方:
     * 0: 平台结束；
     * 1：主叫结束；
     * 2：被叫结束；
     */
    private String finishType;
    /**
     * 结束状态（即挂断原因）：
     * 0： 无绑定关系
     * 1:	主叫挂机
     * 2:	被叫挂机
     * 3:	主叫放弃
     * 4:	被叫无应答
     * 5:	被叫忙
     * 6:	被叫不可及
     * 7:	路由失败
     * 8:	中间号状态异常
     * 9:	订单超过有效期（自1.9.1版本开始弃用）
     * 10:	平台系统异常
     * 11: 关机
     * 12: 停机
     * 13: 拒接
     * 14: 空号
     * 注：11-14状态值只出现在AS呼叫
     */
    private String finishState;
    /**
     * 回传参数
     */
    private String userData;
}
