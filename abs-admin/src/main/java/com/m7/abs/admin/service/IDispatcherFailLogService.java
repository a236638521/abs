package com.m7.abs.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.m7.abs.common.domain.base.BaseRequest;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.bo.support.RetryPushDataBO;
import com.m7.abs.common.domain.entity.DispatcherFailLogEntity;

import java.util.List;

public interface IDispatcherFailLogService extends IService<DispatcherFailLogEntity> {

    BaseResponse heavyPush(BaseRequest<List<RetryPushDataBO>> request);
}
