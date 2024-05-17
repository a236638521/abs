package com.m7.abs.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.m7.abs.admin.service.IAccountFlashSmConfService;
import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.AccountFlashSmConfEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 账户通道关系表 API Server
 * </p>
 *
 * @author Kejie Peng
 * @since 2022-06-16
 */
@RestController
@RequestMapping("/account_flash_sm_conf")
public class AccountFlashSmConfController {

    @Autowired
    private IAccountFlashSmConfService accountFlashSmConfService;

    @WebAspect(injectReqId = true, logDesc = "sys->account_flash_sm_conf->getById")
    @PostMapping(value = "/getByAccountId")
    @PreAuthorize("hasAuthority('sys:account:edit')")
    public BaseResponse getByAccountId(@RequestBody String accountId) {
        LambdaQueryWrapper<AccountFlashSmConfEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AccountFlashSmConfEntity::getAccountId, accountId);
        return BaseResponse.success(accountFlashSmConfService.list(queryWrapper));
    }
}
