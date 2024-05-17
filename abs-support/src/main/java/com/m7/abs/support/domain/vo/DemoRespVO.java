package com.m7.abs.support.domain.vo;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
public class DemoRespVO {
    @NotEmpty
    private String param1;
}
