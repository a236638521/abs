package com.m7.abs.api.projectHandler.midNum.model.lianlian;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString
public class LianLianXBUnbindReq extends LianLianAXBCommonReq {
    /**
     * 绑定ID
     */
    private String bind_id;
    /**
     * 虚号
     */
    private String tel_x;
}
