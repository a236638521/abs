package com.m7.abs.admin.feignClient.export;

import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.vo.reportexport.MidNumCdrExportRequestVO;
import com.m7.abs.common.domain.vo.reportexport.MidNumCdrExportResponseVO;
import com.m7.abs.common.domain.vo.support.PushDataVO;
import com.m7.abs.common.utils.FastJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class ExportClientFallback implements ExportClient {

    @Override
    public BaseResponse<MidNumCdrExportResponseVO> exportExcel(MidNumCdrExportRequestVO request) {
        log.warn("export excel fail.do fallback,RequestData:\n" + FastJsonUtils.toJSONString(request));
        return BaseResponse.fail("service is unavailable");
    }

}
