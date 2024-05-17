package com.m7.abs.api.service;

import com.m7.abs.api.domain.vo.flashSm.DeliverRequestVO;
import com.m7.abs.common.domain.base.BaseResponse;

import java.util.Map;

public interface IFlashSmService {
    /**
     * 发送闪信
     * @param requestVO
     * @return
     */
    BaseResponse deliver(DeliverRequestVO requestVO);

    /**
     * 接收闪信话单推送
     * @param channelCode
     * @param requestVO
     * @return
     */
    Object receiveRecord(String channelCode, Map<String, Object> requestVO);
}
