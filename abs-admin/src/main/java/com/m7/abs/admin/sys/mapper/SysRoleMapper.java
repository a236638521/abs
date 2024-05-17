package com.m7.abs.admin.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.m7.abs.common.domain.entity.SysRoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SysRoleMapper extends BaseMapper<SysRoleEntity> {
    List<SysRoleEntity> findByName(@Param(value = "name") String name);

    List<SysRoleEntity> findByUserId(@Param(value = "userId") String userId);
}