package com.m7.abs.admin.controller;

import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.service.IMiddleNumberCdrService;
import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.domain.base.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * API Server
 * </p>
 *
 * @author Kejie Peng
 * @since 2022-03-16
 */
@RestController
@RequestMapping("/middle_number_cdr")
public class MiddleNumberCdrController {
    @Autowired
    private IMiddleNumberCdrService middleNumberCdrService;


    @WebAspect(injectReqId = true, logDesc = "sys->middle_number_cdr->findPage", logDetail = false)
    @PreAuthorize("hasAuthority('sys:middle_number_cdr:view')")
    @PostMapping(value = "/findPage")
    public BaseResponse findPage(@RequestBody PageBean page) {
        return BaseResponse.success(middleNumberCdrService.findMidNumByPage(page));
    }

    @WebAspect(injectReqId = true, logDesc = "sys->middle_number_cdr->export_excel")
    @PreAuthorize("hasAuthority('sys:middle_number_cdr:export_excel')")
    @PostMapping(value = "/export_excel")
    public BaseResponse exportExcel(@RequestBody PageBean page, HttpServletResponse response) {
        middleNumberCdrService.exportExcel(page, response);
        return BaseResponse.success();
    }


}
