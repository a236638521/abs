package com.m7.abs.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.m7.abs.api.domain.dto.ChannelConfigDto;
import com.m7.abs.common.domain.entity.ChannelEntity;

import java.util.List;

/**
 * <p>
 * 通道表 服务类
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-14
 */
public interface IChannelService extends IService<ChannelEntity> {
    /**
     * 获取channel配置信息
     *
     * @param channelId
     * @return
     */
    ChannelConfigDto getChannelConfigInfo(String channelId, String accountId);

    /**
     * 通过账号和通道信息获取通道配置
     * @param account
     * @param channelCode
     * @return
     */
    ChannelConfigDto getChannelConfigInfoByAccountAndChannelCode(String account, String channelCode);

    /**
     * 获取账户所有通道配置信息
     * @param account
     * @return
     */
    List<ChannelConfigDto> getChannelConfigInfoByAccount(String account);
}
