package com.m7.abs.api.domain.dto.flashSm;

import com.m7.abs.api.projectHandler.flashSm.IFlashSmHandler;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlashSmReqDto {
    /**
     * 闪信通道对应的handler
     */
    private IFlashSmHandler flashSmHandler;
    /**
     * 通道配置
     */
    private FlashSmConfigDto flashSmConfigDto;
}
