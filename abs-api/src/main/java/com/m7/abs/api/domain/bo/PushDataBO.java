package com.m7.abs.api.domain.bo;

import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * @author kejie peng
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PushDataBO {

    /**
     * 账户 id
     */
    @NotBlank
    private String accountId;

    /**
     * 项目 code
     */
    @NotBlank
    private String projectCode;
    /**
     * http url
     */
    @NotBlank
    private String url;

    /**
     * 推送的数据，http body data
     */
    @NotBlank
    private String data;
    /**
     * http method
     */
    @NotBlank
    private String method;
    /**
     * http header content-type
     */
    @NotBlank
    private String contentType;
    /**
     * 什么时候推送数据的时间戳，毫秒
     */
    private Long sendTime;
    /**
     * 推送失败时的重试间隔时间，单位为秒(s)
     */
    private Long retryInterval;
}
