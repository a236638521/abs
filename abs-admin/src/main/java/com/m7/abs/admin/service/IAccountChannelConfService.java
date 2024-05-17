package com.m7.abs.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.domain.dto.AccountChannelConfPageDTO;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.AccountChannelConfEntity;

/**
 * <p>
 * 账户通道关系表 服务类
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-14
 */
public interface IAccountChannelConfService extends IService<AccountChannelConfEntity> {

    BaseResponse deleteById(AccountChannelConfEntity record);

    IPage<AccountChannelConfPageDTO> findByPage(PageBean page, QueryWrapper queryWrapper);

    BaseResponse saveOrUpdateCustom(AccountChannelConfEntity record);
}
