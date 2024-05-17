package com.m7.abs.admin.feignClient.support;


import com.m7.abs.common.constant.requestPath.SupportRequestPath;
import com.m7.abs.common.domain.base.BaseRequest;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.bo.support.RecordingDumpBO;
import com.m7.abs.common.domain.bo.support.RetrySaveToOssBO;
import com.m7.abs.common.domain.vo.support.RecordingDumpVO;
import com.m7.abs.common.domain.vo.support.RetrySaveToOssVO;
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
    BaseResponse<RecordingDumpVO> saveToOss(@RequestBody BaseRequest<RecordingDumpBO> request);


    @PostMapping(value = SupportRequestPath.RECORDING_DUMP_RETRY, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResponse<RetrySaveToOssVO> saveToOssRetry(@RequestBody BaseRequest<List<RetrySaveToOssBO>> request);
}
