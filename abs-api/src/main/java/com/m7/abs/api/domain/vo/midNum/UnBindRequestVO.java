package com.m7.abs.api.domain.vo.midNum;

import lombok.*;

import javax.validation.constraints.NotEmpty;


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
public class UnBindRequestVO {
    /**
     * AXB或者AX绑定关系id:此ID为ABS系统绑定ID
     */
    @NotEmpty
    private String mappingId;

    /**
     * 绑定关系ID:此ID为第三方通道绑定ID,和mappingId关联
     */
    private String associationId;

    /**
     * 第三方groupId
     */
    private String associationGroupId;

    private String telX;
}
