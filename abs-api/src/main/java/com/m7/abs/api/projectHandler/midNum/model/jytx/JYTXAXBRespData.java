package com.m7.abs.api.projectHandler.midNum.model.jytx;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JYTXAXBRespData {

    /**
     * 绑定的中间号码
     */
    private String dstVirtualNum;
}
