package com.m7.abs.api.projectHandler.midNum.model.lianlian;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LianLianResp<T> {
    private Integer code;
    private String message;
    private T data;
}
