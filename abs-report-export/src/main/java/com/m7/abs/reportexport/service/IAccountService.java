package com.m7.abs.reportexport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.m7.abs.common.domain.entity.AccountEntity;

/**
 * <p>
 * 账户表 服务类
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-14
 */
public interface IAccountService extends IService<AccountEntity> {
    /**
     * 通话账户ID或者billId获取账户
     * @param id
     * @return
     */
    AccountEntity getAccountById(String id);
}
