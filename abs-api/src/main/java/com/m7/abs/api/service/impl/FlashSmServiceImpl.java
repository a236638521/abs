package com.m7.abs.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.m7.abs.api.domain.dto.ChannelConfigDto;
import com.m7.abs.api.domain.dto.flashSm.*;
import com.m7.abs.api.domain.vo.flashSm.DeliverRequestVO;
import com.m7.abs.api.domain.vo.flashSm.DeliverResponseVO;
import com.m7.abs.api.domain.vo.flashSm.DeliverResultVO;
import com.m7.abs.api.projectHandler.flashSm.FlashSmType;
import com.m7.abs.api.projectHandler.flashSm.IFlashSmHandler;
import com.m7.abs.api.service.*;
import com.m7.abs.common.constant.common.AccountPushUrlTypeEnum;
import com.m7.abs.common.constant.common.FlashSmDeliveryResultEnum;
import com.m7.abs.common.constant.common.FlashSmTaskStatusEnum;
import com.m7.abs.common.constant.error.code.ErrorCodeConstant;
import com.m7.abs.common.constant.keys.CommonSessionKeys;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.AccountPushUrlEntity;
import com.m7.abs.common.domain.entity.FlashSmDeliveryLogEntity;
import com.m7.abs.common.domain.entity.FlashSmDeliveryTaskEntity;
import com.m7.abs.common.domain.entity.FlashSmTemplateEntity;
import com.m7.abs.common.utils.FastJsonUtils;
import com.m7.abs.common.utils.MyStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FlashSmServiceImpl implements IFlashSmService {
    private Map<String, IFlashSmHandler> handlerMap;
    @Autowired
    private IChannelService channelService;
    @Autowired
    private IFlashSmTemplateService flashSmTemplateService;
    @Autowired
    private IFlashSmDeliveryTaskService flashSmDeliveryTaskService;
    @Autowired
    private IFlashSmDeliveryLogService flashSmDeliveryLogService;
    @Autowired
    @Lazy
    private IAsyncService asyncService;
    @Autowired
    private IAccountPushUrlService accountPushUrlService;
    @Autowired
    private IReportService reportService;

    @Autowired
    public void setHandleMap(List<IFlashSmHandler> handlers) {
        // 注入各种类型的handler
        handlerMap = handlers.stream().collect(
                Collectors.toMap(handler -> AnnotationUtils.findAnnotation(handler.getClass(), FlashSmType.class).channel(),
                        v -> v, (v1, v2) -> v1));
    }

    @Override
    public BaseResponse deliver(DeliverRequestVO requestVO) {

        BaseResponse<FlashSmReqDto> confInfo = getConfInfo(requestVO.getAccount(), requestVO.getTemplateNum());

        if (!ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode().equals(confInfo.getCode())) {
            return confInfo;
        }
        FlashSmReqDto reqDto = confInfo.getData();

        /**
         * 发送闪信
         */
        FlashSmConfigDto flashSmConfigDto = reqDto.getFlashSmConfigDto();
        BaseResponse<DeliverResponseVO> deliverResponse = reqDto.getFlashSmHandler().deliver(flashSmConfigDto, requestVO);
        //投递闪信成功后
        if (deliverResponse != null && ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode().equals(deliverResponse.getCode())) {
            /**
             * todo 发送记录入库
             */
            DeliverResponseVO data = deliverResponse.getData();

            List<DeliverResultVO> deliverResult = data.getDeliverResult();

            String taskId = MyStringUtils.randomUUID();
            FlashSmDeliveryTaskEntity taskEntity = FlashSmDeliveryTaskEntity.builder()
                    .id(taskId)
                    .accountId(flashSmConfigDto.getAccountId())
                    .channelTaskId(data.getTaskId())
                    .channelId(flashSmConfigDto.getChannelId())
                    .notifyUrl(requestVO.getNotifyUrl())
                    .templateId(flashSmConfigDto.getTemplateId())
                    .status(FlashSmTaskStatusEnum.WAITING_FOR_RECEIPT.getValue())
                    .createTime(new Date())
                    .build();
            flashSmDeliveryTaskService.save(taskEntity);

            if (deliverResult != null) {
                deliverResult.forEach(item -> {
                    FlashSmDeliveryLogEntity deliveryLogEntity = FlashSmDeliveryLogEntity.builder()
                            .id(MyStringUtils.randomUUID())
                            .accountId(flashSmConfigDto.getAccountId())
                            .channelId(flashSmConfigDto.getChannelId())
                            .content("")
                            .sender(item.getSender())
                            .taskId(taskId)
                            .target(item.getTarget())
                            .status(item.getStatus())
                            .createTime(new Date())
                            .build();
                    flashSmDeliveryLogService.save(deliveryLogEntity);
                });

            }

            DeliverResponseVO responseVO = DeliverResponseVO.builder()
                    .taskId(taskId)
                    .deliverResult(deliverResult)
                    .build();
            return BaseResponse.success(responseVO);
        } else {
            return deliverResponse;
        }
    }

    @Override
    public Object receiveRecord(String channelCode, Map<String, Object> requestVO) {
        //根据channelCode获取执行handler
        IFlashSmHandler handler = handlerMap.get(channelCode);
        if (handler == null) {
            return BaseResponse.fail("No such channelCode,please check your params!");
        }

        //TODO 翻译不同渠道返回的消息,转换成固定对象
        BaseResponse<FlashSmRecordRespDto> translateResult = handler.translateRecord(requestVO);
        String code = translateResult.getCode();
        FlashSmRecordRespDto flashSmRecordRespDto = translateResult.getData();

        log.info("translate result:{}", FastJsonUtils.toJSONString(translateResult));
        if (StringUtils.isNotEmpty(code) && ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode().equals(code)) {//翻译成功
            //TODO 处理闪信发送记录
            FlashSmTranslateDto translateDto = flashSmRecordRespDto.getTranslateDto();
            String channelTaskId = translateDto.getTaskId();


            LambdaQueryWrapper<FlashSmDeliveryTaskEntity> taskWrapper = new LambdaQueryWrapper<>();
            taskWrapper.eq(FlashSmDeliveryTaskEntity::getChannelTaskId, channelTaskId);
            taskWrapper.eq(FlashSmDeliveryTaskEntity::getStatus, FlashSmTaskStatusEnum.WAITING_FOR_RECEIPT.getValue());
            taskWrapper.last("limit 1");
            FlashSmDeliveryTaskEntity taskEntity = flashSmDeliveryTaskService.getOne(taskWrapper);

            if (taskEntity != null) {
                log.info("task info from db:{}", FastJsonUtils.toJSONString(taskEntity));
                String notifyUrl = taskEntity.getNotifyUrl();
                if (StringUtils.isNotEmpty(notifyUrl)) {
                    /**
                     * 异步通知其他平台闪信发送状态
                     */
                    asyncService.handlerFlashSmTaskNotice(MDC.get(CommonSessionKeys.REQ_ID_KEY), notifyUrl, taskEntity, translateDto);
                }

                List<AccountPushUrlEntity> accountPushUrlEntities = accountPushUrlService.getByAccountId(taskEntity.getAccountId(), AccountPushUrlTypeEnum.FLASH_SMS_CDR_PUSH_URL.getType());
                if (accountPushUrlEntities != null && accountPushUrlEntities.size() > 0) {
                    accountPushUrlEntities.forEach(item -> {
                        String url = item.getUrl();
                        asyncService.handlerFlashSmTaskNotice(MDC.get(CommonSessionKeys.REQ_ID_KEY), url, taskEntity, translateDto);
                    });
                }


                this.updateTaskInfo(translateDto, taskEntity);

                /**
                 * 推送消息至NSQ
                 */
                reportService.pushFlashSmsDataToNsq(taskEntity, translateDto);
            } else {
                log.info("未知闪信发送任务");
            }

        }


        if (flashSmRecordRespDto != null) {
            return flashSmRecordRespDto.getRespData();
        }
        return BaseResponse.fail("[" + channelCode + "] Save fail.");
    }

    /**
     * 更新task数据
     *
     * @param translateDto
     * @param taskEntity
     */
    private void updateTaskInfo(FlashSmTranslateDto translateDto, FlashSmDeliveryTaskEntity taskEntity) {
        LambdaQueryWrapper<FlashSmDeliveryLogEntity> logWrapper = new LambdaQueryWrapper<>();
        logWrapper.eq(FlashSmDeliveryLogEntity::getTaskId, taskEntity.getId());
        List<FlashSmDeliveryLogEntity> logs = flashSmDeliveryLogService.list(logWrapper);

        if (logs != null) {
            List<FlashSmDeliveryResultDto> deliveryResult = translateDto.getDeliveryResult();

            Map<String, FlashSmDeliveryResultDto> targetLogMap = new HashMap<>();

            if (deliveryResult != null) {
                for (FlashSmDeliveryResultDto resultDto : deliveryResult) {
                    targetLogMap.put(resultDto.getTarget(), resultDto);
                }
            }


            for (int i = 0; i < logs.size(); i++) {
                FlashSmDeliveryLogEntity logEntity = logs.get(i);
                if (logEntity != null) {
                    FlashSmDeliveryResultDto resultDto = targetLogMap.get(logEntity.getTarget());
                    if (resultDto != null) {
                        logEntity.setResult(resultDto.getStatus());
                        logEntity.setResultMsg(resultDto.getMsg());
                    } else {
                        logEntity.setResult(FlashSmDeliveryResultEnum.DELIVERY_FAILED.getValue());
                        logEntity.setResultMsg(FlashSmDeliveryResultEnum.DELIVERY_FAILED.getDescription());
                    }
                    logEntity.setUpdateTime(new Date());
                    flashSmDeliveryLogService.updateById(logEntity);
                }
            }

        }

        taskEntity.setStatus(FlashSmTaskStatusEnum.RECEIVED_SUCCESS.getValue());
        taskEntity.setReceiveNotifyTime(new Date());
        flashSmDeliveryTaskService.updateById(taskEntity);
    }

    private BaseResponse<FlashSmReqDto> getConfInfo(String accountId, String templateNum) {
        FlashSmTemplateEntity smsTemplateEntity = flashSmTemplateService.getFlashSmTemplateForDeliver(accountId, templateNum);
        if (smsTemplateEntity == null) {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.UNKNOWN_TEMPLATE);
        }

        //获取channel配置
        ChannelConfigDto channelConfigDto = channelService.getChannelConfigInfo(smsTemplateEntity.getChannelId(), smsTemplateEntity.getAccountId());

        if (channelConfigDto == null) {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.CHANNEL_CONFIG_NOT_FOUND);
        }

        //根据channelCode获取执行handler
        IFlashSmHandler handler = handlerMap.get(channelConfigDto.getChannelCode());
        if (handler == null) {
            return BaseResponse.fail("No such channelCode,please check your params!");
        }

        FlashSmConfigDto configDto = FlashSmConfigDto.builder()
                .appId(channelConfigDto.getAppId())
                .accountId(smsTemplateEntity.getAccountId())
                .accessKey(channelConfigDto.getAccessKey())
                .secretKey(channelConfigDto.getSecretKey())
                .channelId(channelConfigDto.getChannelId())
                .channelCode(channelConfigDto.getChannelCode())
                .templateId(smsTemplateEntity.getId())
                .channelTemplateId(smsTemplateEntity.getChannelTemplateId())
                .build();
        configDto.setBasePath(channelConfigDto.getHost(), channelConfigDto.getPort(), channelConfigDto.getContextPath());

        FlashSmReqDto reqDto = FlashSmReqDto.builder()
                .flashSmHandler(handler)
                .flashSmConfigDto(configDto)
                .build();

        return BaseResponse.success(reqDto);
    }
}
