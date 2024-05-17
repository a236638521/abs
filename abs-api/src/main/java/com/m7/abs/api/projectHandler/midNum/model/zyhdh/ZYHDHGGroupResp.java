package com.m7.abs.api.projectHandler.midNum.model.zyhdh;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

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
public class ZYHDHGGroupResp {
    /**
     * 绑定请求响应码：
     * 0000: 成功；
     * 1001：失败；
     * 1002：请求消息格式错误；
     * 1003：首次添加组未携带号码信息；
     * 1004：method方式暂不支持；
     * 1018：真实号码不正确；
     * 2000：找不到appId对应的应用；
     * 2001：多账户appId不能为空；
     * 1050：系统异常；
     */
    private String code;
    /**
     * 返回结果描述：
     * 例如：“成功”
     */
    private String message;

    /**
     * 根据请求返回的参数：
     * （1）请求消息的method参数值为‘3’时，返回集合包含的全量号码列表；
     * （2）请求消息的method参数值为
     * ‘4’时，返回集合的groupId；
     * （3）请求消息的method参数值为
     * ‘6’时，返回集合包含的全量号码列表；
     * 其他时候填空。
     */
    private String values;
}
