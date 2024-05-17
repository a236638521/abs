package com.m7.abs.api.projectHandler.flashSm.model.migu;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class MiGuCallEventInfoType {
    /**
     * 呼叫标识
     * 注：
     * 需至少保证72小时内唯一；
     */
    private String callIdentifier;
    /**
     * 主叫号码
     * 默认不加密，需进行加密时，按指定的platformid进行AES（ECB-PCKS7-128位）加密，明文号码格式遵循国际电信联盟定义的E.164标准。参见附录中投递电话号码格式定义
     */
    private String scalling;
    /**
     * 被叫号码
     * 默认不加密，需进行加密时，按指定的platformid进行AES（ECB-PCKS7-128位）加密，明文参见附录中投递电话号码格式定义
     */
    private String called;
    /**
     * 呼叫流程
     * MO：主叫流程(去除国家码前缀后target必须和called相同)
     * MT：被叫流程(去除国家码前缀后target必须和scalling相同)
     */
    private String direction;
    /**
     * 呼叫状态事件(暂时不需要关注取值，有请求过来就触发)，取值如下：
     * IDP：呼叫开始（收到IDP或者INVITE请求）
     * Ringing：振铃（SIP 180临时响应）
     * Forwarding：发生前转（SIP 181临时响应）
     * Answer：用户应答
     * Busy：被叫忙
     * Not Reachable ：被叫不可达（SIP 480最终失败响应）
     * Route Failure ：路由失败（SIP其它最终失败响应）
     * No Answer：无应答
     * Abandon：主叫放弃
     * Release：主被叫通话后的正常挂机事件
     * Exception：呼叫过程中发生异常
     */
    private String event;
    /**
     * 呼叫事件发生的时间戳，UTC时间。
     * 格式：YYYY-MM-DDThh:mm:ss.SSSZ
     */
    private String timeStamp;
}
