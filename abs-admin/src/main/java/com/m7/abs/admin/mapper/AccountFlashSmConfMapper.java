package com.m7.abs.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.m7.abs.common.domain.entity.AccountFlashSmConfEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 账户通道关系表 Mapper 接口
 * </p>
 *
 * @author Kejie Peng
 * @since 2022-06-16
 */
@Mapper
@Repository
public interface AccountFlashSmConfMapper extends BaseMapper<AccountFlashSmConfEntity> {

}
