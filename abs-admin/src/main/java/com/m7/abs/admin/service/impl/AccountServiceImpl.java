package com.m7.abs.admin.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.domain.dto.AccountPageDTO;
import com.m7.abs.admin.mapper.AccountMapper;
import com.m7.abs.admin.service.IAccountService;
import com.m7.abs.common.domain.entity.AccountEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
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
    public IPage<AccountPageDTO> findByPage(PageBean page, QueryWrapper queryWrapper) {
        return accountMapper.findByPage(page, queryWrapper);
    }

    @Override
    public boolean saveOrUpdateAccount(AccountEntity record) {
        String id = record.getId();
        if (StringUtils.isEmpty(id)) {//新增
            record.setBillAccountId(getAccountId());
        }
        return this.saveOrUpdate(record);
    }

    private String getAccountId() {
        String accountId = RandomStringUtils.randomAlphanumeric(16);
        LambdaQueryWrapper<AccountEntity> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(AccountEntity::getBillAccountId, accountId);
        lambdaQueryWrapper.last("LIMIT 1");
        AccountEntity accountEntity = accountMapper.selectOne(lambdaQueryWrapper);
        if (accountEntity != null) {
            log.warn("have the same account id.retry");
            return getAccountId();
        } else {
            return accountId;
        }
    }
}
