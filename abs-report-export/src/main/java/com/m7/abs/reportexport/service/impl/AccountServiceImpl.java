package com.m7.abs.reportexport.service.impl;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.common.domain.entity.AccountEntity;
import com.m7.abs.reportexport.mapper.AccountMapper;
import com.m7.abs.reportexport.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 账户表 服务实现类
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-14
 */
@Service
@DS("abs_config")
public class AccountServiceImpl extends ServiceImpl<AccountMapper, AccountEntity> implements IAccountService {
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public AccountEntity getAccountById(String id) {
        LambdaQueryWrapper<AccountEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.and(orWrapper -> {
            orWrapper.eq(AccountEntity::getId, id);
            orWrapper.or();
            orWrapper.eq(AccountEntity::getBillAccountId, id);
        });
        return accountMapper.selectOne(queryWrapper);
    }
}
