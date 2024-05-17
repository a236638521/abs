package com.m7.abs.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.domain.vo.account.AccountInsertVO;
import com.m7.abs.admin.service.IAccountFlashSmConfService;
import com.m7.abs.admin.service.IAccountService;
import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.constant.common.DataStatusEnum;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.AccountEntity;
import com.m7.abs.common.domain.entity.AccountFlashSmConfEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 账户表 API Server
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-14
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private IAccountService accountService;
    @Autowired
    private IAccountFlashSmConfService accountFlashSmConfService;

    /**
     * 新增/修改账户
     *
     * @param record
     * @return
     */
    @WebAspect(injectReqId = true, logDesc = "sys->account->saveOrUpdate")
    @PreAuthorize("hasAuthority('sys:account:add') OR hasAuthority('sys:account:edit')")
    @PostMapping(value = "/saveOrUpdate")
    public BaseResponse saveOrUpdate(@RequestBody AccountInsertVO record) {
        AccountEntity accountEntity = record.getAccountEntity();
        boolean b = accountService.saveOrUpdateAccount(accountEntity);
        if (b) {
            /**
             * 跟新闪信配置
             */
            List<AccountFlashSmConfEntity> flashSmConfList = record.getFlashSmConfList();
            flashSmConfList.stream().forEach(item -> {
                item.setAccountId(accountEntity.getId());
                item.setStatus(DataStatusEnum.ENABLE.getValue());
                accountFlashSmConfService.saveOrUpdate(item);
            });
        }
        return BaseResponse.success();
    }

    @WebAspect(injectReqId = true, logDesc = "sys->account->getById")
    @PreAuthorize("hasAuthority('sys:account:edit')")
    @PostMapping(value = "/getById")
    public BaseResponse getById(@RequestBody String id) {
        QueryWrapper<AccountEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        return BaseResponse.success(accountService.getOne(queryWrapper));
    }


    /**
     * 启用/停用账户
     *
     * @param record
     * @return
     */
    @WebAspect(injectReqId = true, logDesc = "sys->account->changeStatus")
    @PreAuthorize("hasAuthority('sys:account:enable') OR hasAuthority('sys:account:disable')")
    @PostMapping(value = "/changeStatus")
    public BaseResponse changeStatus(@RequestBody AccountEntity record) {
        return BaseResponse.success(accountService.updateById(record));
    }


    /**
     * 删除账户
     *
     * @param records
     * @return
     */
    @WebAspect(injectReqId = true, logDesc = "sys->account->delete")
    @PreAuthorize("hasAuthority('sys:account:delete')")
    @PostMapping(value = "/delete")
    public BaseResponse delete(@RequestBody List<String> records) {
        return BaseResponse.success(accountService.removeByIds(records));
    }

    /**
     * 账户列表
     *
     * @param page
     * @return
     */
    @WebAspect(injectReqId = true, logDesc = "sys->account->findPage")
    @PreAuthorize("hasAuthority('sys:account:view')")
    @PostMapping(value = "/findPage")
    public BaseResponse findPage(@RequestBody PageBean page) {
        return BaseResponse.success(accountService.findByPage(page, page.getQueryWrapper()));
    }

    @WebAspect(injectReqId = true, logDesc = "sys->account->findDropdownPage")
    @PostMapping(value = "/findDropdownPage")
    public BaseResponse findDropdownPage(@RequestBody PageBean page) {
        QueryWrapper queryWrapper = page.getQueryWrapper();
        queryWrapper.select("id", "name");
        queryWrapper.orderByDesc("create_time");
        IPage pageResult = accountService.page(page, queryWrapper);
        return BaseResponse.success(pageResult);
    }
}
