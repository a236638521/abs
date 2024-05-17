package com.m7.abs.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.domain.dto.MiddleNumberBindLogDTO;
import com.m7.abs.admin.service.IAccountService;
import com.m7.abs.admin.service.IMiddleNumberBindLogService;
import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.AccountEntity;
import com.m7.abs.common.domain.entity.MiddleNumberBindLogEntity;
import com.m7.abs.common.utils.BeanUtil;
import com.m7.abs.common.utils.MyStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @program: abs
 * @description: 小号池绑定记录
 * @author: yx
 * @create: 2021-12-23 11:51
 */
@RestController
@RequestMapping("/middleNumberBindLog")
public class MiddleNumberBindLogController {

    @Autowired
    private IMiddleNumberBindLogService middleNumberBindLogService;
    @Autowired
    private IAccountService accountService;

    @WebAspect(injectReqId = true, logDesc = "sys->middle_number_bind_log->findPage")
    @PreAuthorize("hasAuthority('sys:middle_number_bind_log:view')")
    @PostMapping(value = "/findPage")
    public BaseResponse findPage(@RequestBody PageBean page) {
        PageBean returnPage = middleNumberBindLogService.page(page, page.getQueryWrapper());
        List<MiddleNumberBindLogEntity> records = returnPage.getRecords();

        List<MiddleNumberBindLogDTO> newRecords = new ArrayList<>();
        Set<String> accountSet = new HashSet<>();
        if (records != null) {
            records.stream().forEach(item -> {
                item.setTelA(MyStringUtils.encryptPhoneNumber(item.getTelA()));
                item.setTelB(MyStringUtils.encryptPhoneNumber(item.getTelB()));
                item.setTelX(MyStringUtils.encryptPhoneNumber(item.getTelX()));
                MiddleNumberBindLogDTO cdrDTO = MiddleNumberBindLogDTO.builder().build();
                BeanUtil.setVOToVO(item, cdrDTO);
                newRecords.add(cdrDTO);
                accountSet.add(item.getAccountId());
            });
        }


        if (accountSet.size()>0){
            LambdaQueryWrapper<AccountEntity> queryWrapper = new LambdaQueryWrapper<>();
            Set<String> finalAccountSet = accountSet;
            queryWrapper.and(orWrapper -> {
                orWrapper.in(AccountEntity::getBillAccountId, finalAccountSet);
                orWrapper.or();
                orWrapper.in(AccountEntity::getId, finalAccountSet);
            });
            List<AccountEntity> list = accountService.list(queryWrapper);
            Map<String, String> accountNameMap = new HashMap<>();
            if (list != null) {
                accountNameMap = list.parallelStream().collect(Collectors.toMap(AccountEntity::getBillAccountId, AccountEntity::getName));
                accountNameMap.putAll( list.parallelStream().collect(Collectors.toMap(AccountEntity::getId, AccountEntity::getName)));
            }

            if (accountNameMap.size() > 0) {
                for (int i = 0; i < newRecords.size(); i++) {
                    MiddleNumberBindLogDTO cdrDTO = newRecords.get(i);
                    cdrDTO.setAccountName(accountNameMap.get(cdrDTO.getAccountId()));
                }
            }
        }

        returnPage.setRecords(newRecords);

        return BaseResponse.success(returnPage);
    }


}
