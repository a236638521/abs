package com.m7.abs.admin.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.admin.sys.service.ISysLogService;
import com.m7.abs.common.domain.entity.SysLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日志控制器
 *
 * @author Louis
 * @date Oct 29, 2018
 */
@RestController
@RequestMapping("/log")
public class SysLogController {

    @Autowired
    private ISysLogService sysLogService;

    @PreAuthorize("hasAuthority('sys:log:view')")
    @PostMapping(value = "/findPage")
    public BaseResponse findPage(@RequestBody Page<SysLogEntity> page) {
        IPage<SysLogEntity> pageResult = sysLogService.page(page);
        return BaseResponse.success(pageResult);
    }
}
