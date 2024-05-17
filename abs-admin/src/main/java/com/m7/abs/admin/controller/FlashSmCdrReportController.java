package com.m7.abs.admin.controller;

import com.m7.abs.admin.domain.vo.report.ReportSearchVO;
import com.m7.abs.admin.service.IFlashSmCdrReportService;
import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.domain.base.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * API Server
 * </p>
 *
 * @author Kejie Peng
 * @since 2023-04-10
 */
@RestController
@RequestMapping("/flash_sm_cdr_report")
public class FlashSmCdrReportController {

    @Autowired
    private IFlashSmCdrReportService flashSmCdrReportService;

    @WebAspect(injectReqId = true, logDesc = "sys->flash_sm_cdr_report->search")
    @PreAuthorize("hasAuthority('sys:flash_sm_cdr_report:view')")
    @PostMapping(value = "/search")
    public BaseResponse findPage(@RequestBody ReportSearchVO searchVO) {
        return BaseResponse.success(flashSmCdrReportService.search(searchVO));
    }
}
