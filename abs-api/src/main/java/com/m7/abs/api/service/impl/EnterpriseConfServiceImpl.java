package com.m7.abs.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.api.mapper.EnterpriseConfMapper;
import com.m7.abs.api.service.IAccountService;
import com.m7.abs.api.service.IEnterpriseConfService;
import com.m7.abs.common.domain.entity.AccountEntity;
import com.m7.abs.common.domain.entity.EnterpriseConfEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Kejie Peng
 * @since 2022-08-09
 */
@Service
public class EnterpriseConfServiceImpl extends ServiceImpl<EnterpriseConfMapper, EnterpriseConfEntity> implements IEnterpriseConfService {
    @Autowired
    private IAccountService accountService;
    @Autowired
    private EnterpriseConfMapper enterpriseConfMapper;

    @Override
    public EnterpriseConfEntity getEnterpriseConfByAccountId(String billAccountId) {
        AccountEntity accountEntity = accountService.getByBillAccountId(billAccountId);
        if (accountEntity == null) {
            return null;
        }
        LambdaQueryWrapper<EnterpriseConfEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterpriseConfEntity::getEnterpriseId, accountEntity.getEnterpriseId());
        queryWrapper.last("LIMIT 1");
        return enterpriseConfMapper.selectOne(queryWrapper);
    }
}
