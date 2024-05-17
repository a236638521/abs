package com.m7.abs.admin.controller;


import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.service.IChannelConfService;
import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.ChannelConfEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 通道配置 API Server
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-14
 */
@RestController
@RequestMapping("/channel_conf")
public class ChannelConfController {

    @Autowired
    private IChannelConfService channelConfService;

    @WebAspect(injectReqId = true, logDesc = "sys->channel_conf->saveOrUpdate")
    @PreAuthorize("hasAuthority('sys:channel_conf:add') AND hasAuthority('sys:channel_conf:edit')")
    @PostMapping(value = "/saveOrUpdate")
    public BaseResponse save(@RequestBody ChannelConfEntity record) {
        return BaseResponse.success(channelConfService.saveOrUpdate(record));
    }

    /**
     * 启用/停用账户
     *
     * @param record
     * @return
     */
    @WebAspect(injectReqId = true, logDesc = "sys->channel_conf->changeStatus")
    @PreAuthorize("hasAuthority('sys:channel_conf:enable') OR hasAuthority('sys:channel_conf:disable')")
    @PostMapping(value = "/changeStatus")
    public BaseResponse changeStatus(@RequestBody ChannelConfEntity record) {
        return BaseResponse.success(channelConfService.changeStatus(record));
    }

    @WebAspect(injectReqId = true, logDesc = "sys->channel_conf->delete")
    @PreAuthorize("hasAuthority('sys:channel_conf:delete')")
    @PostMapping(value = "/delete")
    public BaseResponse delete(@RequestBody List<String> records) {
        return BaseResponse.success(channelConfService.removeByIds(records));
    }

    @WebAspect(injectReqId = true, logDesc = "sys->channel_conf->findPage")
    @PreAuthorize("hasAuthority('sys:channel_conf:view')")
    @PostMapping(value = "/findPage")
    public BaseResponse findPage(@RequestBody PageBean page) {
        return BaseResponse.success(channelConfService.page(page, page.getQueryWrapper()));
    }
}
