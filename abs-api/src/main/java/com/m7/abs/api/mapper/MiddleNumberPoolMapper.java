package com.m7.abs.api.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.m7.abs.api.domain.dto.midNum.MiddleNumberPoolDto;
import com.m7.abs.common.domain.entity.MiddleNumberPoolEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 小号池 Mapper 接口
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-14
 */
@Mapper
@Repository
@DS("abs_config")
public interface MiddleNumberPoolMapper extends BaseMapper<MiddleNumberPoolEntity> {

    MiddleNumberPoolDto getByAccountAndTelX(String account, String number);
}
