package com.m7.abs.admin.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.m7.abs.common.domain.entity.SysMenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SysMenuMapper extends BaseMapper<SysMenuEntity> {

    List<SysMenuEntity> findByUserName(@Param(value = "userName") String userName, @Param("onlyMenu") Boolean onlyMenu, @Param("projectId") String projectId);

    List<SysMenuEntity> findRoleMenus(@Param(value = "roleId") String roleId);

    List<SysMenuEntity> findAllByUserName(@Param(value = "userName") String userName, @Param("onlyMenu") Boolean onlyMenu);
}