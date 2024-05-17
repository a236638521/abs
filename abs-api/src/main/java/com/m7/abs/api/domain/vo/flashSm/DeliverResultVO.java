package com.m7.abs.api.domain.vo.flashSm;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * 小号绑定返回
 *
 * @author hulin
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeliverResultVO {
    private String sender;

    private String target;

    private String status;

    private String msg;

}
