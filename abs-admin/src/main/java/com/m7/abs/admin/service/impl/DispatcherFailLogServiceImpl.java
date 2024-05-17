package com.m7.abs.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.admin.feignClient.support.PushDataClient;
import com.m7.abs.admin.mapper.DispatcherFailLogMapper;
import com.m7.abs.admin.service.IDispatcherFailLogService;
import com.m7.abs.common.domain.base.BaseRequest;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.bo.support.RetryPushDataBO;
import com.m7.abs.common.domain.entity.DispatcherFailLogEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 推送失败记录管理
 * @Author: yx
 * @date: 2021/12/30
 */
@Service
@Slf4j
public class DispatcherFailLogServiceImpl extends ServiceImpl<DispatcherFailLogMapper, DispatcherFailLogEntity> implements IDispatcherFailLogService {
    @Autowired
    private PushDataClient pushDataClient;

    @Override
    public BaseResponse heavyPush(BaseRequest<List<RetryPushDataBO>> request) {
        return pushDataClient.retryPush(request);
    }
}
