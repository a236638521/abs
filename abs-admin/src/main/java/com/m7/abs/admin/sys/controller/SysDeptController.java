package com.m7.abs.admin.sys.controller;


import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.admin.sys.service.ISysDeptService;
import com.m7.abs.common.domain.entity.SysDeptEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 机构控制器
 *
 * @author Louis
 * @date Oct 29, 2018
 */
@RestController
@RequestMapping("/dept")
public class SysDeptController {

    @Autowired
    private ISysDeptService sysDeptService;

    @PreAuthorize("hasAuthority('sys:dept:add') OR hasAuthority('sys:dept:edit')")
    @PostMapping(value = "/saveOrUpdate")
    public BaseResponse save(@RequestBody SysDeptEntity record) {
        return BaseResponse.success(sysDeptService.saveOrUpdate(record));
    }

    @PreAuthorize("hasAuthority('sys:dept:delete')")
    @PostMapping(value = "/delete")
    public BaseResponse delete(@RequestBody List<String> records) {
        return BaseResponse.success(sysDeptService.removeByIds(records));
    }

    @PreAuthorize("hasAuthority('sys:dept:view')")
    @GetMapping(value = "/findTree")
    public BaseResponse findTree() {
        return BaseResponse.success(sysDeptService.findTree());
    }

}
