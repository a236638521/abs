package com.m7.abs.admin.sys.controller;


import com.m7.abs.admin.sys.service.ISysMenuService;
import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.SysMenuEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单控制器
 *
 * @author Louis
 * @date Oct 29, 2018
 */
@RestController
@RequestMapping("/menu")
public class SysMenuController {

    @Autowired
    private ISysMenuService sysMenuService;

    @WebAspect(logDesc = "sys:menu:save")
    @PreAuthorize("hasAuthority('sys:menu:add') OR hasAuthority('sys:menu:edit')")
    @PostMapping(value = "/saveOrUpdate")
    public BaseResponse save(@RequestBody SysMenuEntity record) {
        String projectId = record.getProjectId();
        boolean b = sysMenuService.saveOrUpdate(record);
        Boolean autoAddBtu = record.getAutoAddBtu();
        if (b && autoAddBtu != null && autoAddBtu) {
            String id = record.getId();
            SysMenuEntity add = SysMenuEntity.builder()
                    .parentId(id)
                    .projectId(projectId)
                    .type(2)
                    .name("新增")
                    .perms(record.getPermsPrefix() + "add")
                    .orderNum(1).build();
            sysMenuService.saveOrUpdate(add);
            SysMenuEntity edit = SysMenuEntity.builder()
                    .type(2)
                    .projectId(projectId)
                    .parentId(id)
                    .name("编辑")
                    .perms(record.getPermsPrefix() + "edit")
                    .orderNum(2).build();
            sysMenuService.saveOrUpdate(edit);
            SysMenuEntity delete = SysMenuEntity.builder()
                    .parentId(id)
                    .projectId(projectId)
                    .type(2)
                    .name("删除")
                    .perms(record.getPermsPrefix() + "delete")
                    .orderNum(3).build();
            sysMenuService.saveOrUpdate(delete);
        }
        return BaseResponse.success();
    }

    @WebAspect(logDesc = "sys:menu:delete")
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    @PostMapping(value = "/delete")
    public BaseResponse delete(@RequestBody List<String> records) {
        return BaseResponse.success(sysMenuService.removeByIds(records));
    }

    @WebAspect(logDesc = "sys:menu:findNavTree")
    @PreAuthorize("hasAuthority('sys:isLogin')")
    @GetMapping(value = "/findNavTree")
    public BaseResponse findNavTree(@RequestParam String username, String projectId) {
        return BaseResponse.success(sysMenuService.findTree(username, 1, true, projectId));
    }

    @WebAspect(logDesc = "sys:menu:findMenuTree")
    @PreAuthorize("hasAuthority('sys:isLogin')")
    @GetMapping(value = "/findMenuTree")
    public BaseResponse findMenuTree(boolean onlyMenu) {
        return BaseResponse.success(sysMenuService.findAllTree(null, 0, onlyMenu));
    }

}
