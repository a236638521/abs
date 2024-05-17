package com.m7.abs.admin.controller;

import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.service.IXNumGroupService;
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
 * @since 2022-08-02
 */
@RestController
@RequestMapping("/x_num_group")
public class XNumGroupController {

    @Autowired
    private IXNumGroupService xNumGroupService;

    @WebAspect(injectReqId = true, logDesc = "sys->x_num_group->findPage")
    @PreAuthorize("hasAuthority('sys:x_num_group:view')")
    @PostMapping(value = "/findPage")
    public BaseResponse findPage(@RequestBody PageBean page) {
        return BaseResponse.success(xNumGroupService.page(page, page.getQueryWrapper()));
    }
}
