package com.m7.abs.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.api.domain.dto.midNum.MiddleNumberPoolDto;
import com.m7.abs.api.mapper.MiddleNumberPoolMapper;
import com.m7.abs.api.service.IMiddleNumberPoolService;
import com.m7.abs.common.domain.entity.MiddleNumberPoolEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 小号池 服务实现类
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-14
 */
@Service
public class MiddleNumberPoolServiceImpl extends ServiceImpl<MiddleNumberPoolMapper, MiddleNumberPoolEntity> implements IMiddleNumberPoolService {
    @Autowired
    private MiddleNumberPoolMapper middleNumberPoolMapper;

    @Override
    public MiddleNumberPoolDto getByAccountAndTelX(String account, String telX) {
        return middleNumberPoolMapper.getByAccountAndTelX(account, telX);
    }
}
