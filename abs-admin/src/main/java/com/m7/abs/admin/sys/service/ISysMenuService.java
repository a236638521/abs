package com.m7.abs.admin.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.m7.abs.common.domain.entity.SysMenuEntity;

import java.util.List;

/**
 * 菜单管理
 *
 * @author Louis
 * @date Oct 29, 2018
 */
public interface ISysMenuService extends IService<SysMenuEntity> {

    /**
     * 查询菜单树,用户ID和用户名为空则查询全部
     *
     * @param menuType 获取菜单类型，0：获取所有菜单，包含按钮，1：获取所有菜单，不包含按钮
     * @return
     */
    List<SysMenuEntity> findTree(String userName, int menuType, boolean onlyMenu, String projectId);

    /**
     * 根据用户名查找菜单列表
     *
     * @param userName
     * @return
     */
    List<SysMenuEntity> findByUser(String userName, Boolean onlyMenu, String projectId);

    /**
     * 获取所有数据
     *
     * @param userName
     * @param onlyMenu
     * @return
     */
    List<SysMenuEntity> findAllByUser(String userName, Boolean onlyMenu);

    /**
     * 查询菜单树所有数据
     *
     * @param userName
     * @param menuType
     * @param onlyMenu
     * @return
     */
    List<SysMenuEntity> findAllTree(String userName, int menuType, boolean onlyMenu);
}
