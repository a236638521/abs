package com.m7.abs.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.api.domain.dto.ChannelConfigDto;
import com.m7.abs.api.mapper.ChannelMapper;
import com.m7.abs.api.service.IChannelService;
import com.m7.abs.common.domain.entity.ChannelEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 通道表 服务实现类
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-14
 */
@Service
public class ChannelServiceImpl extends ServiceImpl<ChannelMapper, ChannelEntity> implements IChannelService {
    @Autowired
    private ChannelMapper channelMapper;

    @Override
    public ChannelConfigDto getChannelConfigInfo(String channelId, String accountId) {
        return channelMapper.getChannelConfigInfo(channelId, accountId);
    }

    @Override
    public ChannelConfigDto getChannelConfigInfoByAccountAndChannelCode(String account, String channelCode) {
        return channelMapper.getChannelConfigInfoByAccountAndChannelCode(account, channelCode);
    }

    @Override
    public List<ChannelConfigDto> getChannelConfigInfoByAccount(String account) {
        return channelMapper.getChannelConfigInfoByAccount(account);
    }
}
