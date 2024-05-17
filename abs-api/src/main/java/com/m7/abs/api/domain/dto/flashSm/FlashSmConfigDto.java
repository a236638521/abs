package com.m7.abs.api.domain.dto.flashSm;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlashSmConfigDto {
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
     * 模板ID
     */
    private String templateId;
    /**
     * 通道编码
     */
    private String channelCode;
    /**
     * 应用请求路径
     */
    private String basePath;
    /**
     * 通道模板ID
     */
    private String channelTemplateId;

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
