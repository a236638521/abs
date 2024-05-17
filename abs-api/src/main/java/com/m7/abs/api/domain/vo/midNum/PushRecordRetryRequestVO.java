package com.m7.abs.api.domain.vo.midNum;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


/**
 * AXB绑定请求参数
 *
 * @author hulin
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PushRecordRetryRequestVO {
    /**
     * 话单IDs
     */
    @NotNull
    @Size(max = 20)
    private List<String> recorderIds;

}
