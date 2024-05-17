package com.m7.abs.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.common.domain.entity.ChannelEntity;

/**
 * <p>
 * 通道表 服务类
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-14
 */
public interface IChannelService extends IService<ChannelEntity> {
    IPage findNavTree(PageBean page);
}
