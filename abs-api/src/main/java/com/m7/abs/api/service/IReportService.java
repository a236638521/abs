package com.m7.abs.api.service;

import com.m7.abs.api.domain.dto.flashSm.FlashSmTranslateDto;
import com.m7.abs.common.domain.entity.FlashSmDeliveryTaskEntity;
import com.m7.abs.common.domain.entity.MiddleNumberCdrEntity;

/**
 * 报表
 *
 * @author Kejie Peng
 * @date 2023年 03月28日 11:06:38
 */
public interface IReportService {
    /**
     * 推送消息至NSQ
     * @param cdrEntity
     */
    void pushMidNumCdrDataToNsq(MiddleNumberCdrEntity cdrEntity);

    /**
     * 推送闪信消息至NSQ
     * @param taskEntity
     * @param translateDto
     */
    void pushFlashSmsDataToNsq(FlashSmDeliveryTaskEntity taskEntity, FlashSmTranslateDto translateDto);
}
