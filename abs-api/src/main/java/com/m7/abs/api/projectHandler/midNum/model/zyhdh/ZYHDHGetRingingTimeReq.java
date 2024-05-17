package com.m7.abs.api.projectHandler.midNum.model.zyhdh;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ZYHDHGetRingingTimeReq {
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
     * 振铃时间:
     * 14位YYYYMMDDHHMMSS格式，例如：20160906131516
     */
    private String ringTime;
    /**
     * 回传参数
     */
    private String userData;
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
    private String telX;
}
