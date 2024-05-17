package com.m7.abs.api.service.impl;

import com.m7.abs.api.common.properties.AbsApiProperties;
import com.m7.abs.api.common.utils.JsonComponent;
import com.m7.abs.api.domain.dto.flashSm.FlashSmTranslateDto;
import com.m7.abs.api.mapstruct.FlashSmsMapStructMapper;
import com.m7.abs.api.mapstruct.MidNumCdrMapStructMapper;
import com.m7.abs.api.service.IReportService;
import com.m7.abs.common.constant.keys.CommonSessionKeys;
import com.m7.abs.common.domain.dto.FlashSmReportDto;
import com.m7.abs.common.domain.dto.MiddleNumberCdrReport;
import com.m7.abs.common.domain.dto.NsqMsgDTO;
import com.m7.abs.common.domain.entity.FlashSmDeliveryTaskEntity;
import com.m7.abs.common.domain.entity.MiddleNumberCdrEntity;
import com.sproutsocial.nsq.Publisher;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

/**
 * 报表
 *
 * @author Kejie Peng
 * @date 2023年 03月28日 11:07:00
 */
@Slf4j
@Service
public class ReportServiceImpl implements IReportService {
    private Publisher publisher;
    private AbsApiProperties absApiProperties;
    private JsonComponent jsonComponent;
    private MidNumCdrMapStructMapper midNumCdrMapStructMapper;
    private FlashSmsMapStructMapper flashSmsMapStructMapper;

    public ReportServiceImpl(
            Publisher publisher,
            JsonComponent jsonComponent,
            AbsApiProperties absApiProperties,
            MidNumCdrMapStructMapper midNumCdrMapStructMapper,
            FlashSmsMapStructMapper flashSmsMapStructMapper) {
        this.publisher = publisher;
        this.jsonComponent = jsonComponent;
        this.absApiProperties = absApiProperties;
        this.midNumCdrMapStructMapper = midNumCdrMapStructMapper;
        this.flashSmsMapStructMapper = flashSmsMapStructMapper;
    }

    /**
     * 推送话单报表数据至NSQ
     *
     * @param cdrEntity
     */
    @Override
    public void pushMidNumCdrDataToNsq(MiddleNumberCdrEntity cdrEntity) {
        if (cdrEntity == null || !absApiProperties.getApi().getReport().isEnable()) {
            return;
        }
        MiddleNumberCdrReport middleNumberCdrReport = midNumCdrMapStructMapper.convertCdrEntityToCdrReport(cdrEntity);
        String traceId = MDC.get(CommonSessionKeys.REQ_ID_KEY);
        NsqMsgDTO<MiddleNumberCdrReport> msg = new NsqMsgDTO<>();
        msg.setTraceId(traceId);
        msg.setData(middleNumberCdrReport);
        byte[] data = jsonComponent.writeValueAsBytes(msg);
        String midNumCdrReportTopic = absApiProperties.getApi().getReport().getMidNumCdrReportTopic();
        log.info("[{}] 推送小号话单数据至nsq", midNumCdrReportTopic);
        try {
            publisher.publish(midNumCdrReportTopic, data);
        } catch (Exception e) {
            log.error("推送nsq消息异常", e);
        }
    }

    /**
     * 推送闪信话单报表
     *
     * @param taskEntity
     * @param translateDto
     */
    @Override
    public void pushFlashSmsDataToNsq(FlashSmDeliveryTaskEntity taskEntity, FlashSmTranslateDto translateDto) {
        if (translateDto == null || !absApiProperties.getApi().getReport().isEnable()) {
            return;
        }

        FlashSmReportDto reportDto = flashSmsMapStructMapper.convertFlashSmTranslateDtoToFlashSmReportDto(translateDto);
        reportDto.setTaskId(taskEntity.getId());
        reportDto.setAccountId(taskEntity.getAccountId());
        reportDto.setCreateTime(taskEntity.getCreateTime());

        String traceId = MDC.get(CommonSessionKeys.REQ_ID_KEY);
        NsqMsgDTO<FlashSmReportDto> msg = new NsqMsgDTO<>();
        msg.setTraceId(traceId);
        msg.setData(reportDto);
        byte[] data = jsonComponent.writeValueAsBytes(msg);
        String flashSmCdrReportTopic = absApiProperties.getApi().getReport().getFlashSmCdrReportTopic();
        log.info("[{}] 推送闪信话单数据至nsq", flashSmCdrReportTopic);
        try {
            publisher.publish(flashSmCdrReportTopic, data);
        } catch (Exception e) {
            log.error("推送nsq消息异常", e);
        }

    }
}
