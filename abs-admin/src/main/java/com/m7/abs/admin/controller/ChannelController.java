package com.m7.abs.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.service.IChannelService;
import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.ChannelEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 通道表 API Server
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-14
 */
@RestController
@RequestMapping("/channel")
public class ChannelController {

    @Autowired
    private IChannelService channelService;

    /**
     * 新增/修改通道
     *
     * @param record
     * @return
     */
    @WebAspect(injectReqId = true, logDesc = "sys->channel->saveOrUpdate")
    @PreAuthorize("hasAuthority('sys:channel:add') OR hasAuthority('sys:channel:edit')")
    @PostMapping(value = "/saveOrUpdate")
    public BaseResponse save(@RequestBody ChannelEntity record) {
        return BaseResponse.success(channelService.saveOrUpdate(record));
    }

    /**
     * 删除通道
     *
     * @param records
     * @return
     */
    @WebAspect(injectReqId = true, logDesc = "sys->channel->delete")
    @PreAuthorize("hasAuthority('sys:channel:delete')")
    @PostMapping(value = "/delete")
    public BaseResponse delete(@RequestBody List<String> records) {
        return BaseResponse.success(channelService.removeByIds(records));
    }

    /**
     * 通道列表
     *
     * @param page
     * @return
     */
    @WebAspect(injectReqId = true, logDesc = "sys->channel->findPage")
    @PreAuthorize("hasAuthority('sys:channel:view')")
    @PostMapping(value = "/findPage")
    public BaseResponse findPage(@RequestBody PageBean page) {
        return BaseResponse.success(channelService.page(page, page.getQueryWrapper()));
    }

    /**
     * 通道列表
     *
     * @param page
     * @return
     */
    @WebAspect(injectReqId = true, logDesc = "sys->channel->findNavTree")
    @PreAuthorize("hasAuthority('sys:channel:view')")
    @PostMapping(value = "/findNavTree")
    public BaseResponse findNavTree(@RequestBody PageBean page) {
        return BaseResponse.success(channelService.findNavTree(page));
    }

    @WebAspect(injectReqId = true, logDesc = "sys->channel->findDropdownPage")
    @PostMapping(value = "/findDropdownPage")
    public BaseResponse findDropdownPage(@RequestBody PageBean page) {
        QueryWrapper queryWrapper = page.getQueryWrapper();
        queryWrapper.select("id", "name");
        queryWrapper.orderByDesc("create_time");
        IPage pageResult = channelService.page(page, queryWrapper);
        return BaseResponse.success(pageResult);
    }
}
