package com.m7.abs.api.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.m7.abs.common.domain.entity.AccountEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 账户表 Mapper 接口
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-14
 */
@Mapper
@Repository
@DS("abs_config")
public interface AccountMapper extends BaseMapper<AccountEntity> {

}
