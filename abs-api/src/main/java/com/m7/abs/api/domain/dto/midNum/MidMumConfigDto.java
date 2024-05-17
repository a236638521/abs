package com.m7.abs.api.domain.dto.midNum;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MidMumConfigDto {
    /**
     * 鉴权APPID
     */
    private String appId;
    /**
     * 鉴权access_key
     */
    private String accessKey;
    /**
     * 鉴权secret_key
     */
    private String secretKey;
    /**
     * 账户ID
     */
    private String accountId;
    /**
     * 通道ID
     */
    private String channelId;
    /**
     * 通道编码
     */
    private String channelCode;
    /**
     * 应用请求路径
     */
    private String basePath;

    public void setBasePath(String host, String port, String contextPath) {
        if (StringUtils.isNotEmpty(host)) {
            this.basePath = host;
        }
        if (StringUtils.isNotEmpty(port)) {
            this.basePath = this.basePath + ":" + port;
        }
        if (StringUtils.isNotEmpty(contextPath)) {
            this.basePath = this.basePath + "/" + contextPath;
        }
    }
}
