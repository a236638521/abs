package com.m7.abs.admin.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.admin.common.constant.SysConstants;
import com.m7.abs.admin.sys.mapper.SysMenuMapper;
import com.m7.abs.admin.sys.service.ISysMenuService;
import com.m7.abs.common.domain.entity.SysMenuEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenuEntity> implements ISysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;


    @Override
    public List<SysMenuEntity> findTree(String userName, int menuType, boolean onlyMenu, String projectId) {
        List<SysMenuEntity> sysMenus = new ArrayList<>();
        List<SysMenuEntity> menus = findByUser(userName, onlyMenu, projectId);
        for (SysMenuEntity menu : menus) {
            String parentId = menu.getParentId();
            if (StringUtils.isEmpty(parentId) || "0".equals(parentId)) {
                menu.setLevel(0);
                if (!exists(sysMenus, menu)) {
                    sysMenus.add(menu);
                }
            }
        }
        sysMenus.sort((o1, o2) -> o1.getOrderNum().compareTo(o2.getOrderNum()));
        findChildren(sysMenus, menus, menuType);
        return sysMenus;
    }

    @Override
    public List<SysMenuEntity> findByUser(String userName, Boolean onlyMenu, String projectId) {
        if (userName == null || "".equals(userName) || SysConstants.ADMIN.equalsIgnoreCase(userName)) {
            QueryWrapper<SysMenuEntity> queryWrapper = new QueryWrapper();

            queryWrapper.eq("status", 0);

            if (onlyMenu != null && onlyMenu) {
                queryWrapper.and(wrapper -> {
                    wrapper.eq("type", 0);
                    wrapper.or();
                    wrapper.eq("type", 1);
                });
            }

            if (StringUtils.isNotEmpty(projectId)) {
                queryWrapper.and(wrapper -> {
                    wrapper.isNull("project_id");
                    wrapper.or();
                    wrapper.eq("project_id", projectId);
                    wrapper.or();
                    wrapper.eq("project_id", "");
                });
            } else {
                queryWrapper.and(wrapper -> {
                    wrapper.isNull("project_id");
                    wrapper.or();
                    wrapper.eq("project_id", "");
                });
            }

            return sysMenuMapper.selectList(queryWrapper);
        }

        return sysMenuMapper.findByUserName(userName, onlyMenu, projectId);
    }

    @Override
    public List<SysMenuEntity> findAllTree(String userName, int menuType, boolean onlyMenu) {
        List<SysMenuEntity> sysMenus = new ArrayList<>();
        List<SysMenuEntity> menus = findAllByUser(userName, onlyMenu);
        for (SysMenuEntity menu : menus) {
            String parentId = menu.getParentId();
            if (StringUtils.isEmpty(parentId) || "0".equals(parentId)) {
                menu.setLevel(0);
                if (!exists(sysMenus, menu)) {
                    sysMenus.add(menu);
                }
            }
        }
        sysMenus.sort((o1, o2) -> o1.getOrderNum().compareTo(o2.getOrderNum()));
        findChildren(sysMenus, menus, menuType);
        return sysMenus;
    }

    @Override
    public List<SysMenuEntity> findAllByUser(String userName, Boolean onlyMenu) {
        if (userName == null || "".equals(userName) || SysConstants.ADMIN.equalsIgnoreCase(userName)) {
            QueryWrapper<SysMenuEntity> queryWrapper = new QueryWrapper();

            queryWrapper.eq("status", 0);

            if (onlyMenu != null && onlyMenu) {
                queryWrapper.and(wrapper -> {
                    wrapper.eq("type", 0);
                    wrapper.or();
                    wrapper.eq("type", 1);
                });
            }

            return sysMenuMapper.selectList(queryWrapper);
        }

        return sysMenuMapper.findAllByUserName(userName, onlyMenu);
    }

    private void findChildren(List<SysMenuEntity> SysMenus, List<SysMenuEntity> menus, int menuType) {
        for (SysMenuEntity SysMenu : SysMenus) {
            List<SysMenuEntity> children = new ArrayList<>();
            for (SysMenuEntity menu : menus) {
                if (menuType == 1 && menu.getType() == 2) {
                    // 如果是获取类型不需要按钮，且菜单类型是按钮的，直接过滤掉
                    continue;
                }
                if (SysMenu.getId() != null && SysMenu.getId().equals(menu.getParentId())) {
                    menu.setParentName(SysMenu.getName());
                    menu.setLevel(SysMenu.getLevel() + 1);
                    if (!exists(children, menu)) {
                        children.add(menu);
                    }
                }
            }
            SysMenu.setChildren(children);
            children.sort((o1, o2) -> o1.getOrderNum().compareTo(o2.getOrderNum()));
            findChildren(children, menus, menuType);
        }
    }

    private boolean exists(List<SysMenuEntity> sysMenus, SysMenuEntity sysMenu) {
        boolean exist = false;
        for (SysMenuEntity menu : sysMenus) {
            if (menu.getId().equals(sysMenu.getId())) {
                exist = true;
            }
        }
        return exist;
    }

}
