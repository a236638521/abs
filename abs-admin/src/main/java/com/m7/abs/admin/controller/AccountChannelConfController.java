package com.m7.abs.admin.controller;

import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.service.IAccountChannelConfService;
import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.AccountChannelConfEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: abs
 * @description: 账户关系 API
 * @author: yx
 * @create: 2021-12-28 10:01
 */
@RestController
@RequestMapping("/accountChannelConf")
public class AccountChannelConfController {

    @Autowired
    private IAccountChannelConfService accountChannelConfService;


    /**
     * 新增/修改账户通道关系
     *
     * @param record
     * @return
     */
    @WebAspect(injectReqId = true, logDesc = "sys->account_channel_conf->saveOrUpdate")
    @PreAuthorize("hasAuthority('sys:account_channel_conf:add') OR hasAuthority('sys:account_channel_conf:edit')")
    @PostMapping(value = "/saveOrUpdate")
    public BaseResponse save(@RequestBody AccountChannelConfEntity record) {
        return accountChannelConfService.saveOrUpdateCustom(record);
    }

    /**
     * 删除账户通道关系
     *
     * @param record
     * @return
     */
    @WebAspect(injectReqId = true, logDesc = "sys->account_channel_conf->delete")
    @PreAuthorize("hasAuthority('sys:account_channel_conf:delete')")
    @PostMapping(value = "/delete")
    public BaseResponse delete(@RequestBody AccountChannelConfEntity record) {
        return accountChannelConfService.deleteById(record);
    }

    /**
     * 账户通道关系列表
     *
     * @param page
     * @return
     */
    @WebAspect(injectReqId = true, logDesc = "sys->account_channel_conf->findPage")
    @PreAuthorize("hasAuthority('sys:account:setUp')")
    @PostMapping(value = "/findPage")
    public BaseResponse findPage(@RequestBody PageBean page) {
        return BaseResponse.success(accountChannelConfService.findByPage(page, page.getQueryWrapper()));
    }

}
