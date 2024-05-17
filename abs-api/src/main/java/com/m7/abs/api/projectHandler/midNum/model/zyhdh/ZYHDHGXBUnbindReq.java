package com.m7.abs.api.projectHandler.midNum.model.zyhdh;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 *
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class ZYHDHGXBUnbindReq {
    private String appId;
    /**
     * 号码集合ID：
     * 生成号码集合时生成的号码集合编号，参数具体定义参见“3.1 号码集合管理接口”请求消息的groupId参数说明。
     */
    private String groupId;
    /**
     * 需要解绑的绑定ID（绑定关系生成时已在响应消息回传给接入平台）。
     */
    private String bindId;

}
