package com.m7.abs.common.domain.vo.support;

import lombok.*;

import javax.validation.constraints.NotEmpty;

/**
 * @author KejiePeng
 * @Title:
 * @Description
 * @date 2021/12/14 10:39
 */
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DemoVO {
    @NotEmpty
    private String param1;
}
