package com.m7.abs.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.m7.abs.common.domain.entity.AccountPushUrlEntity;

import java.util.List;

/**
 * <p>
 * 账户推送地址表 服务类
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-14
 */
public interface IAccountPushUrlService extends IService<AccountPushUrlEntity> {

    /**
     * 获取账户推送地址
     * @param accountId
     * @param type
     * @return
     */
    List<AccountPushUrlEntity> getByAccountId(String accountId, String type);
}
