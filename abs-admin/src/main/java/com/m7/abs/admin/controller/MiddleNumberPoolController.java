package com.m7.abs.admin.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.domain.dto.MiddleNumberPoolPageDTO;
import com.m7.abs.admin.service.IMiddleNumberPoolService;
import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.MiddleNumberPoolEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 小号池 API Server
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-14
 */
@RestController
@RequestMapping("/middle_number_pool")
public class MiddleNumberPoolController {

    @Autowired
    private IMiddleNumberPoolService middleNumberPoolService;

    @WebAspect(injectReqId = true, logDesc = "sys->middle_number_pool->saveOrUpdate")
    @PreAuthorize("hasAuthority('sys:middle_number_pool:add') OR hasAuthority('sys:middle_number_pool:edit')")
    @PostMapping(value = "/saveOrUpdate")
    public BaseResponse save(@RequestBody MiddleNumberPoolEntity record) {
        return BaseResponse.success(middleNumberPoolService.saveOrUpdate(record));
    }

    @WebAspect(injectReqId = true, logDesc = "sys->middle_number_pool->delete")
    @PreAuthorize("hasAuthority('sys:middle_number_pool:delete')")
    @PostMapping(value = "/delete")
    public BaseResponse delete(@RequestBody List<String> records) {
        return BaseResponse.success(middleNumberPoolService.removeByIds(records));
    }

    @WebAspect(injectReqId = true, logDesc = "sys->middle_number_pool->findPage")
    @PreAuthorize("hasAuthority('sys:middle_number_pool:view')")
    @PostMapping(value = "/findPage")
    public BaseResponse<IPage<MiddleNumberPoolPageDTO>> findPage(@RequestBody PageBean page) {
        return BaseResponse.success(middleNumberPoolService.findByPage(page, page.getQueryWrapper()));
    }

    @WebAspect(injectReqId = true, logDesc = "sys->middle_number_pool->savesOrUpdates")
    @PreAuthorize("hasAuthority('sys:middle_number_pool:adds') OR hasAuthority('sys:middle_number_pool:edits')")
    @PostMapping(value = "/savesOrUpdates")
    public BaseResponse save(@RequestBody List<MiddleNumberPoolEntity> recordList) {
        return BaseResponse.success(middleNumberPoolService.saveOrUpdateBatch(recordList));
    }

}
