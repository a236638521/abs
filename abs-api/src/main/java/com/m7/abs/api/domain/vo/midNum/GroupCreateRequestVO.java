package com.m7.abs.api.domain.vo.midNum;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


/**
 * 
 *
 * @author hulin
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupCreateRequestVO {
    @NotEmpty
    @Size(max = 40)
    private String account;
    /**
     * 分组名字
     */
    @NotEmpty
    @Size(max = 40)
    private String name;
}
