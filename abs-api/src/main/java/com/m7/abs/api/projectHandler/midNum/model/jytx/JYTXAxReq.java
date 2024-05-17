package com.m7.abs.api.projectHandler.midNum.model.jytx;

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
public class JYTXAxReq {
    /**
     * 必选 客户 ID(固定，方便合作方平台区分)
     */
    private String appId;

    /**
     * 必选 绑定唯一标识，该标识全局唯一
     */
    private String bindId;

    /**
     * 可选 主叫号码(必须为 11 位手机号或固话，号码前加 0086， 如 008613631686024)，如果 caller 默认为空，则为小号模式。
     */
    private String caller;

    /**
     * 必选 被叫号码，规则同 caller
     */
    private String callee;

    /**
     * 必选 分配的直呼虚拟中间保护号码，规则同 caller
     */
    private String dstVirtualNum;

    /**
     * 可选 被叫显号，如果指定了该字段，被叫透传强显该号码，如果该字段为空，被叫默认显虚拟中间号，规则同 caller
     */
    private String calleeDisplayNum;

    /**
     * 可选来显控制字段，传值： 0 或缺省：被叫来显为中间号 1 ：caller 和 callee 被叫来显为真实号码 2 ：caller 被叫来显为真实号码 3 ：callee 被叫来显为真实号码
     */
    private String calldisplay;

    /**
     * 可选短信控制字段，传值： 0 或缺省：不支持短信 1：支持短信电信中间号接口介绍 V3.45
     */
    private String smscode;

    /**
     * 必选 主被叫+虚拟保护号码允许合作方最大 cache 存储时间(单位秒)，超过该时间绑定关系失效，拨叫无法接通
     */
    private String maxAge;

    /**
     * 必选 请求 ID，该 requestId 在后面话单和录音 URL 推送中原样带回
     */
    private String requestId;

    /**
     * 可选 状态回调通知地址，正式环境可以配置默认推送地址
     */
    private String statusUrl;

    /**
     * 可选 话单推送地址，不填推到默认协商地址
     */
    private String hangupUrl;

    /**
     * 可选 通话是否录音标志，不传:不录音，0：不录音，1:录音
     */
    private String record;

    /**
     * 可选 如果 record 传值为 1，则传值推送录音 URL
     */
    private String recordUrl;

    /**
     * 可选主叫个性化放音控制字段。放音编码一般包含 3 个场景的编码，不同编码以英文逗号分隔，
     * 例：”1,2,3”。第一个录音编码为 caller 作为被叫时，主叫在拨打转接 过程中所听到的个性化放音。
     * 第二个录音编码为 callee 作为被叫时，主叫在拨打转接 过程中所听到的个性化放音。第三个录音编码为无绑定放音编码，
     * 该放音场景无绑 定，该值无效，可以不传。
     * 注：该字段为可选，3 个放音编码同样为可选，可以根据场景在相应位置选填放音编码，无需放音的位置可以为空值或者字符”0”，如:”,2,3”，“1,,3”
     */
    private String anucode;

}
