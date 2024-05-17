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
public class MiGuDeliveryResultResp {
   /**
    * 接收对象，默认不加密，需进行加密时，按指定的platformid进行AES（ECB-PCKS7-128位）加密，明文对应于请求消息中target
    */
   private String target;
   /**
    * 返回结果码
    * 参见附录中投递结果通知中的结果码定义
    */
   private String status;
   /**
    * 结果/原因描述，返回内容包含蓝色的英文注解、短信中心投递详细结果（投递闪信，如果短信中心返回，则有此字段值）、短信中心投递详细结果描述（投递闪信，如果短信中心返回，则有此字段值），以竖线分隔
    * 格式举例如下：
    * The flashSM_delivery succeed|000000|SUCCESS
    */
   private String msg;
   /**
    * 计费条数
    */
   private String chargeNum;
   /**
    * 交互USSD时，用户回复指令
    */
   private String reply;
   /**
    * 运营商
    * 1：移动
    * 2：联通
    * 3：电信
    */
   private String platform;
}
