package com.m7.abs.admin.controller;

import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.service.IOssUploadFailLogService;
import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.constant.error.code.ErrorCodeConstant;
import com.m7.abs.common.domain.base.BaseRequest;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.bo.support.RetrySaveToOssBO;
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
 * @description: 录音转存失败管理
 * @author: yx
 * @create: 2021-12-30 14:51
 */
@RestController
@RequestMapping("/ossUploadFailLog")
public class OssUploadFailLogController {

    @Autowired
    private IOssUploadFailLogService ossUploadFailLogService;

    /**
     * 录音转存失败记录列表
     *
     * @param page
     * @return
     */
    @WebAspect(injectReqId = true, logDesc = "sys->oss_upload_fail_log->findPage")
    @PreAuthorize("hasAuthority('sys:oss_upload_fail_log:view')")
    @PostMapping(value = "/findPage")
    public BaseResponse findPage(@RequestBody PageBean page) {
        return BaseResponse.success(ossUploadFailLogService.page(page, page.getQueryWrapper()));
    }


    /**
     * 批量重新转存
     *
     * @param request
     * @return
     */
    @WebAspect(injectReqId = true, logDesc = "sys->oss_upload_fail_log->heavy_download")
    @PreAuthorize("hasAuthority('sys:oss_upload_fail_log:heavy_download')")
    @PostMapping(value = "/heavyDownload")
    public BaseResponse heavyDownload(@RequestBody BaseRequest<List<RetrySaveToOssBO>> request) {
        List<RetrySaveToOssBO> recordList = request.getParam();
        if (CollectionUtils.isEmpty(recordList)) {
            return BaseResponse.fail(ErrorCodeConstant.CommonErrorCode.PARAM_ERR.getCode()
                    , ErrorCodeConstant.CommonErrorCode.PARAM_ERR.getErrMsg());
        }
        for (RetrySaveToOssBO retrySaveToOssBO : recordList) {
            if (StringUtils.isEmpty(retrySaveToOssBO.getTaskId())) {
                return BaseResponse.fail(ErrorCodeConstant.CommonErrorCode.PARAM_ERR.getCode()
                        , ErrorCodeConstant.CommonErrorCode.PARAM_ERR.getErrMsg());
            }
        }
        return ossUploadFailLogService.heavyDownload(request);
    }


}
