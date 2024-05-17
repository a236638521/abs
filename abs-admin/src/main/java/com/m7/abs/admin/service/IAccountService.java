package com.m7.abs.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.domain.dto.AccountPageDTO;
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

    IPage<AccountPageDTO> findByPage(PageBean page, QueryWrapper queryWrapper);


    boolean saveOrUpdateAccount(AccountEntity record);
}
