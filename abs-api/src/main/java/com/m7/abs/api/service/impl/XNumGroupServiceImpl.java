package com.m7.abs.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.api.mapper.XNumGroupChannelConfMapper;
import com.m7.abs.api.mapper.XNumGroupMapper;
import com.m7.abs.api.service.IXNumGroupService;
import com.m7.abs.common.domain.entity.XNumGroupChannelConfEntity;
import com.m7.abs.common.domain.entity.XNumGroupEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Kejie Peng
 * @since 2022-08-02
 */
@Service
public class XNumGroupServiceImpl extends ServiceImpl<XNumGroupMapper, XNumGroupEntity> implements IXNumGroupService {
    @Autowired
    private XNumGroupChannelConfMapper xNumGroupChannelConfMapper;

    @Override
    public XNumGroupChannelConfEntity getGroupConfInfo(String groupId, String channelId) {
        LambdaQueryWrapper<XNumGroupChannelConfEntity> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(XNumGroupChannelConfEntity::getChannelId,channelId);
        wrapper.eq(XNumGroupChannelConfEntity::getGroupId,groupId);
        return xNumGroupChannelConfMapper.selectOne(wrapper);
    }
}
