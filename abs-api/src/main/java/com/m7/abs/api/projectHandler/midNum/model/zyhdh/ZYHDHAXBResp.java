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
public class ZYHDHAXBResp {
    /**
     * 绑定请求响应码：
     * 0000: 成功；
     * 1001：绑定关系已存在；
     * 1002： 请求消息指定地区无可用中间号；
     * 1003：中间号号码池已达绑定上限；
     * 1004：请求消息过期时间参数无效（小于或等于0）；
     * 1005： 请求消息格式错误；
     * 1006： 请求消息参数有误；
     * 1008：企业不存在；
     * 1011：账户与中间号模式不匹配；
     * 1018：真实号码不正确；
     * 1111：该企业暂停服务；
     * 1113：入参必须指示要不录音；
     * 1114：入参必须指示要录音；
     * 2000：找不到appId对应的应用；
     * 2001：多账户appId不能为空；
     * 1050：系统异常；
     */
    private String code;
    private String message;
    /**
     * 绑定关系ID：
     * 唯一标示一组绑定关系，构成格式为：收到请求消息时间（YYYYMMDDHHMMSS格式）+14位随机数；
     */
    private String bindId;
    /**
     * 和多号平台生成绑定数据后返回的中间号，即AXB的X号码；
     * 格式遵循国际电信联盟定义的E.164标准,例如：8613803111234.
     * 手机号码格式：86 + 11位号码。
     */
    @JSONField(name = "x_no")
    private String x_no;
}
