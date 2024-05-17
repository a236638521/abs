package com.m7.abs.api.feignClient.support;

import com.m7.abs.api.domain.bo.PushDataBO;
import com.m7.abs.api.domain.vo.midNum.PushDataVO;
import com.m7.abs.common.domain.base.BaseRequest;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.utils.FastJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class PushDataClientFallback implements PushDataClient {

    @Override
    public BaseResponse<PushDataVO> dataPush(BaseRequest<PushDataBO> request) {
        log.warn("Push data fail.do fallback,RequestData:\n" + FastJsonUtils.toJSONString(request));
        return null;
    }
}
