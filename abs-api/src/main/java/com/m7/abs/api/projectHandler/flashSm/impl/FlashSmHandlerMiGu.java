package com.m7.abs.api.projectHandler.flashSm.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.m7.abs.api.common.constant.RedisKeys;
import com.m7.abs.api.common.properties.AbsApiProperties;
import com.m7.abs.api.common.utils.RedisUtil;
import com.m7.abs.api.domain.dto.flashSm.FlashSmConfigDto;
import com.m7.abs.api.domain.dto.flashSm.FlashSmDeliveryResultDto;
import com.m7.abs.api.domain.dto.flashSm.FlashSmRecordRespDto;
import com.m7.abs.api.domain.dto.flashSm.FlashSmTranslateDto;
import com.m7.abs.api.domain.vo.flashSm.DeliverContentVO;
import com.m7.abs.api.domain.vo.flashSm.DeliverRequestVO;
import com.m7.abs.api.domain.vo.flashSm.DeliverResponseVO;
import com.m7.abs.api.domain.vo.flashSm.DeliverResultVO;
import com.m7.abs.api.projectHandler.flashSm.FlashSmType;
import com.m7.abs.api.projectHandler.flashSm.IFlashSmHandler;
import com.m7.abs.api.projectHandler.flashSm.model.migu.*;
import com.m7.abs.common.constant.common.FlashSmDeliveryResultEnum;
import com.m7.abs.common.constant.common.FlashSmDeliveryStatusEnum;
import com.m7.abs.common.constant.common.FlashSmTypeConstant;
import com.m7.abs.common.constant.error.code.ErrorCodeConstant;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.*;

/**
 * 运营商：咪咕动漫
 * 对接人：彭科杰
 * 对接时间：2022年05月30日
 */
@Slf4j
@FlashSmType(channel = FlashSmTypeConstant.MGDM)
public class FlashSmHandlerMiGu implements IFlashSmHandler {
    @Autowired
    private RedisUtil redisUtil;
    @Resource
    private AbsApiProperties absApiProperties;

    @Override
    public BaseResponse<DeliverResponseVO> deliver(FlashSmConfigDto configDto, DeliverRequestVO requestVO) {
        try {
            String url = configDto.getBasePath() + "/ecpfep/deliveryServices/delivery";
            List<MiGuDeliverContent> contentList = new ArrayList<>();

            List<DeliverContentVO> deliveryList = requestVO.getDeliveryList();
            //热线集合,收集本次闪信发送涉及的热线号码
            Set<String> hotlines = new HashSet<>();


            if (deliveryList != null) {
                if (deliveryList.size() > 1) {
                    return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.BATCH_PUSH_NOT_SUPPORTED);
                }

                for (DeliverContentVO contentVO : deliveryList) {
                    /**
                     * 呼叫状态事件
                     */
                    MiGuCallEventInfoType eventInfoType = MiGuCallEventInfoType.builder()
                            .callIdentifier(MyStringUtils.randomUUID())
                            .scalling("86" + contentVO.getSender())
                            .called("86" + contentVO.getTarget())
                            .direction("MO")
                            .event("Ringing")
                            .timeStamp(DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_UTC2))
                            .build();

                    /**
                     * 接收对象、模板类型编号和参数
                     */
                    MiGuDeliverContent content = MiGuDeliverContent.builder()
                            .template(configDto.getChannelTemplateId())
                            .target(contentVO.getTarget())
                            .argv(contentVO.getArgs())
                            .callEvent(eventInfoType)
                            .build();

                    contentList.add(content);

                    if (StringUtils.isNotEmpty(contentVO.getSender())) {
                        hotlines.add(contentVO.getSender());
                    }


                }
            }

            if (hotlines.size() > 0) {
                checkHotline(configDto, hotlines);
            }

