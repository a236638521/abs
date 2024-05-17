package com.m7.abs.support.controller;

import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.constant.requestPath.SupportRequestPath;
import com.m7.abs.common.domain.base.BaseRequest;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.bo.support.RecordingDumpBO;
import com.m7.abs.common.domain.bo.support.RetrySaveToOssBO;
import com.m7.abs.common.domain.vo.support.PreSignedUrlVO;
import com.m7.abs.common.domain.vo.support.RecordingDumpVO;
import com.m7.abs.common.domain.vo.support.RetrySaveToOssVO;
import com.m7.abs.support.core.storage.MixCloudStorage;
import com.m7.abs.support.domain.bo.PreSignedUrlBO;
import com.m7.abs.support.service.impl.RecordingDumpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhuhf
 */
@RestController
public class RecordingDumpController {
    private final RecordingDumpService saveToOssService;
    @Autowired
    private MixCloudStorage cloudStorage;

    public RecordingDumpController(RecordingDumpService saveToOssService) {
        this.saveToOssService = saveToOssService;
    }

    @PostMapping(SupportRequestPath.RECORDING_DUMP)
    @WebAspect(injectReqId = true, logDesc = "文件转存接口")
    public BaseResponse<RecordingDumpVO> saveToOss(@Validated @RequestBody BaseRequest<RecordingDumpBO> request) {
        return saveToOssService.recordingDump(request);
    }

    @PostMapping(SupportRequestPath.RECORDING_DUMP_RETRY)
    @WebAspect(injectReqId = true, logDesc = "文件转存重试接口")
    public BaseResponse<RetrySaveToOssVO> retryPush(@Validated @RequestBody BaseRequest<List<RetrySaveToOssBO>> bo) {
        if (CollectionUtils.isEmpty(bo.getParam())) {
            return BaseResponse.success();
        }
        return saveToOssService.retrySubmitTask(bo.getParam());
    }


    @PostMapping(SupportRequestPath.OS_GENERATE_PRE_SIGNED_URL)
    @WebAspect(injectReqId = true, logDesc = "生成可预览的外链")
    public BaseResponse<List<PreSignedUrlVO>> generatePreSignedUrl(@Validated @RequestBody BaseRequest<PreSignedUrlBO> request) {
        PreSignedUrlBO param = request.getParam();
        List<PreSignedUrlVO> urls = cloudStorage.generatePreSignedUrl(param.getStorageIds(), param.getKey(), param.getExpireDate());
        return BaseResponse.success(urls);
    }
}
