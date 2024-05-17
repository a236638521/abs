package com.m7.abs.api.controller;

import com.m7.abs.api.service.IMidNumSmsService;
import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.utils.FastJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 小号短信相关接口
 *
 * @author kejie peng
 */
@Slf4j
@RestController
@RequestMapping("/midNum/sms")
public class MidNumSmsController {
    private IMidNumSmsService midNumSmsService;

    @Autowired
    public MidNumSmsController(IMidNumSmsService midNumSmsService) {
        this.midNumSmsService = midNumSmsService;
    }


    /**
     * 获取小号短息话单
     *
     * @param requestVO
     * @return
     */
    @WebAspect(injectReqId = true, logDesc = "api->midNum->sms->record")
    @PostMapping(value = "/record/{channelCode}")
    public Object axbBrecordind(@PathVariable("channelCode") String channelCode, @RequestBody Map<String, Object> requestVO) {
        log.info("[" + channelCode + "] Request Body:" + FastJsonUtils.toJSONString(requestVO));
        return midNumSmsService.record(channelCode, requestVO);
    }

}
