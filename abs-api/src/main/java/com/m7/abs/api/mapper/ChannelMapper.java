package com.m7.abs.api.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.m7.abs.api.domain.dto.ChannelConfigDto;
import com.m7.abs.common.domain.entity.ChannelEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 通道表 Mapper 接口
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-14
 */
@Mapper
@Repository
@DS("abs_config")
public interface ChannelMapper extends BaseMapper<ChannelEntity> {

    /**
     * 获取通道配置信息
     *
     * @param channelId
     * @return
     */
    ChannelConfigDto getChannelConfigInfo(String channelId, String accountId);

    /**
     * 获取通道配置信息
     *
     * @param account
     * @param channelCode
     * @return
     */
    ChannelConfigDto getChannelConfigInfoByAccountAndChannelCode(String account, String channelCode);

    /**
     *
     * @param account
     * @return
     */
    List<ChannelConfigDto> getChannelConfigInfoByAccount(String account);
}
