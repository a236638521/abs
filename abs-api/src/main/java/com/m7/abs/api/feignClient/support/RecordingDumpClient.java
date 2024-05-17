package com.m7.abs.api.feignClient.support;

import com.m7.abs.api.domain.bo.RetrySaveToOssBO;
import com.m7.abs.api.domain.bo.SaveToOssBO;
import com.m7.abs.api.domain.vo.midNum.SaveToOssVO;
import com.m7.abs.common.constant.requestPath.SupportRequestPath;
import com.m7.abs.common.domain.base.BaseRequest;
import com.m7.abs.common.domain.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Primary
@FeignClient(value = "${abs.support.server-url}", contextId = "RecordingDumpClient", fallback = RecordingDumpClientFallback.class)
public interface RecordingDumpClient {

    @PostMapping(value = SupportRequestPath.RECORDING_DUMP, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResponse<SaveToOssVO> saveToOss(@RequestBody BaseRequest<SaveToOssBO> request);


    @PostMapping(value = SupportRequestPath.RECORDING_DUMP_RETRY, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResponse<SaveToOssVO> saveToOssRetry(@RequestBody BaseRequest<List<RetrySaveToOssBO>> request);
}
