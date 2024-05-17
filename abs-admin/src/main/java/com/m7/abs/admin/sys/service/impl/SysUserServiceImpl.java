package com.m7.abs.admin.sys.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.domain.vo.sysUser.SysUserPageVO;
import com.m7.abs.admin.domain.vo.sysUser.SysUserVO;
import com.m7.abs.admin.sys.mapper.SysDeptMapper;
import com.m7.abs.admin.sys.mapper.SysRoleMapper;
import com.m7.abs.admin.sys.mapper.SysUserMapper;
import com.m7.abs.admin.sys.mapper.SysUserRoleMapper;
import com.m7.abs.admin.sys.service.ISysMenuService;
import com.m7.abs.admin.sys.service.ISysUserService;
import com.m7.abs.common.domain.entity.*;
import com.m7.abs.common.utils.BeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserEntity> implements ISysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private ISysMenuService sysMenuService;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Transactional
    @Override
    public String saveSysUser(SysUserEntity record) {
        String id = null;
        String recordId = record.getId();
        if (StringUtils.isEmpty(recordId)) {
            // 新增用户
            sysUserMapper.insert(record);
            id = record.getId();
        } else {
            // 更新用户信息
            sysUserMapper.updateById(record);
        }

        if (id != null) {
            for (SysUserRoleEntity sysUserRole : record.getUserRoles()) {
                sysUserRole.setUserId(id);
            }
        } else {
            sysUserRoleMapper.deleteByUserId(recordId);
        }
        for (SysUserRoleEntity sysUserRole : record.getUserRoles()) {
            sysUserRoleMapper.insert(sysUserRole);
        }
        return id;
    }

    @Override
    public SysUserVO findByName(String username) {
        return sysUserMapper.findByName(username);
    }


    @Override
    public Set<String> findPermissions(String userName) {
        Set<String> permsSet = new HashSet<>();
        List<SysMenuEntity> sysMenus = sysMenuService.findAllByUser(userName, false);
        for (SysMenuEntity sysMenu : sysMenus) {
            String perms = sysMenu.getPerms();
            if (StringUtils.isNotEmpty(perms)) {
                if (perms.indexOf(",") != -1) {
                    String[] split = perms.split(",");
                    Arrays.stream(split).forEach(item -> {
                        permsSet.add(item);
                    });
                } else {
                    permsSet.add(perms);
                }
            }
        }
        return permsSet;
    }

    @Override
    public List<SysUserRoleEntity> findUserRoles(String userId) {
        return sysUserRoleMapper.findUserRoles(userId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Page<SysUserPageVO> findSysUserByPage(PageBean pageBean) {


        Page<SysUserEntity> sysUserEntityIPage = new Page<>(pageBean.getCurrent(), pageBean.getSize());
        QueryWrapper queryWrapper = pageBean.getQueryWrapper();

        sysUserEntityIPage = (Page<SysUserEntity>) sysUserMapper.selectPage(sysUserEntityIPage, queryWrapper);

        List<SysUserEntity> records = sysUserEntityIPage.getRecords();

        Page<SysUserPageVO> resultPage = new Page(sysUserEntityIPage.getCurrent(), sysUserEntityIPage.getSize(), sysUserEntityIPage.getTotal());
        List<SysUserPageVO> resultRecords = new ArrayList<>();

        if (records != null && records.size() > 0) {
            for (SysUserEntity sysUserEntity : records) {
                SysUserPageVO sysUserPageVO = new SysUserPageVO();
                BeanUtil.setVOToVO(sysUserEntity, sysUserPageVO);


                String deptId = sysUserEntity.getDeptId();
                String userId = sysUserEntity.getId();
                SysDeptEntity sysDeptEntity = sysDeptMapper.selectById(deptId);
                if (sysDeptEntity != null) {
                    sysUserPageVO.setDeptName(sysDeptEntity.getName());
                }

                StringBuilder roleNames = new StringBuilder();

                List<SysRoleEntity> sysRoleEntities = sysRoleMapper.findByUserId(userId);
                List<SysUserRoleEntity> userRoleEntities = new ArrayList<>();
                if (sysRoleEntities != null && sysRoleEntities.size() > 0) {
                    for (int i = 0; i < sysRoleEntities.size(); i++) {

                        SysRoleEntity sysRoleEntity = sysRoleEntities.get(i);
                        if (sysRoleEntity != null) {
                            SysUserRoleEntity sysUserRoleEntity = new SysUserRoleEntity();
                            String roleName = sysRoleEntity.getName();
                            if (i != sysRoleEntities.size() - 1) {
                                roleNames.append(roleName + ";");
                            } else {
                                roleNames.append(roleName);
                            }

                            sysUserRoleEntity.setRoleId(sysRoleEntity.getId());
                            userRoleEntities.add(sysUserRoleEntity);
                        }
                    }

                }

                sysUserPageVO.setUserRoles(userRoleEntities);
                sysUserPageVO.setRoleNames(roleNames.toString());
                resultRecords.add(sysUserPageVO);
            }
        }

        resultPage.setRecords(resultRecords);
        return resultPage;
    }
}
