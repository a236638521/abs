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
public class GroupQueryNumberListRequestVO {
    @NotEmpty
    @Size(max = 40)
    private String account;
    /**
     * 分组ID
     */
    @NotEmpty
    @Size(max = 64)
    private String groupId;
}
