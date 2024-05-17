package com.m7.abs.admin.feignClient.support;


import com.m7.abs.common.constant.requestPath.SupportRequestPath;
import com.m7.abs.common.domain.base.BaseRequest;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.bo.support.PushDataBO;
import com.m7.abs.common.domain.bo.support.RetryPushDataBO;
import com.m7.abs.common.domain.vo.support.PushDataVO;
import com.m7.abs.common.domain.vo.support.RetryPushDataVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@Primary
@FeignClient(value = "${abs.support.server-url}", contextId = "PushDataClient", fallback = PushDataClientFallback.class)
public interface PushDataClient {

    @PostMapping(value = SupportRequestPath.PUSH_DATA, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResponse<PushDataVO> dataPush(@RequestBody BaseRequest<PushDataBO> request);

    @PostMapping(value = SupportRequestPath.PUSH_DATA_RETRY, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResponse<RetryPushDataVO> retryPush(@Validated @RequestBody BaseRequest<List<RetryPushDataBO>> request);
}
