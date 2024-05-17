package com.m7.abs.api.projectHandler.midNum.model.zyhdh;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 *
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class ZYHDHAXBReq {
    private String appId;
    private String requestId;
    /**
     * A号码
     */
    private String telA;
    /**
     * B号码
     */
    private String telB;
    /**
     * 区号，传递X号码时可不传，传递时后选择该区号的X号码
     */
    private Integer areaCode;
    /**
     * 绑定过期时间，单位秒，0代表永久绑定
     */
    private Integer expiration;
    /**
     * 录音控制，0：不开通 1：开通，默认0，不开通
     */
    private String record;
    /**
     * 自定义字段
     */
    private String userData;

}
