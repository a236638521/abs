package com.m7.abs.api.domain.vo.midNum;

import lombok.*;


/**
 * AXB绑定请求参数
 *
 * @author hulin
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PushRecordRetryInfoVO {
    private String recorderId;
    private String msg;
    private String code;


}
