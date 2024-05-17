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
public class GroupDelRequestVO {
    @NotEmpty
    @Size(max = 40)
    private String account;
    /**
     * 分组ID
     */
    @NotEmpty
    @Size(max = 64)
    private String groupId;
    /**
     * 关联group id;和其他通道关联的ID
     */
    private String associationId;
}
