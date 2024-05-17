package com.m7.abs.admin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.m7.abs.common.domain.entity.ProjectEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 项目表 Mapper 接口
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-14
 */
@Mapper
@Repository
@DS("abs_config")
public interface ProjectMapper extends BaseMapper<ProjectEntity> {

}
