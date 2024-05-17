package com.m7.abs.api.controller;

import com.m7.abs.api.common.annotation.Authenticate;
import com.m7.abs.api.domain.vo.midNum.*;
import com.m7.abs.api.service.IMidNumService;
import com.m7.abs.api.service.ISupportService;
import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.utils.FastJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * 小号绑定相关
 *
 * @author hulin
 */
@Slf4j
@RestController
@RequestMapping("/midNum")
public class MidNumController {
    @Autowired
    private IMidNumService midNumService;
    @Autowired
    private ISupportService supportService;


    /**
     * AXB绑定
     *
     * @param requestVO
     * @return
     */
    @Authenticate(permission = true, checkIp = true)
    @WebAspect(injectReqId = true, logDesc = "api->midNum->axbBind")
    @PostMapping(value = "/axb/bind")
    public BaseResponse axbBind(@RequestBody @Valid BindAXBRequestVO requestVO) {
        return midNumService.axbBind(requestVO);
    }

    /**
     * AX绑定
     * @param requestVO
     * @return
     */
    @Authenticate(permission = true, checkIp = true)
    @WebAspect(injectReqId = true, logDesc = "api->midNum->axBind")
    @PostMapping(value = "/ax/bind")
    public BaseResponse axBind(@RequestBody @Valid BindAXRequestVO requestVO) {
        return midNumService.axBind(requestVO);
    }

    /**
     * AXB解绑
     *
     * @param requestVO
     * @return
     */
    @Authenticate(permission = true, checkIp = true)
    @WebAspect(injectReqId = true, logDesc = "api->midNum->axbUnBind")
    @PostMapping(value = "/axb/unBind")
    public BaseResponse axbUnBind(@RequestBody @Valid UnBindRequestVO requestVO) {
        return midNumService.unBindAXB(requestVO);
    }

    /**
     * AX解绑
     * @param requestVO
     * @return
     */
    @Authenticate(permission = true, checkIp = true)
    @WebAspect(injectReqId = true, logDesc = "api->midNum->axUnBind")
    @PostMapping(value = "/ax/unBind")
    public BaseResponse axUnBind(@RequestBody @Valid UnBindRequestVO requestVO) {
        return midNumService.axUnBind(requestVO);
    }

    /**
     * AX延长绑定时间
     * @param requestVO
     * @return
     */
    @Authenticate(permission = true, checkIp = true)
    @WebAspect(injectReqId = true, logDesc = "api->midNum->axDelay")
    @PostMapping(value = "/ax/delay")
    public BaseResponse axDelay(@RequestBody @Valid DelayAxRequestVO requestVO) {
        return midNumService.axDelay(requestVO);
    }

    /**
     * AXB延长绑定时间
     *
     * @param requestVO
     * @return
     */
    @Authenticate(permission = true, checkIp = true)
    @WebAspect(injectReqId = true, logDesc = "api->midNum->axbDelay")
    @PostMapping(value = "/axb/delay")
    public BaseResponse axbDelay(@RequestBody @Valid DelayAXBRequestVO requestVO) {
        return midNumService.axbDelay(requestVO);
    }

    /**
     * 接收话单
     * 描述:为不同渠道提供的账单回调接口,用于接收通话结束后话单数据
     *
     * @param requestVO
     * @return
     */
    @WebAspect(injectReqId = true, logDesc = "api->midNum->record")
    @PostMapping(value = "/record/{channelCode}")
    public Object record(@PathVariable("channelCode") String channelCode, @RequestBody Map<String, Object> requestVO) {
        log.info("[" + channelCode + "] Request Body:" + FastJsonUtils.toJSONString(requestVO));
        return midNumService.record(channelCode, requestVO);
    }

    /**
     * 接收录音地址
     * 描述:部分通道录音地址是单独推送的,需要一个接口来接收录音地址,并修改话单
     *
     * @param channelCode
     * @param requestVO
     * @return
     */
    @WebAspect(injectReqId = true, logDesc = "api->midNum->recordUrl")
    @PostMapping(value = "/recordUrl/{channelCode}")
    public Object recordUrl(@PathVariable("channelCode") String channelCode, @RequestBody Map<String, Object> requestVO) {
        log.info("[" + channelCode + "] Request Body:" + FastJsonUtils.toJSONString(requestVO));
        return midNumService.recordUrl(channelCode, requestVO);
    }

    /**
     * 接收振铃话单数据
     * 兼容中移和多号话单中没有振铃数据问题
     *
     * @param channelCode
     * @param requestVO
     * @return
     */
    @WebAspect(injectReqId = true, logDesc = "api->midNum->getRingTime")
    @PostMapping(value = "/getRingTime/{channelCode}")
    public Object getRingTime(@PathVariable("channelCode") String channelCode, @RequestBody Map<String, Object> requestVO) {
        log.info("[" + channelCode + "] Request Body:" + FastJsonUtils.toJSONString(requestVO));
        return midNumService.getRingTime(channelCode, requestVO);
    }

    /**
     * 重试下载录音
     *
     * @return
     */
    @Authenticate(permission = true, checkIp = true)
    @WebAspect(injectReqId = true, logDesc = "api->midNum->pushRecord->retry")
    @PostMapping(value = "/pushRecord/retry")
    public BaseResponse pushRecordRetry(@Valid @RequestBody PushRecordRetryRequestVO requestVO) {
        return supportService.pushRecordRetry(requestVO);
    }
}
