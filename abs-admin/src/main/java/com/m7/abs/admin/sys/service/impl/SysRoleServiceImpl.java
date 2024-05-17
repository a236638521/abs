package com.m7.abs.admin.sys.service.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.admin.common.constant.SysConstants;
import com.m7.abs.admin.sys.mapper.SysMenuMapper;
import com.m7.abs.admin.sys.mapper.SysRoleMapper;
import com.m7.abs.admin.sys.mapper.SysRoleMenuMapper;
import com.m7.abs.admin.sys.service.ISysRoleService;
import com.m7.abs.common.domain.entity.SysMenuEntity;
import com.m7.abs.common.domain.entity.SysRoleEntity;
import com.m7.abs.common.domain.entity.SysRoleMenuEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRoleEntity> implements ISysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<SysRoleEntity> findAll() {
        return sysRoleMapper.selectList(Wrappers.emptyWrapper());
    }

    public SysRoleMapper getSysRoleMapper() {
        return sysRoleMapper;
    }

    public void setSysRoleMapper(SysRoleMapper sysRoleMapper) {
        this.sysRoleMapper = sysRoleMapper;
    }

    @Override
    public List<SysMenuEntity> findRoleMenus(String roleId) {
        SysRoleEntity sysRole = sysRoleMapper.selectById(roleId);
        if (SysConstants.ADMIN.equalsIgnoreCase(sysRole.getName())) {
            // 如果是超级管理员，返回全部
            return sysMenuMapper.selectList(Wrappers.emptyWrapper());
        }
        return sysMenuMapper.findRoleMenus(roleId);
    }

    @Transactional
    @Override
    public int saveRoleMenus(List<SysRoleMenuEntity> records) {
        if (records == null || records.isEmpty()) {
            return 1;
        }
        String roleId = records.get(0).getRoleId();
        sysRoleMenuMapper.deleteByRoleId(roleId);
        for (SysRoleMenuEntity record : records) {
            sysRoleMenuMapper.insert(record);
        }
        return 1;
    }

    @Override
    public List<SysRoleEntity> findByName(String name) {
        return sysRoleMapper.findByName(name);
    }

}
