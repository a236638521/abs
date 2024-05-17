package com.m7.abs.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.service.IFlashSmTemplateService;
import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.FlashSmTemplateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * API Server
 * </p>
 *
 * @author Kejie Peng
 * @since 2022-05-31
 */
@RestController
@RequestMapping("/flashSmTemplate")
public class FlashSmTemplateController {

    @Autowired
    private IFlashSmTemplateService flashSmTemplateService;

    @WebAspect(injectReqId = true, logDesc = "sys->flash_sm_template->saveOrUpdate")
    @PreAuthorize("hasAuthority('sys:flash_sm_template:add') AND hasAuthority('sys:flash_sm_template:edit')")
    @PostMapping(value = "/saveOrUpdate")
    public BaseResponse saveOrUpdate(@RequestBody FlashSmTemplateEntity record) {
        boolean b = flashSmTemplateService.saveOrUpdateFlashSmTemplate(record);
        return BaseResponse.success();
    }

    @WebAspect(injectReqId = true, logDesc = "sys->flash_sm_template->delete")
    @PreAuthorize("hasAuthority('sys:flash_sm_template:delete')")
    @PostMapping(value = "/delete")
    public BaseResponse delete(@RequestBody List<String> records) {
        return BaseResponse.success(flashSmTemplateService.removeByIds(records));
    }

    @WebAspect(injectReqId = true, logDesc = "sys->flash_sm_template->findPage")
    @PreAuthorize("hasAuthority('sys:flash_sm_template:view')")
    @PostMapping(value = "/findPage")
    public BaseResponse findPage(@RequestBody PageBean page) {
        return BaseResponse.success(flashSmTemplateService.findFlashSmTemplateByPage(page, page.getQueryWrapper()));
    }

    @WebAspect(injectReqId = true, logDesc = "sys->account->findDropdownPage")
    @PostMapping(value = "/findDropdownPage")
    public BaseResponse findDropdownPage(@RequestBody PageBean page) {
        QueryWrapper queryWrapper = page.getQueryWrapper();
        queryWrapper.select("id", "name");
        queryWrapper.orderByDesc("create_time");
        IPage pageResult = flashSmTemplateService.page(page, queryWrapper);
        return BaseResponse.success(pageResult);
    }
}
