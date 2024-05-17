package com.m7.abs.api.projectHandler.midNum.model.tyzn;

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
public class TYZNAXBAndXBUnbindReq {

    /**
     * 虚拟号码
     */
    private String telX;
}
