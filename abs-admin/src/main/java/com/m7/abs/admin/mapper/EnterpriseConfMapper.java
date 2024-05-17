package com.m7.abs.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.m7.abs.common.domain.entity.EnterpriseConfEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Kejie Peng
 * @since 2022-08-09
 */
@Mapper
@Repository
public interface EnterpriseConfMapper extends BaseMapper<EnterpriseConfEntity> {

}
