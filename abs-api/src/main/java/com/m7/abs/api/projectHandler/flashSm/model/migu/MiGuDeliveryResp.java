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
public class MiGuDeliveryResp {
    private String taskid;
    private List<MiGuDeliveryResultResp> deliveryresult;
}
