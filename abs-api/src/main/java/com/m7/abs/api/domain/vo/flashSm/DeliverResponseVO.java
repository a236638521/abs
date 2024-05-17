package com.m7.abs.api.domain.vo.flashSm;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

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
public class DeliverResponseVO {

    /**
     * 任务ID
     */
    @NotEmpty
    private String taskId;


    private List<DeliverResultVO> deliverResult;

}
