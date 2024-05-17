package com.m7.abs.api.service;

import com.m7.abs.api.domain.dto.flashSm.FlashSmTranslateDto;
import com.m7.abs.api.domain.dto.midNum.MidNumRecordUrlTranslateDto;
import com.m7.abs.api.domain.dto.midNum.MidNumSmsTranslateDto;
import com.m7.abs.api.domain.dto.midNum.MidNumTranslateDto;
import com.m7.abs.common.domain.entity.FlashSmDeliveryTaskEntity;
import com.m7.abs.common.domain.entity.MiddleNumberBindLogEntity;

/**
 * @author Kejie Peng
 */
public interface IAsyncService {

    /**
     * 处理小号通话记录
     * @param reqId
     * @param bindLogEntity
     * @param translateDto
     */
    void handleMidNumCdr(String reqId, MiddleNumberBindLogEntity bindLogEntity, MidNumTranslateDto translateDto);

    /**
     * 处理小号录音
     * @param reqId
     * @param bindLogEntity
     * @param recordDto
     */
    void handleMidNumRecordUrl(String reqId, MiddleNumberBindLogEntity bindLogEntity, MidNumRecordUrlTranslateDto recordDto);

    /**
     * 处理闪信话单推送任务
     * @param reqId
     * @param pushUrl
     * @param taskEntity
     * @param translateDto
     */
    void handlerFlashSmTaskNotice(String reqId, String pushUrl, FlashSmDeliveryTaskEntity taskEntity, FlashSmTranslateDto translateDto);

    /**
     * 发送闪信
     * @param reqId
     * @param flashSmAccountId
     * @param flashSmTemplateId
     * @param sender
     * @param target
     */
    void sendFlashSmAsync(String reqId, String flashSmAccountId, String flashSmTemplateId, String sender, String target);

    /**
     * 处理小号短信话单
     * @param reqId
     * @param bindLogEntity
     * @param recordDto
     */
    void handleMidNumSmsRecord(String reqId, MiddleNumberBindLogEntity bindLogEntity, MidNumSmsTranslateDto recordDto);
}
