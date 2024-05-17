package com.m7.abs.api.projectHandler.flashSm.model.migu;

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
public class MiGuHotlineResp {
    /**
     * 返回结果码，业务受理结果
     */
    private String ResultCode;
    /**
     * 响应描述信息
     */
    private String ResultDesc;
}
