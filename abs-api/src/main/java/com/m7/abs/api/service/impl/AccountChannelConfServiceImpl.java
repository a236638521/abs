package com.m7.abs.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.api.mapper.AccountChannelConfMapper;
import com.m7.abs.api.service.IAccountChannelConfService;
import com.m7.abs.common.domain.entity.AccountChannelConfEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 账户通道关系表 服务实现类
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-14
 */
@Service
public class AccountChannelConfServiceImpl extends ServiceImpl<AccountChannelConfMapper, AccountChannelConfEntity> implements IAccountChannelConfService {
    @Autowired
    private AccountChannelConfMapper accountChannelConfMapper;

}
