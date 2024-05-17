package com.m7.abs.api.service;

import com.m7.abs.api.domain.bo.SaveToOssReqBO;
import com.m7.abs.api.domain.vo.midNum.PushRecordRetryRequestVO;
import com.m7.abs.api.domain.vo.midNum.SaveToOssVO;
import com.m7.abs.common.domain.base.BaseResponse;

public interface ISupportService {
    /**
     * 录音转存
     * @param projectCode
     * @param recordDto
     * @return
     */
    SaveToOssVO pushRecordToOssMidNumRecordDto(String projectCode, SaveToOssReqBO recordDto);

    /**
     * 消息推送
     * @param projectCode
     * @param url
     * @param accountId
     * @param recordDto
     * @return
     */
    String pushCdrDataToOtherPlatform(String projectCode, String url, String accountId, Object recordDto);

    /**
     * 重推录音文件至文件存储服务
     * @param requestVO
     * @return
     */
    BaseResponse pushRecordRetry(PushRecordRetryRequestVO requestVO);
}
