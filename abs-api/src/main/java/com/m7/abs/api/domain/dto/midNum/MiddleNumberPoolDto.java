package com.m7.abs.api.domain.dto.midNum;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MiddleNumberPoolDto {
    /**
     * 账户ID
     */
    private String accountId;

    /**
     * 通道ID
     */
    private String channelId;

    /**
     * 中间号类型;AXB;AX;AXYB (多种模式用;号隔开)
     */
    private String type;

    /**
     * 区号
     */
    private String areaCode;
}
