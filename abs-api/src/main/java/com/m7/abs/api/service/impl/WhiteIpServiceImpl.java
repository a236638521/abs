package com.m7.abs.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.api.mapper.WhiteIpMapper;
import com.m7.abs.api.service.IWhiteIpService;
import com.m7.abs.common.constant.common.DataStatusEnum;
import com.m7.abs.common.domain.entity.WhiteIpEntity;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Kejie Peng
 * @since 2022-03-01
 */
@Service
public class WhiteIpServiceImpl extends ServiceImpl<WhiteIpMapper, WhiteIpEntity> implements IWhiteIpService {

    @Override
    public boolean checkIpAddress(String ip) {
        LambdaQueryWrapper<WhiteIpEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WhiteIpEntity::getIp, ip);
        queryWrapper.eq(WhiteIpEntity::getStatus, DataStatusEnum.ENABLE.getValue());
        queryWrapper.select(WhiteIpEntity::getId);
        queryWrapper.last("LIMIT 1");
        WhiteIpEntity one = this.getOne(queryWrapper);
        if (one != null) {
            return true;
        }
        return false;
    }
}
