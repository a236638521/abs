package com.m7.abs.admin.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.m7.abs.admin.common.constant.SysConstants;
import com.m7.abs.admin.sys.mapper.SysRoleMapper;
import com.m7.abs.common.utils.FastJsonUtils;
import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.admin.sys.service.ISysRoleService;
import com.m7.abs.common.domain.entity.SysRoleEntity;
import com.m7.abs.common.domain.entity.SysRoleMenuEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色控制器
 *
 * @author Louis
 * @date Oct 29, 2018
 */
@Slf4j
@RestController
@RequestMapping("/role")
public class SysRoleController {

    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @PreAuthorize("hasAuthority('sys:role:add') OR hasAuthority('sys:role:edit')")
    @PostMapping(value = "/saveOrUpdate")
    public BaseResponse save(@RequestBody SysRoleEntity record) {
        String id = record.getId();
        SysRoleEntity role = sysRoleService.getById(id);
        if (role != null) {
            if (SysConstants.ADMIN.equalsIgnoreCase(role.getName())) {
                return BaseResponse.error("超级管理员不允许修改!");
            }
        }
        // 新增角色

        if (StringUtils.isNotEmpty(id) && !sysRoleService.findByName(record.getName()).isEmpty()) {
            return BaseResponse.error("角色名已存在!");
        }
        return BaseResponse.success(sysRoleService.saveOrUpdate(record));
    }

    @PreAuthorize("hasAuthority('sys:role:delete')")
    @PostMapping(value = "/delete")
    public BaseResponse delete(@RequestBody List<String> records) {
        return BaseResponse.success(sysRoleService.removeByIds(records));
    }

    @PreAuthorize("hasAuthority('sys:role:view')")
    @PostMapping(value = "/findPage")
    public BaseResponse findPage(@RequestBody PageBean pageBean) {
        Page<SysRoleEntity> page = (Page<SysRoleEntity>) sysRoleService.page(pageBean, pageBean.getQueryWrapper());
        log.info("角色分页查询防护数据:" + FastJsonUtils.toJSONString(page));
        return BaseResponse.success(page);
    }

    @PreAuthorize("hasAuthority('sys:role:view')")
    @GetMapping(value = "/findAll")
    public BaseResponse findAll() {
        return BaseResponse.success(sysRoleService.findAll());
    }

    @PreAuthorize("hasAuthority('sys:role:view')")
    @GetMapping(value = "/findRoleMenus")
    public BaseResponse findRoleMenus(@RequestParam String roleId) {
        return BaseResponse.success(sysRoleService.findRoleMenus(roleId));
    }

    @PreAuthorize("hasAuthority('sys:role:view')")
    @PostMapping(value = "/saveRoleMenus")
    public BaseResponse saveRoleMenus(@RequestBody List<SysRoleMenuEntity> records) {
        for (SysRoleMenuEntity record : records) {
            SysRoleEntity sysRole = sysRoleMapper.selectById(record.getRoleId());
            if (SysConstants.ADMIN.equalsIgnoreCase(sysRole.getName())) {
                // 如果是超级管理员，不允许修改
                return BaseResponse.error("超级管理员拥有所有菜单权限，不允许修改！");
            }
        }
        return BaseResponse.success(sysRoleService.saveRoleMenus(records));
    }
}
