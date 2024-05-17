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
public class MiGuDeliverResp {
    private String taskId;

    /**
     * 除了msgType=4和5外的最终投递结果
     */
    private List<MiGuDeliveryResult> processresult;
}
