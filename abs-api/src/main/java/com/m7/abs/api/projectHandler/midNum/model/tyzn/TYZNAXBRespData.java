package com.m7.abs.api.projectHandler.midNum.model.tyzn;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TYZNAXBRespData {
    /**
     * 请求唯一id
     */
    private String requestId;

    /**
     * 虚拟号码
     */
    private String telX;

    /**
     * 绑定唯一id
     */
    private String subId;
}
