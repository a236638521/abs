package com.m7.abs.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.service.IDispatcherFailLogService;
import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.constant.error.code.ErrorCodeConstant;
import com.m7.abs.common.domain.base.BaseRequest;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.bo.support.RetryPushDataBO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: abs
 * @description: 推送失败记录管理
 * @author: yx
 * @create: 2021-12-30 14:26
 */
@RestController
@RequestMapping("/dispatcherFailLog")
public class DispatcherFailLogController {

    @Autowired
    private IDispatcherFailLogService dispatcherFailLogService;

    /**
     * 推送失败记录列表
     *
     * @param page
     * @return
     */
    @WebAspect(injectReqId = true, logDesc = "sys->dispatcher_fail_log->findPage")
    @PreAuthorize("hasAuthority('sys:dispatcher_fail_log:view')")
    @PostMapping(value = "/findPage")
    public BaseResponse findPage(@RequestBody PageBean page) {
        QueryWrapper queryWrapper = page.getQueryWrapper();
        return BaseResponse.success(dispatcherFailLogService.page(page, queryWrapper));
    }


    /**
     * 批量重推推送失败记录
     *
     * @param request
     * @return
     */
    @WebAspect(injectReqId = true, logDesc = "sys->account_channel_conf->heavy_push")
    @PreAuthorize("hasAuthority('sys:dispatcher_fail_log:heavy_push')")
    @PostMapping(value = "/heavyPush")
    public BaseResponse heavyPush(@RequestBody BaseRequest<List<RetryPushDataBO>> request) {
        List<RetryPushDataBO> param = request.getParam();
        if (CollectionUtils.isEmpty(param)) {
            return BaseResponse.fail(ErrorCodeConstant.CommonErrorCode.PARAM_ERR.getCode()
                    , ErrorCodeConstant.CommonErrorCode.PARAM_ERR.getErrMsg());
        }
        for (RetryPushDataBO retryPushDataBO : param) {
            if (StringUtils.isEmpty(retryPushDataBO.getTaskId())) {
                return BaseResponse.fail(ErrorCodeConstant.CommonErrorCode.PARAM_ERR.getCode()
                        , ErrorCodeConstant.CommonErrorCode.PARAM_ERR.getErrMsg());
            }
        }
        return dispatcherFailLogService.heavyPush(request);
    }

}
