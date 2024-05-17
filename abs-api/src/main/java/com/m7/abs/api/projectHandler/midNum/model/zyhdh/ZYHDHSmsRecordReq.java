package com.m7.abs.api.projectHandler.midNum.model.zyhdh;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ZYHDHSmsRecordReq {
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
     * 短信源号码：
     * 格式遵循国际电信联盟定义的E.164标准
     * 例如：8613803111231
     */
    private String callNo;
    /**
     * 短信目的号码：
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
     * 发送短信时间:
     * 14位YYYYMMDDHHMMSS格式，例如：20160906131516
     */
    private String smsTime;
    /**
     * 短信发送结果：
     * ‘0’: 短信网关返回发送失败；
     * ‘1’: 发送短信成功；
     * ‘2’: 等待短信结果超时；
     * ‘3’: 中间号状态不正确；
     * ‘4’: 企业状态不正确；
     * ‘5’: 企业未开启短信功能；
     * ‘6’: 无绑定关系；
     * ‘7’: 订单超过有效期；（自1.9.18版本开始弃用）
     * ‘8’: 订单状态不正确；
     * ‘9’:	平台系统异常；
     * ‘19’: 测试账号测试调用次数超出限额；
     * ‘20’: 短信内容含涉敏关键词；
     * ‘21’: 短信内容含违规关键词；
     */
    private String smsResult;
    /**
     * 短信条数（长短信被拆分的条数）
     */
    private int smsNumber;
    /**
     * 回传参数
     */
    private String userData;
}
