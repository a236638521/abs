package com.m7.abs.support.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.m7.abs.common.domain.entity.MiddleNumberBindLogEntity;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-21
 */
public interface IMiddleNumberBindLogService extends IService<MiddleNumberBindLogEntity> {

    /**
     * 更新已经过期绑定记录的状态
     */
    boolean updateExpiredLogsStatus();

    /**
     * 清理已经失效的数据
     */
    void removeInvalidLogs();

    /**
     * 批量更新
     * @param needUpdateLogs
     * @return
     */
    boolean updateBatchById(List<MiddleNumberBindLogEntity> needUpdateLogs);
}
