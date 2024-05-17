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
public class MiGuHotlineManagerReq {
    /**
     * 操作类型：
     * 0：新增
     * 1：删除
     */
    private String actiontype;
    /**
     * 热线号码列表，默认不加密，需进行加密时，按指定的platformid进行AES（ECB-PCKS7-128位）加密，单条消息上限50
     * 明文参见附录中的管理号码格式定义
     */
    private List<String> hotlines;
}
