package com.m7.abs.api.domain.vo.flashSm;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliverContentVO {
    /**
     * 发送者,非必填.目前此参数用于咪咕闪信
     */
    private String sender;
    /**
     * 发送目标
     */
    private String target;

    private List<String> args;
}
