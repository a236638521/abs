package com.m7.abs.api.domain.dto.midNum;

import com.m7.abs.api.projectHandler.midNum.IMidNumHandler;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MidNumReqDto {
    /**
     * 小号通道对应的handler
     */
    private IMidNumHandler midNumHandler;
    /**
     * 通道配置
     */
    private MidMumConfigDto midMumConfigDto;

    private String areaCode;

}
