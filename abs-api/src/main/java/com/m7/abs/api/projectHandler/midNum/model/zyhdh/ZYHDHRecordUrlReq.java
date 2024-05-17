package com.m7.abs.api.projectHandler.midNum.model.zyhdh;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ZYHDHRecordUrlReq {
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
     * 通话录音文件保存url
     */
    private String recordUrl;
    /**
     * 回传参数
     */
    private String userData;
}
