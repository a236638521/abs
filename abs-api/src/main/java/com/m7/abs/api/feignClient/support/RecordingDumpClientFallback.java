package com.m7.abs.api.feignClient.support;

import com.m7.abs.api.domain.bo.RetrySaveToOssBO;
import com.m7.abs.api.domain.bo.SaveToOssBO;
import com.m7.abs.api.domain.vo.midNum.SaveToOssVO;
import com.m7.abs.common.domain.base.BaseRequest;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.utils.FastJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RecordingDumpClientFallback implements RecordingDumpClient {

    @Override
    public BaseResponse<SaveToOssVO> saveToOss(BaseRequest<SaveToOssBO> request) {
        log.warn("Save to oss fail.do fallback,RequestData:\n" + FastJsonUtils.toJSONString(request));
        return null;
    }

    @Override
    public BaseResponse<SaveToOssVO> saveToOssRetry(BaseRequest<List<RetrySaveToOssBO>> request) {
        log.warn("retry to oss fail.do fallback,RequestData:\n" + FastJsonUtils.toJSONString(request));
        return null;
    }
}
