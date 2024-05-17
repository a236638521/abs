package com.m7.abs.admin.controller;

import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.service.IFlashSmDeliveryLogService;
import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.domain.base.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * API Server
 * </p>
 *
 * @author Kejie Peng
 * @since 2022-06-06
 */
@RestController
@RequestMapping("/flash_sm_delivery_log")
public class FlashSmDeliveryLogController {

    @Autowired
    private IFlashSmDeliveryLogService flashSmDeliveryLogService;


    @WebAspect(injectReqId = true, logDesc = "sys->flash_sm_delivery_log->delete")
    @PreAuthorize("hasAuthority('sys:flash_sm_delivery_log:delete')")
    @PostMapping(value = "/delete")
    public BaseResponse delete(@RequestBody List<String> records) {
        return BaseResponse.success(flashSmDeliveryLogService.removeByIds(records));
    }

    @WebAspect(injectReqId = true, logDesc = "sys->flash_sm_delivery_log->findPage")
    @PreAuthorize("hasAuthority('sys:flash_sm_delivery_log:view')")
    @PostMapping(value = "/findPage")
    public BaseResponse findPage(@RequestBody PageBean page) {
        return BaseResponse.success(flashSmDeliveryLogService.page(page, page.getQueryWrapper()));
    }
}
