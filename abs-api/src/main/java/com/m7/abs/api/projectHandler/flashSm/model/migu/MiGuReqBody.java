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
public class MiGuReqBody<T> {
   private Object header;
   private T body;
}
