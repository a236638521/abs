package com.m7.abs.admin.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.m7.abs.common.domain.entity.SysRoleDeptEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysRoleDeptMapper extends BaseMapper<SysRoleDeptEntity> {
}