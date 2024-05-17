package com.m7.abs.api.service.impl;

import com.m7.abs.api.domain.bo.PushDataBO;
import com.m7.abs.api.domain.bo.RetrySaveToOssBO;
import com.m7.abs.api.domain.bo.SaveToOssBO;
import com.m7.abs.api.domain.bo.SaveToOssReqBO;
import com.m7.abs.api.domain.vo.midNum.*;
import com.m7.abs.api.feignClient.support.PushDataClient;
import com.m7.abs.api.feignClient.support.RecordingDumpClient;
import com.m7.abs.api.service.IMiddleNumberCdrService;
import com.m7.abs.api.service.ISupportService;
import com.m7.abs.common.constant.common.MidNumOSSRetryStatusEnum;
import com.m7.abs.common.constant.error.code.ErrorCodeConstant;
import com.m7.abs.common.constant.keys.CommonSessionKeys;
import com.m7.abs.common.domain.base.BaseRequest;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.MiddleNumberCdrEntity;
import com.m7.abs.common.utils.DateUtil;
import com.m7.abs.common.utils.FastJsonUtils;
import com.m7.abs.common.utils.MyStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SupportServiceImpl implements ISupportService {
    @Autowired
    private PushDataClient pushDataClient;
    @Autowired
    private RecordingDumpClient recordingDumpClient;
    @Autowired
    private IMiddleNumberCdrService middleNumberCdrService;

    /**
     * 录音文件转存至OSS服务
     *
     * @param recordDto
     */
    @Override
    public SaveToOssVO pushRecordToOssMidNumRecordDto(String projectCode, SaveToOssReqBO recordDto) {
        String recordFileUrl = recordDto.getRecordFileUrl();
        String accountId = recordDto.getAccountId();

        BaseRequest<SaveToOssBO> baseRequest = new BaseRequest<>();

        Date callStartDate = recordDto.getBeginTime();
        String currentDate = DateUtil.parseDateToStr(callStartDate, "yyyyMMdd-HHmmss");
        String fileSuffix = MyStringUtils.parseSuffix(recordFileUrl);

        //默认文件后缀为wav
        if (StringUtils.isEmpty(fileSuffix)) {
            fileSuffix = "wav";
        }

        String fileName = currentDate + "_" + accountId + "_" + callStartDate.getTime() + "_" + recordDto.getRecorderId() + "." + fileSuffix;

        SaveToOssBO saveToOssBO = SaveToOssBO.builder()
                .accountId(accountId)
                .storageIds(recordDto.getStorageIds())
                .fileUrl(recordFileUrl)
                .projectCode(projectCode)
                .fileName(fileName)
                .fileTime(callStartDate.getTime())
                .build();

        baseRequest.setRequestId(MDC.get(CommonSessionKeys.REQ_ID_KEY));
        baseRequest.setParam(saveToOssBO);

        log.info("Support oss request params:" + FastJsonUtils.toJSONString(baseRequest));
        BaseResponse<SaveToOssVO> baseResponse = recordingDumpClient.saveToOss(baseRequest);
        log.info("Support Response:" + FastJsonUtils.toJSONString(baseResponse));

        if (baseResponse != null && baseResponse.isSuccess()) {
            SaveToOssVO saveToOssVO = baseResponse.getData();
            return saveToOssVO;
        }

        return null;
    }

    /**
     * 推送数据给第三方
     *
     * @param url
     * @param recordDto
     * @return
     */
    @Override
    public String pushCdrDataToOtherPlatform(String projectCode, String url, String accountId, Object recordDto) {
        String taskId = null;

        BaseRequest<PushDataBO> baseRequest = new BaseRequest<>();

        PushDataBO saveToOssBO = PushDataBO.builder()
                .accountId(accountId)
                .projectCode(projectCode)
                .url(url)
                .data(FastJsonUtils.toJSONString(recordDto))
                .method("POST")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .build();
        baseRequest.setRequestId(MDC.get(CommonSessionKeys.REQ_ID_KEY));
        baseRequest.setParam(saveToOssBO);

        log.info("push cdr data to other platform:" + FastJsonUtils.toJSONString(baseRequest));
        BaseResponse<PushDataVO> baseResponse = pushDataClient.dataPush(baseRequest);
        log.info("Support Response:" + FastJsonUtils.toJSONString(baseResponse));
        if (baseResponse != null && baseResponse.isSuccess()) {
            PushDataVO pushDataVO = baseResponse.getData();
            taskId = pushDataVO.getTaskId();
        }
        return taskId;
    }

    @Override
    public BaseResponse<PushRecordRetryRespVO> pushRecordRetry(PushRecordRetryRequestVO requestVO) {
        List<String> recorderIdsRq = requestVO.getRecorderIds();
        /**
         * 去重
         */
        List<String> recorderIds = recorderIdsRq.stream().distinct().collect(Collectors.toList());
        List<PushRecordRetryInfoVO> successResult = new ArrayList();
        List<PushRecordRetryInfoVO> failResult = new ArrayList();
        /**
         * 校验recorders
         */
        List<MiddleNumberCdrEntity> middleNumberCdrEntities = middleNumberCdrService.listByIds(recorderIds);
        if (middleNumberCdrEntities == null || middleNumberCdrEntities.size() == 0) {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.NO_RECORD_FOUND);
        }

        Map<String, List<Map<String, Object>>> stringListMap = checkPushRecordCdrList(middleNumberCdrEntities);
        List<Map<String, Object>> success = stringListMap.get("success");
        List<Map<String, Object>> fail = stringListMap.get("fail");

        List<RetrySaveToOssBO> retryList = new ArrayList<>();

        if (success != null) {
            success.stream().forEach(needPushCdrMap -> {
                MiddleNumberCdrEntity entity = (MiddleNumberCdrEntity) needPushCdrMap.get("entity");
                String id = entity.getId();

                RetrySaveToOssBO retrySaveToOssBO = RetrySaveToOssBO.builder()
                        .taskId(entity.getOssTaskId())
                        .retryInterval(60l)
                        .build();
                retryList.add(retrySaveToOssBO);

                PushRecordRetryInfoVO infoVO = PushRecordRetryInfoVO.builder()
                        .recorderId(entity.getId())
                        .code(MidNumOSSRetryStatusEnum.RETRY_SUCCESS.getValue())
                        .msg(MidNumOSSRetryStatusEnum.RETRY_SUCCESS.getDescription())
                        .build();

                successResult.add(infoVO);
                /**
                 * 移除已加入队列的数据
                 */
                recorderIds.remove(id);
            });
        }

        if (retryList != null && retryList.size() > 0) {
            BaseRequest<List<RetrySaveToOssBO>> baseRequest = new BaseRequest<>();
            baseRequest.setRequestId(MDC.get(CommonSessionKeys.REQ_ID_KEY));
            baseRequest.setParam(retryList);
            log.info("Retry to save oss data:" + FastJsonUtils.toJSONString(baseRequest));
            BaseResponse<SaveToOssVO> baseResponse = recordingDumpClient.saveToOssRetry(baseRequest);
            log.info("Support Response:" + FastJsonUtils.toJSONString(baseResponse));
        }


        fail.stream().forEach(map -> {
            MiddleNumberCdrEntity entity = (MiddleNumberCdrEntity) map.get("entity");
            String id = entity.getId();
            PushRecordRetryInfoVO infoVO = PushRecordRetryInfoVO.builder()
                    .recorderId(id)
                    .code((String) map.get("code"))
                    .msg((String) map.get("reason"))
                    .build();
            failResult.add(infoVO);
            /**
             * 移除已判定为错误的数据
             */
            recorderIds.remove(id);
        });

        recorderIds.stream().forEach(item -> {

            PushRecordRetryInfoVO infoVO = PushRecordRetryInfoVO.builder()
                    .recorderId(item)
                    .code(MidNumOSSRetryStatusEnum.NO_RECORD.getValue())
                    .msg(MidNumOSSRetryStatusEnum.NO_RECORD.getDescription())
                    .build();

            failResult.add(infoVO);
        });

        PushRecordRetryRespVO respVO = PushRecordRetryRespVO.builder()
                .success(successResult)
                .fail(failResult)
                .build();


        return BaseResponse.success(respVO);
    }

    private Map<String, List<Map<String, Object>>> checkPushRecordCdrList(List<MiddleNumberCdrEntity> cdrEntities) {
        List<Map<String, Object>> success = new ArrayList();
        List<Map<String, Object>> fail = new ArrayList();
        cdrEntities.stream().forEach(entity -> {
            Map<String, Object> result = new HashMap<>();
            result.put("entity", entity);
            String ossTaskId = entity.getOssTaskId();
            if (StringUtils.isNotEmpty(ossTaskId)) {
                Date createTime = entity.getCreateTime();
                long[] distanceTime = DateUtil.getDistanceTime(createTime, new Date());
                long distanceDay = distanceTime[0];
                long distanceMin = distanceTime[2];
                if (distanceDay <= 7) {
                    if (distanceDay < 1 && distanceMin <= 5) {
                        result.put("reason", MidNumOSSRetryStatusEnum.NOT_SUPPORT.getDescription());
                        result.put("code", MidNumOSSRetryStatusEnum.NOT_SUPPORT.getValue());
                        fail.add(result);
                        return;
                    }
                    success.add(result);
                } else {
                    result.put("reason", MidNumOSSRetryStatusEnum.EXPIRED.getDescription());
                    result.put("code", MidNumOSSRetryStatusEnum.EXPIRED.getValue());
                    fail.add(result);
                }
                return;
            } else {
                result.put("reason", MidNumOSSRetryStatusEnum.NO_OSS_RECORD.getDescription());
                result.put("code", MidNumOSSRetryStatusEnum.NO_OSS_RECORD.getValue());
                fail.add(result);
                return;
            }

        });

        Map<String, List<Map<String, Object>>> resultMap = new HashMap<>();
        resultMap.put("success", success);
        resultMap.put("fail", fail);
        return resultMap;
    }
}
