package com.m7.abs.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.admin.mapper.ChannelConfMapper;
import com.m7.abs.admin.service.IChannelConfService;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.ChannelConfEntity;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 通道配置 服务实现类
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-14
 */
@Service
public class ChannelConfServiceImpl extends ServiceImpl<ChannelConfMapper, ChannelConfEntity> implements IChannelConfService {

    @Override
    public BaseResponse changeStatus(ChannelConfEntity record) {
        String id = record.getId();
        ChannelConfEntity confEntity = this.getById(id);
        if (confEntity == null) {
            return BaseResponse.error("未知配置");
        }
        Integer status = record.getStatus();
        if (status == null) {
            return BaseResponse.error("参数异常");
        }

        if (status.equals(1)) {
            String channelId = confEntity.getChannelId();
            QueryWrapper<ChannelConfEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("channel_id", channelId);
            queryWrapper.eq("status", 1);
            queryWrapper.last("limit 1");
            ChannelConfEntity one = this.getOne(queryWrapper);
            if (one != null) {
                return BaseResponse.error("同一通道只允许启用一个配置。");
            }

        }
        confEntity.setStatus(record.getStatus());
        this.updateById(confEntity);

        return BaseResponse.error("未知配置");
    }
}
