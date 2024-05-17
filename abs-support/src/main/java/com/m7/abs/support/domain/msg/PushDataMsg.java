package com.m7.abs.support.domain.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhuhf
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PushDataMsg {

    /**
     * 任务 ID
     */
    private String taskId;

    /**
     * 请求 id
     */
    private String requestId;

    /**
     * 账户 id
     */
    private String accountId;

    /**
     * 项目 code
     */
    private String projectCode;
    /**
     * http url
     */
    private String url;

    /**
     * 推送的数据，http body data
     */
    private String data;
    /**
     * http method
     */
    private String method;
    /**
     * http header content-type
     */
    private String contentType;
    /**
     * 什么时候推送数据的时间戳
     */
    private Long sendTime;
    /**
     * 推送失败时的重试间隔时间，单位为秒(s)
     */
    private Long retryInterval;
}
