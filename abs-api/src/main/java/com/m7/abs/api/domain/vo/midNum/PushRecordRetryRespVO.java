package com.m7.abs.api.domain.vo.midNum;

import lombok.*;

import java.util.List;


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
public class PushRecordRetryRespVO {
    private List<PushRecordRetryInfoVO> success;
    private List<PushRecordRetryInfoVO> fail;
}
