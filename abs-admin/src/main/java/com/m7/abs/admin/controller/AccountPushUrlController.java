package com.m7.abs.admin.controller;

import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.service.IAccountPushUrlService;
import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.AccountPushUrlEntity;
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
 * @since 2023-04-10
 */
@RestController
@RequestMapping("/account_push_url")
public class AccountPushUrlController {

    @Autowired
    private IAccountPushUrlService accountPushUrlService;

    @WebAspect(injectReqId = true, logDesc = "sys->account_push_url->saveOrUpdate")
    @PreAuthorize("hasAuthority('sys:account_push_url:add') AND hasAuthority('sys:account_push_url:edit')")
    @PostMapping(value = "/saveOrUpdate")
    public BaseResponse save(@RequestBody AccountPushUrlEntity record) {
        return BaseResponse.success(accountPushUrlService.saveOrUpdate(record));
    }

    @WebAspect(injectReqId = true, logDesc = "sys->account_push_url->delete")
    @PreAuthorize("hasAuthority('sys:account_push_url:delete')")
    @PostMapping(value = "/delete")
    public BaseResponse delete(@RequestBody List<String> records) {
        return BaseResponse.success(accountPushUrlService.removeByIds(records));
    }

    @WebAspect(injectReqId = true, logDesc = "sys->account_push_url->findPage")
    @PreAuthorize("hasAuthority('sys:account_push_url:view')")
    @PostMapping(value = "/findPage")
    public BaseResponse findPage(@RequestBody PageBean page) {
        return BaseResponse.success(accountPushUrlService.page(page, page.getQueryWrapper()));
    }
}
