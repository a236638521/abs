package com.m7.abs.admin.controller;

import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.service.IWhiteIpService;
import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.WhiteIpEntity;
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
 * @since 2022-03-01
 */
@RestController
@RequestMapping("/white_ip")
public class WhiteIpController {

    @Autowired
    private IWhiteIpService whiteIpService;

    @WebAspect(injectReqId = true, logDesc = "sys->white_ip->saveOrUpdate")
    @PreAuthorize("hasAuthority('sys:white_ip:add') AND hasAuthority('sys:white_ip:edit')")
    @PostMapping(value = "/save")
    public BaseResponse save(@RequestBody WhiteIpEntity record) {
        return BaseResponse.success(whiteIpService.saveOrUpdate(record));
    }

    @WebAspect(injectReqId = true, logDesc = "sys->white_ip->delete")
    @PreAuthorize("hasAuthority('sys:white_ip:delete')")
    @PostMapping(value = "/delete")
    public BaseResponse delete(@RequestBody List<String> records) {
        return BaseResponse.success(whiteIpService.removeByIds(records));
    }

    @WebAspect(injectReqId = true, logDesc = "sys->white_ip->findPage")
    @PreAuthorize("hasAuthority('sys:white_ip:view')")
    @PostMapping(value = "/findPage")
    public BaseResponse findPage(@RequestBody PageBean page) {
        return BaseResponse.success(whiteIpService.page(page, page.getQueryWrapper()));
    }
}
