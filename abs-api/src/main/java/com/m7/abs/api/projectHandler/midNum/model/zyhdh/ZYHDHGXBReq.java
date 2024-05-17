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
public class ZYHDHGXBReq {
    private String appId;
    private String orderId;
    /**
     * 号码集合ID：
     * 生成号码集合时生成的号码集合编号，参数具体定义参见“3.1 号码集合管理接口”请求消息的groupId参数说明。
     */
    private String groupId;
    /**
     * 与groupId号码集合有重叠号码的其他groupId编号：
     * 数组格式，无则填空。
     */
    private String acrossGroupId;
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
