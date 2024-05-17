package com.m7.abs.common.domain.base;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author KejiePeng
 * @Title:
 * @Description
 * @date 2021/12/14 10:25
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseRequest<T> {
    private String requestId;
    @Valid
    @NotNull
    private T param;
}
