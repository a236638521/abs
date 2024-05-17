package com.m7.abs.admin.sys.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.m7.abs.admin.domain.vo.sysUser.SysUserPageVO;
import com.m7.abs.admin.domain.vo.sysUser.SysUserVO;
import com.m7.abs.common.domain.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
@DS("abs_config")
public interface SysUserMapper extends BaseMapper<SysUserEntity> {
    SysUserVO findByName(@Param(value = "username") String name);

    Page<SysUserPageVO> findSysUserByPage(Page<SysUserPageVO> pageRequest);
}