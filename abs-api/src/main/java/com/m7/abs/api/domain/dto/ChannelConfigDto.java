package com.m7.abs.api.domain.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChannelConfigDto {
    private String channelId;
    private String channelCode;
    /**
     * 环境;dev：开发环境；prod：生产环境
     */
    private String env;

    /**
     * https;0:否;1:是
     */
    private Integer https;

    /**
     * url host
     */
    private String host;

    /**
     * url port
     */
    private String port;

    /**
     * url context path
     */
    private String contextPath;
    private String appId;
    private String accessKey;
    private String secretKey;
}
