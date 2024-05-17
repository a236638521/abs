package com.m7.abs.admin.feignClient.export;


import com.m7.abs.common.constant.requestPath.SupportRequestPath;
import com.m7.abs.common.domain.base.BaseRequest;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.bo.support.PushDataBO;
import com.m7.abs.common.domain.bo.support.RetryPushDataBO;
import com.m7.abs.common.domain.vo.reportexport.MidNumCdrExportRequestVO;
import com.m7.abs.common.domain.vo.reportexport.MidNumCdrExportResponseVO;
import com.m7.abs.common.domain.vo.support.PushDataVO;
import com.m7.abs.common.domain.vo.support.RetryPushDataVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


/**
 * @author Kejie Peng
 */
@Primary
@FeignClient(value = "${abs.report-export.server-url}", contextId = "ExportClient", fallback = ExportClientFallback.class)
public interface ExportClient {

    /**
     * 导出Excel
     * @param request
     * @return
     */
    @PostMapping(value = "/midNum/export/excel", consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResponse<MidNumCdrExportResponseVO> exportExcel(@RequestBody MidNumCdrExportRequestVO request);

}
