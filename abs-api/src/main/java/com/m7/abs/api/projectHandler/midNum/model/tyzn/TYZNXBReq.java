package com.m7.abs.api.projectHandler.midNum.model.tyzn;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * @program: abs
 * @description:
 * @author: yx
 * @create: 2022-02-15 15:54
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class TYZNXBReq {

    /**
     * A号码
     */
    private String telA;

    /**
     * 虚拟号码
     */
    private String telX;

    /**
     * 回呼放音编码，数字编码由平台生成
     */
    private String anucode;

    /**
     * 绑定过期时间，单位秒，0代表永久绑定。默认永久绑定
     */
    private String expiration;

    /**
     * 转接号码的来显控制，0:显示虚拟号码 1:显示真实号码。 默认为 0(不显示真实号码)。
     */
    private String callDisplay;

    /**
     * 录音控制，0：不开通 1：开通，默认0，不开通
     */
    private String callRecording;

    /**
     * 运营商自定义字段
     */
    private String remark;


}
