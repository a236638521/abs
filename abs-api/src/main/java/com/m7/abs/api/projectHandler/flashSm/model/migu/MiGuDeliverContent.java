package com.m7.abs.api.projectHandler.flashSm.model.migu;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class MiGuDeliverContent {
    /**
     * 接收对象，默认不加密，需进行加密时，按指定的platformid进行AES（ECB-PCKS7-128位）加密，明文参见附录中投递电话号码格式定义
     */
    private String target;
    /**
     * 发送的模板类型编号（模板提前约定并保存在开放能力平台侧）
     */
    private String template;
    /**
     * 模板中需要填充的参数，长度要求详见3.1.1.5注意事项
     */
    private List<String> argv;
    /**
     * 会叫状态事件
     */
    private MiGuCallEventInfoType callEvent;
}
