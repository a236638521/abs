package com.m7.abs.api.projectHandler.midNum.model.lianlian;

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
public class LianLianAXBCommonReq {
    private String corp_key;
    private String ts;
    private String sign;
}
