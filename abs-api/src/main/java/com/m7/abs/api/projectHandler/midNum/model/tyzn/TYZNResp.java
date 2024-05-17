package com.m7.abs.api.projectHandler.midNum.model.tyzn;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TYZNResp<T> {
    private Integer code;
    private String message;
    private T data;
}
