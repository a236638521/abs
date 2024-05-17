package com.m7.abs.api.projectHandler.midNum.model.jytx;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JYTXResp {

    /**
     *  返回消息
     */
    private String meg;

    /**
     * 返回状态码： 200：成功
     *           201：号码已解绑
     *           400：参数异常
     *           500：落地放接口异常
     */
    private String status;

    /**
     * 绑定的中间号码
     */
    private String dstVirtualNum;
}
