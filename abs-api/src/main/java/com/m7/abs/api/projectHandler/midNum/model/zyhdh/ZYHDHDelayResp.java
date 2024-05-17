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
public class ZYHDHDelayResp {
    /**
     * 状态码。
     * 0000：成功；
     * 1001：绑定关系不存在；
     * 1002：订单已过期；
     * 1005：请求消息格式错误；
     * 1008：企业不存在；
     * 1011：账户与中间号模式不匹配；
     * 1111：该企业暂停服务；
     * 2000：找不到appId对应的应用；
     * 2001：多账户appId不能为空；
     * 1050：系统异常；
     */
    private String code;
    private String message;
}
