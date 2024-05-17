package com.m7.abs.api.feignClient.support;

import com.m7.abs.api.domain.bo.PushDataBO;
import com.m7.abs.api.domain.vo.midNum.PushDataVO;
import com.m7.abs.common.constant.requestPath.SupportRequestPath;
import com.m7.abs.common.domain.base.BaseRequest;
import com.m7.abs.common.domain.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Primary
@FeignClient(value = "${abs.support.server-url}", contextId = "PushDataClient", fallback = PushDataClientFallback.class)
public interface PushDataClient {

    @PostMapping(value = SupportRequestPath.PUSH_DATA, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResponse<PushDataVO> dataPush(@RequestBody BaseRequest<PushDataBO> request);

}
