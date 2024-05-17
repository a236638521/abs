package com.m7.abs.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.m7.abs.common.domain.entity.XNumGroupEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Kejie Peng
 * @since 2022-08-02
 */
@Mapper
@Repository
public interface XNumGroupMapper extends BaseMapper<XNumGroupEntity> {

}
