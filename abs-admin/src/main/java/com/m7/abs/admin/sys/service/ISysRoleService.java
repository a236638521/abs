package com.m7.abs.admin.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.m7.abs.common.domain.entity.SysMenuEntity;
import com.m7.abs.common.domain.entity.SysRoleEntity;
import com.m7.abs.common.domain.entity.SysRoleMenuEntity;

import java.util.List;

/**
 * 角色管理
 *
 * @author Louis
 * @date Oct 29, 2018
 */
public interface ISysRoleService extends IService<SysRoleEntity> {

    /**
     * 查询全部
     *
     * @return
     */
    List<SysRoleEntity> findAll();

    /**
     * 查询角色菜单集合
     *
     * @return
     */
    List<SysMenuEntity> findRoleMenus(String roleId);

    /**
     * 保存角色菜单
     *
     * @param records
     * @return
     */
    int saveRoleMenus(List<SysRoleMenuEntity> records);

    /**
     * 根据名称查询
     *
     * @param name
     * @return
     */
    List<SysRoleEntity> findByName(String name);

}
