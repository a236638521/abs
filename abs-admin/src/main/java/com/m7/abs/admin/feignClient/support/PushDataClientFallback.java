package com.m7.abs.admin.feignClient.support;

import com.m7.abs.common.domain.base.BaseRequest;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.bo.support.PushDataBO;
import com.m7.abs.common.domain.bo.support.RetryPushDataBO;
import com.m7.abs.common.domain.vo.support.PushDataVO;
import com.m7.abs.common.domain.vo.support.RetryPushDataVO;
import com.m7.abs.common.utils.FastJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
class PushDataClientFallback implements PushDataClient {

    @Override
    public BaseResponse<PushDataVO> dataPush(BaseRequest<PushDataBO> request) {
        log.warn("Push data fail.do fallback,RequestData:\n" + FastJsonUtils.toJSONString(request));
        return null;
    }

    @Override
    public BaseResponse<RetryPushDataVO> retryPush(BaseRequest<List<RetryPushDataBO>> request) {
        log.warn("Retry Push data fail.do fallback,RequestData:\n" + FastJsonUtils.toJSONString(request));
        return null;
    }
}
