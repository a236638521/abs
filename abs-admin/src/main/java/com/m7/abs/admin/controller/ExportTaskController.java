package com.m7.abs.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.m7.abs.admin.core.security.SecurityUtil;
import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.domain.vo.export.ExportTaskVO;
import com.m7.abs.admin.service.IExportTaskService;
import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.domain.base.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * API Server
 * </p>
 *
 * @author Kejie Peng
 * @since 2023-03-24
 */
@RestController
@RequestMapping("/export_task")
public class ExportTaskController {

    @Autowired
    private IExportTaskService exportTaskService;

    @WebAspect(injectReqId = true, logDesc = "sys->export_task->saveOrUpdate")
    @PostMapping(value = "/saveOrUpdate")
    public BaseResponse save(@RequestBody ExportTaskVO record) {
        return exportTaskService.createExportTask(record);
    }

    @WebAspect(injectReqId = true, logDesc = "sys->export_task->findPage")
    @PostMapping(value = "/findPage")
    public BaseResponse findPage(@RequestBody PageBean page) {
        QueryWrapper queryWrapper = page.getQueryWrapper();
        queryWrapper.eq("create_by", SecurityUtil.getUsername());
        return BaseResponse.success(exportTaskService.page(page, queryWrapper));
    }
}
