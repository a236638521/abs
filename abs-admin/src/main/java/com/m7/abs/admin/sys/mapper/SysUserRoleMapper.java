package com.m7.abs.admin.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.m7.abs.common.domain.entity.SysUserRoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SysUserRoleMapper extends BaseMapper<SysUserRoleEntity> {
    List<SysUserRoleEntity> findUserRoles(@Param(value = "userId") String userId);

    int deleteByUserId(@Param(value = "userId") String userId);
}