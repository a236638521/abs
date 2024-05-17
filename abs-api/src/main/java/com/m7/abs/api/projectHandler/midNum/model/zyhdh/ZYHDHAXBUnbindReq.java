package com.m7.abs.api.projectHandler.midNum.model.zyhdh;

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
public class ZYHDHAXBUnbindReq {
    /**
     * 绑定ID
     */
    private String bindId;
    private String appId;
}