            MiGuDeliverReq req = MiGuDeliverReq.builder()
                    .msgtype("4")//发送闪信
                    .src("10658086")
                    .biztype("1")
                    .notifyurl(absApiProperties.getApi().getServerUrl() + "/flashSm/record/receive/" + FlashSmTypeConstant.MGDM)//接收咪咕发送回执
                    .content(contentList)
                    .build();
            String resultStr = sendRequest(configDto, url, req, null);
            if (StringUtils.isNotEmpty(resultStr)) {
                MiGuDeliverResp result = JSON.parseObject(resultStr, MiGuDeliverResp.class);
                String taskId = result.getTaskId();

                List<MiGuDeliveryResult> processresult = result.getProcessresult();
                List<DeliverResultVO> deliverResults = new ArrayList<>();

                Map<String, MiGuDeliveryResult> targetLogMap = new HashMap<>();

                if (processresult != null) {
                    processresult.forEach(item -> {
                        String target = item.getTarget();
                        if (StringUtils.isNotEmpty(target)) {
                            targetLogMap.put(target, item);
                        }
                    });
                }

                if (deliveryList != null && deliveryList.size() > 0) {
                    for (int i = 0; i < deliveryList.size(); i++) {
                        DeliverContentVO deliverContentVO = deliveryList.get(i);
                        String target = deliverContentVO.getTarget();
                        if (StringUtils.isNotEmpty(target)) {
                            MiGuDeliveryResult miGuDeliveryResult = targetLogMap.get(target);
                            if (miGuDeliveryResult == null) {
                                if (processresult != null && processresult.size() == 1) {
                                    miGuDeliveryResult = processresult.get(0);
                                }
                            }
                            if (miGuDeliveryResult != null) {
                                FlashSmDeliveryStatusEnum flashSmDeliveryStatusEnum = translateDeliveryStatus(miGuDeliveryResult.getStatus());
                                DeliverResultVO vo = DeliverResultVO.builder()
                                        .sender(deliverContentVO.getSender())
                                        .target(target)
                                        .status(flashSmDeliveryStatusEnum.getValue())
                                        .msg(flashSmDeliveryStatusEnum.getDescription())
                                        .build();
                                deliverResults.add(vo);
                            }
                        }
                    }
                }

                DeliverResponseVO responseVO = DeliverResponseVO.builder()
                        .taskId(taskId)
                        .deliverResult(deliverResults)
                        .build();

                if (StringUtils.isNotEmpty(taskId)) {
                    return BaseResponse.success(responseVO);
                } else {
                    return BaseResponse.fail(responseVO);
                }
            }
            return BaseResponse.fail("[" + FlashSmTypeConstant.MGDM + "] deliver fail");
        } catch (Exception e) {
            log.error("[" + FlashSmTypeConstant.MGDM + "] deliver Error", e);
            return BaseResponse.error("[" + FlashSmTypeConstant.MGDM + "] deliver error");
        }
    }

    @Override
    public BaseResponse<FlashSmRecordRespDto> translateRecord(Map<String, Object> requestVO) {
        if (requestVO != null) {
            String reqJsonStr = FastJsonUtils.toJSONString(requestVO);
            MiGuReqBody<MiGuDeliveryResp> recordReq = FastJsonUtils.toBean(reqJsonStr, new TypeReference<MiGuReqBody<MiGuDeliveryResp>>() {
            });
            if (recordReq != null) {
                MiGuDeliveryResp body = recordReq.getBody();
                if (body != null) {
                    List<FlashSmDeliveryResultDto> deliveryResultDto = new ArrayList<>();
                    List<MiGuDeliveryResultResp> deliveryresult = body.getDeliveryresult();
                    if (deliveryresult != null) {
                        for (MiGuDeliveryResultResp resultResp : deliveryresult) {
                            FlashSmDeliveryResultDto resultDto = FlashSmDeliveryResultDto.builder()
                                    .target(resultResp.getTarget())
                                    .status(translateStatus(resultResp.getStatus()).getValue())
                                    .msg(resultResp.getMsg())
                                    .build();

                            deliveryResultDto.add(resultDto);
                        }
                    }
                    FlashSmTranslateDto translateDto = FlashSmTranslateDto.builder()
                            .taskId(body.getTaskid())
                            .deliveryResult(deliveryResultDto)
                            .build();

                    Map<String, Object> respMap = new HashMap<>();
                    respMap.put("status", "0");
                    respMap.put("msg", "Successfully received");

                    FlashSmRecordRespDto respDto = FlashSmRecordRespDto.builder()
                            .translateDto(translateDto)
                            .respData(respMap)
                            .build();
                    return BaseResponse.success(respDto);
                }

            }
        }

        return BaseResponse.fail("translate record fail.");
    }


    private FlashSmDeliveryStatusEnum translateDeliveryStatus(String status) {
        if (StringUtils.isNotEmpty(status)) {
            switch (status) {
                case "4":
                    return FlashSmDeliveryStatusEnum.DELIVERY_SUCCEED;
                case "5":
                    return FlashSmDeliveryStatusEnum.DELIVERY_FAILED;
                case "126":
                    return FlashSmDeliveryStatusEnum.DELIVERY_INTERVAL_LIMIT;
                case "127":
                    return FlashSmDeliveryStatusEnum.DELIVERY_DAY_LIMIT;
                case "114":
                    return FlashSmDeliveryStatusEnum.INVALID_TARGET_NUMBER;
                case "115":
                    return FlashSmDeliveryStatusEnum.TARGET_IN_BLACKLIST;
                case "120":
                    return FlashSmDeliveryStatusEnum.TARGET_SEGMENT_IS_RESTRICTED;
                case "121":
                    return FlashSmDeliveryStatusEnum.BEYOND_FLUID_CONTROL;
                case "122":
                    return FlashSmDeliveryStatusEnum.DELIVERY_TYPE_NOT_MATCHING_TEMPLATE;
                case "201":
                    return FlashSmDeliveryStatusEnum.REACH_MAXIMUM_DELIVERY_LIMIT;
                case "202":
                    return FlashSmDeliveryStatusEnum.ILLEGAL_WORD_FOUND;
                case "204":
                    return FlashSmDeliveryStatusEnum.API_RESOURCE_UNAVAILABLE;
                case "206":
                    return FlashSmDeliveryStatusEnum.LENGTH_OF_THE_MESSAGE_CONTENT_EXCEEDS_LIMIT;
                case "604":
                    return FlashSmDeliveryStatusEnum.NOT_SUPPORTED_BY_THE_OPERATOR;
                case "605":
                    return FlashSmDeliveryStatusEnum.SYSTEM_BUSY;
                default:
                    return FlashSmDeliveryStatusEnum.DELIVERY_FAILED;
            }
        }
        return FlashSmDeliveryStatusEnum.DELIVERY_FAILED;
    }

    private FlashSmDeliveryResultEnum translateStatus(String status) {
        if (StringUtils.isNotEmpty(status)) {
            switch (status) {
                case "1":
                    return FlashSmDeliveryResultEnum.DELIVERY_SUCCEED;
                case "2":
                case "110":
                    return FlashSmDeliveryResultEnum.DELIVERY_FAILED;
                case "212":
                    return FlashSmDeliveryResultEnum.REPORT_TIMEOUT;
                default:
                    return FlashSmDeliveryResultEnum.UNKNOWN_ERROR;
            }
        }
        return FlashSmDeliveryResultEnum.SERVICE_ERROR;
    }

    /**
     * 检查主叫号码是否需要添加热线
     *
     * @param configDto
     * @param hotlines
     * @return
     */
    private BaseResponse checkHotline(FlashSmConfigDto configDto, Set<String> hotlines) {
        List<String> hotlineList = new ArrayList<>(hotlines);
        List<String> needRegisterHotlines = new ArrayList<>();
        List<String> needSyncHotlines = new ArrayList<>();

        String hotlineKey = RedisKeys.REDIS_FLASH_SMS_MIGU_HOTLINE + "_" + configDto.getAccessKey();
        String hotlineSyncKey = RedisKeys.REDIS_FLASH_SMS_MIGU_HOTLINE_SYNC + "_" + configDto.getAccessKey() + "_" + configDto.getChannelTemplateId();

        for (int i = 0; i < hotlineList.size(); i++) {
            String hotline = hotlineList.get(i);
            boolean isRegister = redisUtil.sHasKey(hotlineKey, hotline);

            if (!isRegister) {
                /**
                 * 未注册热线号码,添加号码至注册队列
                 */
                needRegisterHotlines.add(hotline);
            }

            boolean isSync = redisUtil.sHasKey(hotlineSyncKey, hotline);
            if (!isSync) {
                /**
                 * 未同步热线号码与模板,添加号码至注册队列
                 */
                needSyncHotlines.add(hotline);
            }
        }

        if (needRegisterHotlines.size() > 0) {
            BaseResponse baseResponse = hotlineManager(configDto, needRegisterHotlines);
            if (baseResponse.getCode().equals(ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode())) {
                redisUtil.sSet(hotlineKey, needRegisterHotlines.toArray());
            }
        }

        if (needSyncHotlines.size() > 0) {
            BaseResponse baseResponse = syncTemplateAndHotline(configDto, needSyncHotlines);
            if (baseResponse.getCode().equals(ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode())) {
                redisUtil.sSet(hotlineSyncKey, needSyncHotlines.toArray());
            }
        }
        return BaseResponse.success();
    }

    /**
     * 新增热线
     * 热线在咪咕闪信接口中代表主叫的意思,在发送闪信之前,需要在咪咕系统中报备热线(主叫)
     *
     * @param configDto
     * @param hotLines
     * @return
     */
    private BaseResponse hotlineManager(FlashSmConfigDto configDto, List<String> hotLines) {
        try {
            String url = configDto.getBasePath() + "/ecpfep/hotlineServices/hotlinemngr";

            MiGuHotlineManagerReq req = MiGuHotlineManagerReq.builder()
                    .actiontype("0")
                    .hotlines(hotLines)
                    .build();
            String resultStr = sendRequest(configDto, url, req, null);
            if (StringUtils.isNotEmpty(resultStr)) {
                MiGuHotlineResp result = JSON.parseObject(resultStr, MiGuHotlineResp.class);
                String resultCode = result.getResultCode();
                /**
                 * 0:表示成功
                 * 100099:表示关系已经存在
                 */
                if (StringUtils.isNotEmpty(resultCode) && ("0".equals(resultCode) || "100099".equals(resultCode))) {
                    return BaseResponse.success();
                } else {
                    return BaseResponse.fail("");
                }
            }
            return BaseResponse.fail("[" + FlashSmTypeConstant.MGDM + "] deliver fail");
        } catch (Exception e) {
            log.error("[" + FlashSmTypeConstant.MGDM + "] deliver Error", e);
            return BaseResponse.error("[" + FlashSmTypeConstant.MGDM + "] deliver error");
        }
    }


    /**
     * 同步模板与热线的关系
     * 模板和热线之间需要进行同步才能发送闪信
     *
     * @param configDto
     * @param hotLines
     * @return
     */
    private BaseResponse syncTemplateAndHotline(FlashSmConfigDto configDto, List<String> hotLines) {
        try {
            String url = configDto.getBasePath() + "/ecpfep/hotlineServices/synctemphotline";

            MiGuSyncHotlineManagerReq req = MiGuSyncHotlineManagerReq.builder()
                    .actiontype("0")
                    .templateid(configDto.getChannelTemplateId())
                    .hotlines(hotLines)
                    .build();
            String resultStr = sendRequest(configDto, url, req, null);
            if (StringUtils.isNotEmpty(resultStr)) {
                MiGuHotlineResp result = JSON.parseObject(resultStr, MiGuHotlineResp.class);
                String resultCode = result.getResultCode();
                /**
                 * 0:表示成功
                 * 100099:表示关系已经存在
                 */
                if (StringUtils.isNotEmpty(resultCode) && "0".equals(resultCode) || "100099".equals(resultCode)) {
                    return BaseResponse.success();
                } else {
                    return BaseResponse.fail("");
                }
            }
            return BaseResponse.fail("[" + FlashSmTypeConstant.MGDM + "] deliver fail");
        } catch (Exception e) {
            log.error("[" + FlashSmTypeConstant.MGDM + "] deliver Error", e);
            return BaseResponse.error("[" + FlashSmTypeConstant.MGDM + "] deliver error");
        }
    }


    /**
     * 发送Http请求
     *
     * @param configDto
     * @param url
     * @param data
     * @param headers
     * @return
     */
    private String sendRequest(FlashSmConfigDto configDto, String url, Object data, Map<String, String> headers) {
        if (headers == null) {
            headers = new HashMap<>();
        }

        String platformId = configDto.getAccessKey();
        String timestamp = String.valueOf(new Date().getTime() / 1000);
        String signature = MD5Util.md5Encrypt32Lower(timestamp + configDto.getSecretKey());

        headers.put("platformid", platformId);
        headers.put("timestamp", timestamp);
        headers.put("sign", signature.substring(16));

        MiGuReqBody reqBody = MiGuReqBody.builder()
                .header(headers)
                .body(data)
                .build();
        JSONObject jsonObject = FastJsonUtils.parseObject(FastJsonUtils.toJSONNoFeatures(reqBody));

        headers.put(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8");
        String resultString = HttpUtil.doPost(url, jsonObject.toString(), headers, true);
        return resultString;
    }
}
