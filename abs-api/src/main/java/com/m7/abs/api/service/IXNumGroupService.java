package com.m7.abs.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.m7.abs.common.domain.entity.XNumGroupChannelConfEntity;
import com.m7.abs.common.domain.entity.XNumGroupEntity;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Kejie Peng
 * @since 2022-08-02
 */
public interface IXNumGroupService extends IService<XNumGroupEntity> {

    XNumGroupChannelConfEntity getGroupConfInfo(String groupId, String channelId);
}
