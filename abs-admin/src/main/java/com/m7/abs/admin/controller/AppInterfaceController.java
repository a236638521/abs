package com.m7.abs.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.m7.abs.admin.service.IAppInterfaceService;
import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.AppInterfaceEntity;
import com.m7.abs.common.utils.MyStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 对接接口列表 API Server
 * </p>
 *
 * @author Kejie Peng
 * @since 2022-02-28
 */
@RestController
@RequestMapping("/app_interface")
public class AppInterfaceController {

    @Autowired
    private IAppInterfaceService appInterfaceService;

    @WebAspect(injectReqId = true, logDesc = "sys->app_interface->saveOrUpdate")
    @PreAuthorize("hasAuthority('sys:app_interface:add') AND hasAuthority('sys:app_interface:edit')")
    @PostMapping(value = "/save")
    public BaseResponse save(@RequestBody AppInterfaceEntity record) {
        record.setAccessKey(MyStringUtils.randomUUID());
        record.setSecretKey(MyStringUtils.randomUUID());
        if (StringUtils.isNotEmpty(record.getId())) {
            record.setUpdateTime(new Date());
        } else {
            record.setCreateTime(new Date());
        }
        return BaseResponse.success(appInterfaceService.saveOrUpdate(record));
    }

    @WebAspect(injectReqId = true, logDesc = "sys->app_interface->delete")
    @PreAuthorize("hasAuthority('sys:app_interface:delete')")
    @PostMapping(value = "/delete")
    public BaseResponse delete(@RequestBody List<String> records) {
        return BaseResponse.success(appInterfaceService.removeByIds(records));
    }

    @WebAspect(injectReqId = true, logDesc = "sys->app_interface->getById")
    @PostMapping(value = "/getByEnterpriseId")
    @PreAuthorize("hasAuthority('sys:app_interface:view')")
    public BaseResponse getByEnterpriseId(@RequestBody String enterpriseId) {
        QueryWrapper<AppInterfaceEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("enterprise_id", enterpriseId);
        return BaseResponse.success(appInterfaceService.getOne(queryWrapper));
    }
}
