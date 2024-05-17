package com.m7.abs.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.m7.abs.api.domain.dto.midNum.MiddleNumberPoolDto;
import com.m7.abs.common.domain.entity.MiddleNumberPoolEntity;

/**
 * <p>
 * 小号池 服务类
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-14
 */
public interface IMiddleNumberPoolService extends IService<MiddleNumberPoolEntity> {

    MiddleNumberPoolDto getByAccountAndTelX(String account, String telX);
}
