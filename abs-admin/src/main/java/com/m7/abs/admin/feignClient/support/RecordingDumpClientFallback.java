package com.m7.abs.admin.feignClient.support;

import com.m7.abs.common.domain.base.BaseRequest;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.bo.support.RecordingDumpBO;
import com.m7.abs.common.domain.bo.support.RetrySaveToOssBO;
import com.m7.abs.common.domain.vo.support.RecordingDumpVO;
import com.m7.abs.common.domain.vo.support.RetrySaveToOssVO;
import com.m7.abs.common.utils.FastJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RecordingDumpClientFallback implements RecordingDumpClient {

    @Override
    public BaseResponse<RecordingDumpVO> saveToOss(BaseRequest<RecordingDumpBO> request) {
        log.warn("Save to oss fail.do fallback,RequestData:\n" + FastJsonUtils.toJSONString(request));
        return null;
    }

    @Override
    public BaseResponse<RetrySaveToOssVO> saveToOssRetry(BaseRequest<List<RetrySaveToOssBO>> request) {
        log.warn("retry to oss fail.do fallback,RequestData:\n" + FastJsonUtils.toJSONString(request));
        return null;
    }
}
