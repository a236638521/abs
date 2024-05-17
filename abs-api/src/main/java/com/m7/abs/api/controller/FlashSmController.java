package com.m7.abs.api.controller;

import com.m7.abs.api.common.annotation.Authenticate;
import com.m7.abs.api.domain.vo.flashSm.DeliverRequestVO;
import com.m7.abs.api.service.IFlashSmService;
import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.utils.FastJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * 闪信
 */
@Slf4j
@RestController
@RequestMapping("/flashSm")
public class FlashSmController {
    @Autowired
    private IFlashSmService flashSmService;

    /**
     * 发送闪信
     * @param requestVO
     * @return
     */
    @Authenticate(permission = true, checkIp = true)
    @WebAspect(injectReqId = true, logDesc = "api->flashSm->deliver")
    @PostMapping(value = "/deliver")
    public BaseResponse deliver(@RequestBody @Valid DeliverRequestVO requestVO) {
        return flashSmService.deliver(requestVO);
    }

    /**
     * 接收闪信投递结果
     * @param channelCode
     * @param requestVO
     * @return
     */
    @WebAspect(injectReqId = true, logDesc = "api->flashSm->record")
    @PostMapping(value = "/record/receive/{channelCode}")
    public Object record(@PathVariable("channelCode") String channelCode, @RequestBody Map<String, Object> requestVO) {
        log.info("[" + channelCode + "] Request Body:" + FastJsonUtils.toJSONString(requestVO));
        return flashSmService.receiveRecord(channelCode, requestVO);
    }

}
