package com.m7.abs.support.controller;

import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.constant.requestPath.SupportRequestPath;
import com.m7.abs.common.domain.base.BaseRequest;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.bo.support.PushDataBO;
import com.m7.abs.common.domain.bo.support.RetryPushDataBO;
import com.m7.abs.common.domain.vo.support.PushDataVO;
import com.m7.abs.common.domain.vo.support.RetryPushDataVO;
import com.m7.abs.support.service.impl.PushDataBaseService;
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
public class PushDataController {
    private final PushDataBaseService pushDataService;

    public PushDataController(PushDataBaseService pushDataService) {
        this.pushDataService = pushDataService;
    }

    @PostMapping(SupportRequestPath.PUSH_DATA)
    @WebAspect(injectReqId = true, logDesc = "数据推送接口")
    public BaseResponse<PushDataVO> dataPush(@Validated @RequestBody BaseRequest<PushDataBO> dataBo) {
        return pushDataService.pushData(dataBo);
    }

    @PostMapping(SupportRequestPath.PUSH_DATA_RETRY)
    @WebAspect(injectReqId = true, logDesc = "数据推送重试接口")
    public BaseResponse<RetryPushDataVO> retryPush(@Validated @RequestBody BaseRequest<List<RetryPushDataBO>> bo) {
        if (CollectionUtils.isEmpty(bo.getParam())) {
            return BaseResponse.success();
        }
        return pushDataService.retrySubmitTask(bo.getParam());
    }
}
