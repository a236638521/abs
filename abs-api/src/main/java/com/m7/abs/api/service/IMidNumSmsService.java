package com.m7.abs.api.service;

import java.util.Map;

/**
 * @author kejie peng
 */
public interface IMidNumSmsService {
    /**
     * 处理短信话单
     * @param channelCode
     * @param requestVO
     * @return
     */
    Object record(String channelCode, Map<String, Object> requestVO);
}
