package com.m7.abs.admin.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.domain.vo.sysUser.SysUserPageVO;
import com.m7.abs.admin.domain.vo.sysUser.SysUserVO;
import com.m7.abs.common.domain.entity.SysUserEntity;
import com.m7.abs.common.domain.entity.SysUserRoleEntity;

import java.util.List;
import java.util.Set;

/**
 * 用户管理
 *
 * @author Louis
 * @date Oct 29, 2018
 */
public interface ISysUserService extends IService<SysUserEntity> {

    String saveSysUser(SysUserEntity record);

    SysUserVO findByName(String username);

    /**
     * 查找用户的菜单权限标识集合
     *
     * @param userName
     * @return
     */
    Set<String> findPermissions(String userName);

    /**
     * 查找用户的角色集合
     *
     * @return
     */
    List<SysUserRoleEntity> findUserRoles(String userId);

    /**
     * 分页查询
     *
     * @param searchBean
     * @return
     */
    Page<SysUserPageVO> findSysUserByPage(PageBean searchBean);
}
