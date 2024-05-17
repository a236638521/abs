package com.m7.abs.api.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.api.mapper.AccountMapper;
import com.m7.abs.api.service.IAccountService;
import com.m7.abs.common.constant.common.DataStatusEnum;
import com.m7.abs.common.domain.entity.AccountEntity;
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
public class AccountServiceImpl extends ServiceImpl<AccountMapper, AccountEntity> implements IAccountService {
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public AccountEntity getByBillAccountId(String billAccountId) {
        LambdaQueryWrapper<AccountEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AccountEntity::getBillAccountId, billAccountId);
        queryWrapper.eq(AccountEntity::getStatus, DataStatusEnum.ENABLE.getValue());
        queryWrapper.last("LIMIT 1");
        return accountMapper.selectOne(queryWrapper);
    }
}
