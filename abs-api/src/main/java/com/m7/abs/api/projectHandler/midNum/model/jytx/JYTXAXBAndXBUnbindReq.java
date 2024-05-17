package com.m7.abs.api.projectHandler.midNum.model.jytx;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class JYTXAXBAndXBUnbindReq {

    /**
     * 必选 客户 ID(固定，方便合作方平台区分)
     */
    private String appId;

    /**
     * 必选 绑定唯一标识，该标识全局唯一
     */
    private String bindId;
}
