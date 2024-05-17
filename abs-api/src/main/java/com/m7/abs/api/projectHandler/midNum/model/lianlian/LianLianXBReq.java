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
public class LianLianXBReq extends LianLianAXBCommonReq {
    private String request_id;
    /**
     * 有效持续时间，即过expiration秒后AXB 关系失效自动解绑；
     */
    private Long expiration;
    /**
     * 非tel号码呼叫 tel_x时，被叫tel(虚号绑定的真实号码使用者)看见的来电显示号码为如下取值:
     * 0：来电真实号码;（若使用，需找商务确认）
     * 1：系统随机分配虚号Y (AYB);
     * 2:tel_x
     * 3: 生成AXB关系，显示tel_x
     */
    private Integer model;
    /**
     * X绑定的真实被叫号码；13800008888或01088889999
     */
    private String tel;
    /**
     * 指定虚拟号码
     */
    private String tel_x;
}
