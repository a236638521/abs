package com.m7.abs.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.service.IProjectService;
import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.ProjectEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 项目表 API Server
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-14
 */
@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private IProjectService projectService;

    @WebAspect(injectReqId = true, logDesc = "sys->project->saveOrUpdate")
    @PreAuthorize("hasAuthority('sys:project:add') OR hasAuthority('sys:project:edit')")
    @PostMapping(value = "/saveOrUpdate")
    public BaseResponse save(@RequestBody ProjectEntity record) {
        return BaseResponse.success(projectService.saveOrUpdate(record));
    }

    @WebAspect(injectReqId = true, logDesc = "sys->project->delete")
    @PreAuthorize("hasAuthority('sys:project:delete')")
    @PostMapping(value = "/delete")
    public BaseResponse delete(@RequestBody List<String> records) {
        return BaseResponse.success(projectService.removeByIds(records));
    }

    @WebAspect(injectReqId = true, logDesc = "sys->project->findPage")
    @PostMapping(value = "/findPage")
    public BaseResponse findPage(@RequestBody PageBean page) {
        return BaseResponse.success(projectService.page(page, page.getQueryWrapper()));
    }

    @WebAspect(injectReqId = true, logDesc = "sys->project->findDropdownPage")
    @PostMapping(value = "/findDropdownPage")
    public BaseResponse findDropdownPage(@RequestBody PageBean page) {
        QueryWrapper queryWrapper = page.getQueryWrapper();
        queryWrapper.select("id", "name");
        queryWrapper.orderByDesc("create_time");
        IPage pageResult = projectService.page(page, queryWrapper);
        return BaseResponse.success(pageResult);
    }
}
