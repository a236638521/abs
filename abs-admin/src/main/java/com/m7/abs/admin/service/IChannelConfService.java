package com.m7.abs.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.ChannelConfEntity;

/**
 * <p>
 * 通道配置 服务类
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-14
 */
public interface IChannelConfService extends IService<ChannelConfEntity> {

    BaseResponse changeStatus(ChannelConfEntity record);
}
