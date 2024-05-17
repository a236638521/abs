package com.m7.abs.api.projectHandler.midNum.model.zyhdh;

import com.alibaba.fastjson.annotation.JSONField;
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
public class ZYHDHGXBUnbindResp {
    /**
     * 绑定关系解除结果：
     * 0000：成功；
     * 1001：绑定关系不存在；
     * 1002：订单已过期；
     * 113：暂未开通录音功能，请修改录音参数；
     * 2000：找不到appId对应的应用；
     * 2001：多账户appId不能为空；
     * 1050：系统异常；
     */
    private String code;
    /**
     * 返回结果描述
     */
    private String message;
    /**
     * 需要解绑的绑定ID（绑定关系生成时已在响应消息回传给接入平台）。
     */
    private String bindId;
    /**
     * 号码集合ID
     */
    private String groupId;
    /**
     * 请求解除GXB绑定关系的B号码；
     * 号码格式需遵循国际电信联盟定义的E.164标准。
     */
    private String telB;
    /**
     * 请求解除GXB关系中的X号码；
     * 号码格式需遵循国际电信联盟定义的E.164标准。
     */
    @JSONField(name = "x_no")
    private String x_no;

}
