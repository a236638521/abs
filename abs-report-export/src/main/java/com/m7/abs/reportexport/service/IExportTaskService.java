package com.m7.abs.reportexport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.m7.abs.common.constant.common.ExportStatusEnum;
import com.m7.abs.common.domain.entity.ExportTaskEntity;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Kejie Peng
 * @since 2023-03-24
 */
public interface IExportTaskService extends IService<ExportTaskEntity> {
    /**
     * 更新任务状态
     * @param taskId
     * @param execution
     * @return
     */
    boolean updateTaskStatus(String taskId, ExportStatusEnum execution);

    /**
     * 更新预计下载量
     * @param taskId
     * @param expectCount
     */
    void updateExpectCount(String taskId, Integer expectCount);

}
