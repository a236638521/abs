package com.m7.abs.admin.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.m7.abs.admin.common.constant.SysConstants;
import com.m7.abs.admin.core.security.SecurityUtil;
import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.domain.vo.sysUser.ChangePasswordVO;
import com.m7.abs.admin.domain.vo.sysUser.SysUserPageVO;
import com.m7.abs.admin.domain.vo.sysUser.SysUserVO;
import com.m7.abs.admin.sys.service.ISysUserService;
import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.SysUserEntity;
import com.m7.abs.common.utils.PasswordUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 用户控制器
 *
 * @author Louis
 * @date Oct 29, 2018
 */
@RestController
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    private ISysUserService sysUserService;

    @WebAspect(logDesc = "sys->user->save")
    @PreAuthorize("hasAuthority('sys:user:add') OR hasAuthority('sys:user:edit')")
    @PostMapping(value = "/saveOrUpdate")
    public BaseResponse save(@RequestBody SysUserEntity record) {
        SysUserEntity user = sysUserService.getById(record.getId());
        if (user != null) {
            if (SysConstants.ADMIN.equalsIgnoreCase(user.getUsername())) {
                return BaseResponse.error("超级管理员不允许修改!");
            }
            record.setLastUpdateTime(new Date());
        } else {
            record.setCreateTime(new Date());
        }
        if (record.getPassword() != null) {
            String salt = PasswordUtils.getSalt();
            if (user == null) {
                // 新增用户
                if (sysUserService.findByName(record.getUsername()) != null) {
                    return BaseResponse.error("用户名已存在!");
                }
                String password = PasswordUtils.encode(record.getPassword(), salt);
                record.setSalt(salt);
                record.setPassword(password);
            } else {
                // 修改用户, 且修改了密码
                if (!record.getPassword().equals(user.getPassword())) {
                    String password = PasswordUtils.encode(record.getPassword(), salt);
                    record.setSalt(salt);
                    record.setPassword(password);
                }
            }
        }

        return BaseResponse.success(sysUserService.saveSysUser(record));
    }

    @WebAspect(logDesc = "sys->user->changePassword")
    @PostMapping(value = "/changePassword")
    public BaseResponse changePassword(@RequestBody ChangePasswordVO record) {
        String username = SecurityUtil.getUsername();

        if (StringUtils.isNotEmpty(username)) {
            SysUserVO sysUserVO = sysUserService.findByName(username);
            if (sysUserVO != null) {
                String oldPassword = record.getOldPassword();

                if (!PasswordUtils.matches(sysUserVO.getSalt(), oldPassword, sysUserVO.getPassword())) {
                    return BaseResponse.error("旧密码不正确");
                }

                String salt = PasswordUtils.getSalt();
                if (!record.getNewPassword().equals(sysUserVO.getPassword())) {
                    SysUserEntity sysUserEntity = new SysUserEntity();
                    String password = PasswordUtils.encode(record.getNewPassword(), salt);
                    sysUserEntity.setId(sysUserVO.getId());
                    sysUserEntity.setSalt(salt);
                    sysUserEntity.setPassword(password);
                    boolean b = sysUserService.updateById(sysUserEntity);
                    if (b) {
                        BaseResponse.success();
                    }
                }
            } else {
                return BaseResponse.error("用户不存在");
            }
        } else {
            return BaseResponse.error("用户登录状态已过期，请重新登录");
        }

        return BaseResponse.error("操作失败");
    }

    @WebAspect(logDesc = "sys->user->delete")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    @PostMapping(value = "/delete")
    public BaseResponse delete(@RequestBody List<String> records) {

        for (String record : records) {
            SysUserEntity sysUser = sysUserService.getById(record);
            if (sysUser != null && SysConstants.ADMIN.equalsIgnoreCase(sysUser.getUsername())) {
                return BaseResponse.error("超级管理员不允许删除!");
            }

        }
        return BaseResponse.success(sysUserService.removeByIds(records));
    }

    @WebAspect(logDesc = "sys->user->findByName")
    @PreAuthorize("hasAuthority('sys:user:view')")
    @GetMapping(value = "/findByName")
    public BaseResponse findByUserName(@RequestParam String name) {
        return BaseResponse.success(sysUserService.findByName(name));
    }

    @WebAspect(logDesc = "sys->user->findPermissions")
    @PreAuthorize("hasAuthority('sys:isLogin')")
    @GetMapping(value = "/findPermissions")
    public BaseResponse findPermissions(@RequestParam String name) {
        return BaseResponse.success(sysUserService.findPermissions(name));
    }

    @WebAspect(logDesc = "sys->user->findUserRoles")
    @PreAuthorize("hasAuthority('sys:user:view')")
    @GetMapping(value = "/findUserRoles")
    public BaseResponse findUserRoles(@RequestParam String userId) {
        return BaseResponse.success(sysUserService.findUserRoles(userId));
    }

    @WebAspect(logDesc = "sys->user->findPage")
    @PreAuthorize("hasAuthority('sys:user:view')")
    @PostMapping(value = "/findPage")
    public BaseResponse findPage(@RequestBody PageBean pageBean) {
        Page<SysUserPageVO> page = sysUserService.findSysUserByPage(pageBean);
        return BaseResponse.success(page);
    }

}
