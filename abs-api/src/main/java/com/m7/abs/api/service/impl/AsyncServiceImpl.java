package com.m7.abs.api.service.impl;

import com.m7.abs.api.common.properties.AbsApiProperties;
import com.m7.abs.api.delayed.bean.RetryBean;
import com.m7.abs.api.delayed.bean.UpdateMidNumCdrOssIdBean;
import com.m7.abs.api.delayed.queue.MidNumUpdateDelayedQueue;
import com.m7.abs.api.domain.bo.SaveToOssReqBO;
import com.m7.abs.api.domain.dto.flashSm.FlashSmDispatcherDto;
import com.m7.abs.api.domain.dto.flashSm.FlashSmTranslateDto;
import com.m7.abs.api.domain.dto.midNum.*;
import com.m7.abs.api.domain.vo.flashSm.DeliverContentVO;
import com.m7.abs.api.domain.vo.flashSm.DeliverRequestVO;
import com.m7.abs.api.domain.vo.midNum.SaveToOssVO;
import com.m7.abs.api.domain.vo.midNum.StorageFileBox;
import com.m7.abs.api.mapstruct.FlashSmsMapStructMapper;
import com.m7.abs.api.mapstruct.MidNumCdrMapStructMapper;
import com.m7.abs.api.mapstruct.MidNumSmsMapStructMapper;
import com.m7.abs.api.service.*;
import com.m7.abs.common.constant.common.*;
import com.m7.abs.common.constant.keys.CommonSessionKeys;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.base.PhoneInfo;
import com.m7.abs.common.domain.entity.*;
import com.m7.abs.common.utils.FastJsonUtils;
import com.m7.abs.common.utils.MyStringUtils;
import com.m7.abs.common.utils.PhoneUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AsyncServiceImpl implements IAsyncService {
    @Autowired
    private ISupportService supportService;
    @Autowired
    private IMiddleNumberCdrService middleNumberCdrService;
    @Autowired
    private IMiddleNumberSmsCdrService middleNumberSmsCdrService;
    @Autowired
    private IFlashSmDeliveryTaskService flashSmDeliveryTaskService;
    @Resource
    private AbsApiProperties absApiProperties;
    @Autowired
    private IFlashSmService flashSmService;
    @Autowired
    private IFlashSmTemplateService flashSmTemplateService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IAccountPushUrlService accountPushUrlService;
    @Autowired
    private IEnterpriseConfService enterpriseConfService;
    @Autowired
    private IReportService reportService;
    @Autowired
    private MidNumCdrMapStructMapper midNumCdrMapStructMapper;
    @Autowired
    private FlashSmsMapStructMapper flashSmsMapStructMapper;
    @Autowired
    private MidNumSmsMapStructMapper midNumSmsMapStructMapper;
    @Autowired
    private MidNumUpdateDelayedQueue midNumUpdateDelayedQueue;


    @Override
    @Async("cdrRequestExecutor")
    public void handleMidNumCdr(String reqId, MiddleNumberBindLogEntity bindLogEntity, MidNumTranslateDto translateDto) {
        MDC.put(CommonSessionKeys.REQ_ID_KEY, reqId);
        String recordId = MyStringUtils.randomUUID();
        try {
            /**
             * 处理归属地信息
             */
            String callerArea = translateDto.getCallerArea();
            String calledArea = translateDto.getCalledArea();
            String caller = translateDto.getCaller();
            String callee = translateDto.getCallee();
            try {

                if (StringUtils.isNotEmpty(caller) && StringUtils.isEmpty(callerArea)) {
                    PhoneInfo callerPhoneInfo = PhoneUtil.getPhoneInfo(caller, 86);
                    if (callerPhoneInfo != null) {
                        translateDto.setCallerArea(callerPhoneInfo.getDescription());
                        CarrierTypeEnum carrier = callerPhoneInfo.getCarrier();
                        if (carrier != null) {
                            translateDto.setCallerCarrier(carrier.getType());
                        }
                    }
                }
                if (StringUtils.isNotEmpty(callee) && StringUtils.isEmpty(calledArea)) {
                    PhoneInfo calleePhoneInfo = PhoneUtil.getPhoneInfo(callee, 86);
                    if (calleePhoneInfo != null) {
                        translateDto.setCalledArea(calleePhoneInfo.getDescription());
                        CarrierTypeEnum carrier = calleePhoneInfo.getCarrier();
                        if (carrier != null) {
                            translateDto.setCalledCarrier(carrier.getType());
                        }
                    }
                }
            } catch (Exception e) {
                log.error("[" + caller + " " + callee + "] get phone area info error");
            }

            MidNumRecordDto recordDto = midNumCdrMapStructMapper.convertMidNumTranslateDtoToMidNumRecordDto(translateDto);

            //bindLog 赋值
            recordDto.setCallRecording(bindLogEntity.isCallRecording());
            recordDto.setAccountId(bindLogEntity.getBillAccountId());
            recordDto.setMappingId(bindLogEntity.getId());
            recordDto.setUserData(bindLogEntity.getUserData());
            recordDto.setRecorderId(recordId);

            long billDuration = recordDto.getBillDuration();
            long rateDuration = getRateDuration(billDuration);
            recordDto.setRateDuration(rateDuration);

            if (BindTypeEnum.GXB.getType().equals(bindLogEntity.getBindType())) {
                recordDto.setGroupId(bindLogEntity.getGroupId());
            }

            /**
             * 保存通话记录
             */
            MiddleNumberCdrEntity cdrEntity = MiddleNumberCdrEntity.builder()
                    .id(recordId)
                    .channelBindId(translateDto.getChannelBindId())
                    .channelRecordId(translateDto.getChannelRecordId())
                    .channelGroupId(translateDto.getChannelGroupId())
                    .accountId(bindLogEntity.getAccountId())
                    .groupId(recordDto.getGroupId())
                    .mappingId(recordDto.getMappingId())
                    .caller(recordDto.getCaller())
                    .callee(recordDto.getCallee())
                    .telX(recordDto.getTelX())
                    .result(recordDto.getResult())
                    .callDisplay(recordDto.getCallDisplay())
                    .callerShow(recordDto.getCallerShow())
                    .calledShow(recordDto.getCalledShow())
                    .callerArea(recordDto.getCallerArea())
                    .calledArea(recordDto.getCalledArea())
                    .callerCarrier(recordDto.getCallerCarrier())
                    .calledCarrier(recordDto.getCalledCarrier())
                    .callRecording(recordDto.isCallRecording())
                    .billDuration(billDuration)
                    .rateDuration(rateDuration)
                    .beginTime(recordDto.getBeginTime())
                    .alertingTime(recordDto.getAlertingTime())
                    .connectTime(recordDto.getConnectTime())
                    .releaseTime(recordDto.getReleaseTime())
                    .releaseDir(recordDto.getReleaseDir())
                    .createTime(new Date())
                    .build();


            /**
             * 1.获取通话记录录音文件地址
             * 2.调用abs-support存储录音文件,拿到自有录音地址,替换话单内容
             */
            try {
                /**
                 * 正常挂断,并且设置录音才会触发转存录音文件
                 */
                if (bindLogEntity.isCallRecording()
                        && StringUtils.isNotEmpty(recordDto.getResult())
                        && recordDto.getResult().equals(MidNumCallStatusEnum.NORMAL_HANGUP.getValue())
                        && (billDuration > 0L)) {
                    EnterpriseConfEntity enterpriseConf = enterpriseConfService.getEnterpriseConfByAccountId(bindLogEntity.getBillAccountId());
                    //如果未查询到企业配置,使用默认配置
                    if (enterpriseConf == null) {
                        enterpriseConf = EnterpriseConfEntity.builder()
                                .ossPushFilePathOnlyEnable(false)
                                .ossProxyEnable(false)
                                .build();
                    }

                    List<String> storageConfList = enterpriseConf.getStorageConfList();
                    if (storageConfList != null && storageConfList.size() > 0) {
                        saveRecordFile(bindLogEntity, translateDto, recordDto, cdrEntity, enterpriseConf);
                    } else {
                        log.info("account have no storage conf");
                    }


                }
            } catch (Exception e) {
                log.error("push record to oss error.", e);
            }


            /**
             * 1.从绑定记录中获取调用方的话单回调地址
             * 2.将数据推送至abs-support 服务进行话单推送
             */
            String pushUrl = bindLogEntity.getRecordReceiveUrl();
            try {
                if (StringUtils.isNotEmpty(pushUrl)) {
                    String cdrPushTaskId = supportService.pushCdrDataToOtherPlatform(ProjectType.MID_NUM, pushUrl, bindLogEntity.getAccountId(), recordDto);
                    cdrEntity.setCdrPushTaskId(cdrPushTaskId);
                }

            } catch (Exception e) {
                log.error("push cdr to other platform error.", e);
            }

            log.info("Save mid num record:" + FastJsonUtils.toJSONString(cdrEntity));
            middleNumberCdrService.save(cdrEntity);
            /**
             * 推送消息至NSQ
             */
            reportService.pushMidNumCdrDataToNsq(cdrEntity);
        } catch (Exception e) {
            log.error("Insert mid num cdr error.", e);
        } finally {
            MDC.remove(CommonSessionKeys.REQ_ID_KEY);
        }

    }


    /**
     * 转存录音文件
     *
     * @param bindLogEntity
     * @param translateDto
     * @param recordDto
     * @param cdrEntity
     * @param enterpriseConf
     */
    private void saveRecordFile(MiddleNumberBindLogEntity bindLogEntity, MidNumTranslateDto translateDto, MidNumRecordDto recordDto, MiddleNumberCdrEntity cdrEntity, EnterpriseConfEntity enterpriseConf) {
        /**
         * 如果recordFileUrl是空的,同样会返回录音下载地址.只不过下载地址是无效的
         */
        List<String> storageConfList = enterpriseConf.getStorageConfList();
        SaveToOssReqBO saveToOssReqBO = SaveToOssReqBO.builder()
                .storageIds(storageConfList)
                .recordFileUrl(recordDto.getRecordFileUrl())
                .accountId(bindLogEntity.getAccountId())
                .beginTime(bindLogEntity.getCreateTime())
                .recorderId(translateDto.getChannelRecordId())
                .build();
        SaveToOssVO saveToOssVO = supportService.pushRecordToOssMidNumRecordDto(ProjectType.MID_NUM, saveToOssReqBO);
        if (saveToOssVO != null) {
            List<StorageFileBox> storageConf = saveToOssVO.getStorageConf();

            Optional<StorageFileBox> pushStorageOptional = storageConf.stream().filter(conf -> absApiProperties.getApi().getOss().getPushStorageId().equals(conf.getStorageId())).findFirst();
            Optional<StorageFileBox> saveCdrStorageOptional = storageConf.stream().filter(conf -> absApiProperties.getApi().getOss().getSaveCdrStorageId().equals(conf.getStorageId())).findFirst();

            String pushHostName = null;
            String cdrHostName = null;
            if (pushStorageOptional != null && pushStorageOptional.isPresent()) {
                pushHostName = pushStorageOptional.get().getFileHost();
            }
            if (saveCdrStorageOptional != null && saveCdrStorageOptional.isPresent()) {
                cdrHostName = saveCdrStorageOptional.get().getFileHost();
            }

            String filePath = saveToOssVO.getFilePath();
            String fileHost = pushHostName;
            /**
             * 设置代理服务地址,有些客户需要ip鉴权,使用代理服务器进行ip授权
             * 默认此选项不开启
             */
            if (enterpriseConf.getOssProxyEnable()) {
                Map<String, String> absApiOssProxyMapping = absApiProperties.getApi().getOssProxy().getMapping();
                if (absApiOssProxyMapping != null && StringUtils.isNotEmpty(pushHostName)) {
                    String proxyHost = absApiOssProxyMapping.get(pushHostName);
                    if (StringUtils.isNotEmpty(proxyHost)) {
                        fileHost = proxyHost;
                        cdrEntity.setRecordFileProxy(proxyHost);
                    }
                }
            }

            boolean midNumPushFilePathOnly = enterpriseConf.getOssPushFilePathOnlyEnable();
            if (midNumPushFilePathOnly) {
                recordDto.setRecordFileUrl(filePath);
            } else {
                recordDto.setRecordFileUrl(fileHost + filePath);
            }

            //TODO 拿到taskId 入库,保存转存记录
            cdrEntity.setOssTaskId(saveToOssVO.getTaskId());
            cdrEntity.setRecordFileHost(cdrHostName);
            cdrEntity.setRecordFilePath(filePath);
        }
    }

    @Override
    @Async("recordFileRequestExecutor")
    public void handleMidNumRecordUrl(String reqId, MiddleNumberBindLogEntity bindLogEntity, MidNumRecordUrlTranslateDto recordDto) {
        MDC.put(CommonSessionKeys.REQ_ID_KEY, reqId);
        recordDto.setAccountId(bindLogEntity.getAccountId());
        try {
            /**
             * 1.获取通话记录录音文件地址
             * 2.调用abs-support存储录音文件,拿到自有录音地址,替换话单内容
             */
            String ossTaskId = "";
            try {
                if (bindLogEntity.isCallRecording() && StringUtils.isNotEmpty(recordDto.getRecordFileUrl())) {
                    EnterpriseConfEntity enterpriseConf = enterpriseConfService.getEnterpriseConfByAccountId(bindLogEntity.getBillAccountId());
                    //如果未查询到企业配置,使用默认配置
                    if (enterpriseConf == null) {
                        enterpriseConf = EnterpriseConfEntity.builder()
                                .ossPushFilePathOnlyEnable(false)
                                .ossProxyEnable(false)
                                .build();
                    }


                    List<String> storageConfList = enterpriseConf.getStorageConfList();
                    if (storageConfList != null && storageConfList.size() > 0) {
                        /**
                         * 如果recordFileUrl是空的,同样会返回录音下载地址.只不过下载地址是无效的
                         */
                        SaveToOssReqBO saveToOssReqBO = SaveToOssReqBO.builder()
                                .storageIds(storageConfList)
                                .recordFileUrl(recordDto.getRecordFileUrl())
                                .accountId(bindLogEntity.getAccountId())
                                .beginTime(bindLogEntity.getCreateTime())
                                .recorderId(recordDto.getChannelRecordId())
                                .build();

                        SaveToOssVO saveToOssVO = supportService.pushRecordToOssMidNumRecordDto(ProjectType.MID_NUM, saveToOssReqBO);
                        if (saveToOssVO != null) {
                            ossTaskId = saveToOssVO.getTaskId();
                            //TODO 拿到taskId 入库,保存转存记录
                            if (StringUtils.isNotEmpty(ossTaskId) && StringUtils.isNotEmpty(bindLogEntity.getId())) {
                                RetryBean retryBean = RetryBean.builder()
                                        .traceId(reqId)
                                        .currentCount(1)
                                        .maxTime(5)
                                        .data(UpdateMidNumCdrOssIdBean.builder()
                                                .ossTaskId(ossTaskId)
                                                .channelRecordId(recordDto.getChannelRecordId())
                                                .build())
                                        .build();
                                /**
                                 * 延迟1秒更新数据库
                                 */
                                midNumUpdateDelayedQueue.addDelayedMsg(retryBean, 1, TimeUnit.SECONDS);
                            }
                        }
                    } else {
                        log.info("account have no storage conf");
                    }
                }
            } catch (Exception e) {
                log.error("push record to oss error.", e);
            }

        } catch (Exception e) {
            log.error("handle mid num record url error.", e);
        } finally {
            MDC.remove(CommonSessionKeys.REQ_ID_KEY);
        }
    }

    @Override
    @Async("flashSmsExecutor")
    public void handlerFlashSmTaskNotice(String reqId, String pushUrl, FlashSmDeliveryTaskEntity taskEntity, FlashSmTranslateDto translateDto) {
        MDC.put(CommonSessionKeys.REQ_ID_KEY, reqId);
        FlashSmDispatcherDto dispatcherDto = flashSmsMapStructMapper.convertFlashSmTranslateDtoToFlashSmDispatcherDto(translateDto);
        dispatcherDto.setTaskId(taskEntity.getId());
        dispatcherDto.setAccountId(taskEntity.getAccountId());

        /**
         * 1.从绑定记录中获取调用方的话单回调地址
         * 2.将数据推送至abs-support 服务进行推送
         */
        try {
            if (StringUtils.isNotEmpty(pushUrl)) {
                String cdrPushTaskId = supportService.pushCdrDataToOtherPlatform(ProjectType.FLASH_SMS, pushUrl, taskEntity.getAccountId(), dispatcherDto);
                taskEntity.setCdrPushTaskId(cdrPushTaskId);
                flashSmDeliveryTaskService.updateById(taskEntity);
            }
        } catch (Exception e) {
            log.error("push cdr to other platform error.", e);
        }
        MDC.remove(CommonSessionKeys.REQ_ID_KEY);
    }


    @Override
    @Async("flashSmsExecutor")
    public void sendFlashSmAsync(String reqId, String flashSmAccountId, String flashSmTemplateId, String sender, String target) {
        MDC.put(CommonSessionKeys.REQ_ID_KEY, reqId);
        FlashSmTemplateEntity flashSmTemplateEntity = flashSmTemplateService.getById(flashSmTemplateId);
        AccountEntity flashSmAccount = accountService.getById(flashSmAccountId);
        if (flashSmAccount == null) {
            log.info("account: {} 账户未配置闪信账户", flashSmAccountId);
        } else if (flashSmTemplateEntity == null) {
            log.info("account: {} 账户未配置闪信模板", flashSmAccountId);
        } else {

            List<DeliverContentVO> deliveryList = new ArrayList<>();
            deliveryList.add(DeliverContentVO.builder()
                    .sender(sender)
                    .target(target)
                    .build());
            DeliverRequestVO flashSmRequestVO = DeliverRequestVO.builder()
                    .account(flashSmAccount.getBillAccountId())
                    .templateNum(flashSmTemplateEntity.getTemplateNumber())
                    .deliveryList(deliveryList)
                    .build();


            log.info("Send flash sm:{}", FastJsonUtils.toJSONString(flashSmRequestVO));
            BaseResponse deliver = flashSmService.deliver(flashSmRequestVO);
            log.info("flash sms response:{}", FastJsonUtils.toJSONString(deliver));
        }
        MDC.remove(CommonSessionKeys.REQ_ID_KEY);
    }

    @Override
    public void handleMidNumSmsRecord(String reqId, MiddleNumberBindLogEntity bindLogEntity, MidNumSmsTranslateDto translateDto) {
        MDC.put(CommonSessionKeys.REQ_ID_KEY, reqId);
        String recordId = MyStringUtils.randomUUID();
        try {

            MidNumSmsDispatcherDto recordDto = midNumSmsMapStructMapper.convertMidNumSmsTranslateToDispatcher(translateDto);

            //bindLog 赋值
            recordDto.setAccountId(bindLogEntity.getBillAccountId());
            recordDto.setMappingId(bindLogEntity.getId());
            recordDto.setUserData(bindLogEntity.getUserData());
            recordDto.setRecorderId(recordId);

            /**
             * 保存短信发送记录
             */
            MiddleNumberSmsCdrEntity cdrEntity = MiddleNumberSmsCdrEntity.builder()
                    .id(recordId)
                    .channelBindId(translateDto.getChannelBindId())
                    .channelRecordId(translateDto.getChannelRecordId())
                    .accountId(bindLogEntity.getAccountId())
                    .mappingId(recordDto.getMappingId())
                    .caller(recordDto.getCaller())
                    .callee(recordDto.getCallee())
                    .telX(recordDto.getTelX())
                    .result(recordDto.getSmsResult())
                    .smsTime(recordDto.getSmsTime())
                    .smsNumber(recordDto.getSmsNumber())
                    .createTime(new Date())
                    .build();


            /**
             * 1.获取短信推送地址
             * 2.将数据推送至abs-support 服务进行话单推送
             */
            List<AccountPushUrlEntity> pushUrlEntities = accountPushUrlService.getByAccountId(bindLogEntity.getAccountId(), AccountPushUrlTypeEnum.X_SMS_CDR_PUSH_URL.getType());

            try {
                if (pushUrlEntities != null && pushUrlEntities.size() > 0) {
                    StringBuilder taskIds = new StringBuilder();
                    pushUrlEntities.stream().forEach(item -> {
                        String cdrPushTaskId = supportService.pushCdrDataToOtherPlatform(ProjectType.MID_NUM_SMS, item.getUrl(), bindLogEntity.getAccountId(), recordDto);
                        taskIds.append(cdrPushTaskId + ";");
                    });
                    cdrEntity.setCdrPushTaskId(taskIds.toString());
                }

            } catch (Exception e) {
                log.error("push sms cdr to other platform error.", e);
            }

            log.info("Save mid num sms record:" + FastJsonUtils.toJSONString(cdrEntity));
            middleNumberSmsCdrService.save(cdrEntity);
        } catch (Exception e) {
            log.error("Insert mid num sms cdr error.", e);
        } finally {
            MDC.remove(CommonSessionKeys.REQ_ID_KEY);
        }

    }


    /**
     * 以60秒为单位,计算分钟数,不足60秒按照1分钟计算
     *
     * @param duration
     * @return
     */
    private long getRateDuration(long duration) {
        if (duration == 0L) {
            return 0L;
        }
        long a = duration / 60;
        long b = duration % 60;
        if (b > 0) {
            a++;
        }
        return a;
    }
}
