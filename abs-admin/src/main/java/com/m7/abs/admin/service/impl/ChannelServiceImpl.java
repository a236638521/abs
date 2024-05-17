package com.m7.abs.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.mapper.ChannelMapper;
import com.m7.abs.admin.service.IChannelService;
import com.m7.abs.common.domain.entity.ChannelEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public IPage findNavTree(PageBean page) {
        QueryWrapper queryWrapper = page.getQueryWrapper();
        queryWrapper.orderByDesc("create_time");
        return channelMapper.selectPage(page, queryWrapper);
    }
}
