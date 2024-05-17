package com.m7.abs.api.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.api.mapper.AccountPushUrlMapper;
import com.m7.abs.api.service.IAccountPushUrlService;
import com.m7.abs.common.domain.entity.AccountPushUrlEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 账户推送地址表 服务实现类
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-14
 */
@Service
public class AccountPushUrlServiceImpl extends ServiceImpl<AccountPushUrlMapper, AccountPushUrlEntity> implements IAccountPushUrlService {
    @Autowired
    private AccountPushUrlMapper accountPushUrlMapper;

    @Override
    public List<AccountPushUrlEntity> getByAccountId(String accountId, String type) {
        LambdaQueryWrapper<AccountPushUrlEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AccountPushUrlEntity::getAccountId, accountId);
        wrapper.eq(AccountPushUrlEntity::getType, type);
        return accountPushUrlMapper.selectList(wrapper);
    }
}
