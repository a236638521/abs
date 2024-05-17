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
public class LianLianAXBReq extends LianLianAXBCommonReq {
    private String request_id;
    /**
     * 有效持续时间，即过expiration秒后AXB 关系失效自动解绑；
     */
    private Long expiration;
    /**
     * 号码A；18600008888或0108888999
     */
    private String tel_a;
    /**
     * 号码B；18600008888或0108888999
     */
    private String tel_b;
    /**
     * 指定虚拟号码
     */
    private String tel_x;
    /**
     * A拨打X时，B看到的外显号码  默认X
     */
    private String a_call_x_showout;
}
