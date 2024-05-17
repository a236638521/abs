package com.m7.abs.common.domain.dto;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author Kejie Peng
 * @date 2023年 03月28日 14:00:59
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NsqMsgDTO<T> {
    private String traceId;
    private T data;
}
