package com.m7.abs.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.m7.abs.admin.domain.vo.export.ExportTaskVO;
import com.m7.abs.common.domain.base.BaseResponse;
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
     * 创建导出任务
     * @param record
     * @return
     */
    BaseResponse createExportTask(ExportTaskVO record);
}
