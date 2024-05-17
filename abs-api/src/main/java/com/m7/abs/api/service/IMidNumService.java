package com.m7.abs.api.service;

import com.m7.abs.api.domain.vo.midNum.*;
import com.m7.abs.common.domain.base.BaseResponse;

import java.util.Map;

/**
 * @author hulin
 */
public interface IMidNumService {

    BaseResponse axbBind(BindAXBRequestVO requestVO);

    BaseResponse axBind(BindAXRequestVO requestVO);

    BaseResponse unBindAXB(UnBindRequestVO requestVO);

    BaseResponse axUnBind(UnBindRequestVO requestVO);

    BaseResponse axDelay(DelayAxRequestVO requestVO);

    BaseResponse axbDelay(DelayAXBRequestVO requestVO);

    Object record(String channelCode, Map<String, Object> requestVO);

    Object recordUrl(String channelCode, Map<String, Object> requestVO);

    Object getRingTime(String channelCode, Map<String, Object> requestVO);
}
