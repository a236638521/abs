package com.m7.abs.admin.controller;

import com.m7.abs.admin.domain.vo.report.ReportSearchVO;
import com.m7.abs.admin.service.IMiddleNumberCdrReportService;
import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.domain.base.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * API Server
 * </p>
 *
 * @author Kejie Peng
 * @since 2023-04-03
 */
@RestController
@RequestMapping("/middle_number_cdr_report")
public class MiddleNumberCdrReportController {

    @Autowired
    private IMiddleNumberCdrReportService middleNumberCdrReportService;

    @WebAspect(injectReqId = true, logDesc = "sys->middle_number_cdr_report->search")
    @PreAuthorize("hasAuthority('sys:middle_number_cdr_report:view')")
    @PostMapping(value = "/search")
    public BaseResponse search(@RequestBody @Valid ReportSearchVO searchVO) {
        return BaseResponse.success(middleNumberCdrReportService.search(searchVO));
    }
}
