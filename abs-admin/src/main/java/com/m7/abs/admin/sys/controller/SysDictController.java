package com.m7.abs.admin.sys.controller;


import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.admin.sys.service.ISysDictService;
import com.m7.abs.common.domain.entity.SysDictEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典控制器
 *
 * @author Louis
 * @date Oct 29, 2018
 */
@RestController
@RequestMapping("/dict")
public class SysDictController {

    @Autowired
    private ISysDictService sysDictService;

    @PreAuthorize("hasAuthority('sys:dict:add') OR hasAuthority('sys:dict:edit')")
    @PostMapping(value = "/saveOrUpdate")
    public BaseResponse save(@RequestBody SysDictEntity record) {
        return BaseResponse.success(sysDictService.saveOrUpdate(record));
    }

    @PreAuthorize("hasAuthority('sys:dict:delete')")
    @PostMapping(value = "/delete")
    public BaseResponse delete(@RequestBody List<String> records) {
        return BaseResponse.success(sysDictService.removeByIds(records));
    }

    @PreAuthorize("hasAuthority('sys:dict:view')")
    @PostMapping(value = "/findPage")
    public BaseResponse findPage(@RequestBody PageBean page) {
        return BaseResponse.success(sysDictService.page(page, page.getQueryWrapper()));
    }

    @PreAuthorize("hasAuthority('sys:dict:view')")
    @GetMapping(value = "/findByLabel")
    public BaseResponse findByLabel(@RequestParam String lable) {
        return BaseResponse.success(sysDictService.findByLabel(lable));
    }
}
