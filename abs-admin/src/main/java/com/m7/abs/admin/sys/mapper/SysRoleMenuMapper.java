package com.m7.abs.admin.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.m7.abs.common.domain.entity.SysRoleMenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenuEntity> {
    List<SysRoleMenuEntity> findRoleMenus(@Param(value = "roleId") String roleId);

    List<SysRoleMenuEntity> findAll();

    int deleteByRoleId(@Param(value = "roleId") String roleId);
}