package com.m7.abs.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.domain.vo.enterprise.EnterpriseSaveOrUpdateVO;
import com.m7.abs.admin.service.IEnterpriseConfService;
import com.m7.abs.admin.service.IEnterpriseService;
import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.EnterpriseConfEntity;
import com.m7.abs.common.domain.entity.EnterpriseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * API Server
 * </p>
 *
 * @author Kejie Peng
 * @since 2022-02-28
 */
@RestController
@RequestMapping("/enterprise")
public class EnterpriseController {

    @Autowired
    private IEnterpriseService enterpriseService;
    @Autowired
    private IEnterpriseConfService enterpriseConfService;

    @WebAspect(injectReqId = true, logDesc = "sys->enterprise->saveOrUpdate")
    @PreAuthorize("hasAuthority('sys:enterprise:add') AND hasAuthority('sys:enterprise:edit')")
    @PostMapping(value = "/save")
    public BaseResponse save(@RequestBody EnterpriseSaveOrUpdateVO record) {
        return BaseResponse.success(enterpriseService.enterpriseSaveOrUpdate(record));
    }

    @WebAspect(injectReqId = true, logDesc = "sys->enterprise->delete")
    @PreAuthorize("hasAuthority('sys:enterprise:delete')")
    @PostMapping(value = "/delete")
    public BaseResponse delete(@RequestBody List<String> records) {
        return BaseResponse.success(enterpriseService.removeByIds(records));
    }

    @WebAspect(injectReqId = true, logDesc = "sys->enterprise->findPage")
    @PreAuthorize("hasAuthority('sys:enterprise:view')")
    @PostMapping(value = "/findPage")
    public BaseResponse findPage(@RequestBody PageBean page) {
        return BaseResponse.success(enterpriseService.page(page, page.getQueryWrapper()));
    }

    @WebAspect(injectReqId = true, logDesc = "sys->enterprise->findDropdownPage")
    @PostMapping(value = "/findDropdownPage")
    public BaseResponse findDropdownPage(@RequestBody PageBean page) {
        QueryWrapper queryWrapper = page.getQueryWrapper();
        queryWrapper.select("id", "name");
        queryWrapper.orderByDesc("create_time");
        IPage pageResult = enterpriseService.page(page, queryWrapper);
        return BaseResponse.success(pageResult);
    }

    @WebAspect(injectReqId = true, logDesc = "sys->enterprise->getById")
    @PreAuthorize("hasAuthority('sys:enterprise:edit')")
    @PostMapping(value = "/getById")
    public BaseResponse getById(@RequestBody String id) {
        QueryWrapper<EnterpriseEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        queryWrapper.select("id", "name", "enterprise_code", "description", "status");
        EnterpriseEntity enterpriseEntity = enterpriseService.getOne(queryWrapper);
        LambdaQueryWrapper<EnterpriseConfEntity> queryConfWrapper = new LambdaQueryWrapper<>();
        queryConfWrapper.eq(EnterpriseConfEntity::getEnterpriseId, id);
        EnterpriseConfEntity enterpriseConfEntity = enterpriseConfService.getOne(queryConfWrapper);
        HashMap<String, Object> result = new HashMap<>();
        result.put("enterpriseEntity", enterpriseEntity);
        result.put("enterpriseConfEntity", enterpriseConfEntity);
        return BaseResponse.success(result);
    }
}
