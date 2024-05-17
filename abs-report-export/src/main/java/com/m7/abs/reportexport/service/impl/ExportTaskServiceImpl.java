package com.m7.abs.reportexport.service.impl;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.common.constant.common.ExportStatusEnum;
import com.m7.abs.common.domain.entity.ExportTaskEntity;
import com.m7.abs.reportexport.mapper.ExportTaskMapper;
import com.m7.abs.reportexport.service.IExportTaskService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Kejie Peng
 * @since 2023-03-24
 */
@Service
public class ExportTaskServiceImpl extends ServiceImpl<ExportTaskMapper, ExportTaskEntity> implements IExportTaskService {

    @Override
    public boolean updateTaskStatus(String taskId, ExportStatusEnum execution) {
        LambdaUpdateWrapper<ExportTaskEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ExportTaskEntity::getId, taskId);
        updateWrapper.set(ExportTaskEntity::getStatus, execution.getCode());
        return this.update(updateWrapper);
    }

    @Override
    public void updateExpectCount(String taskId, Integer expectCount) {
        LambdaUpdateWrapper<ExportTaskEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ExportTaskEntity::getId, taskId);
        updateWrapper.set(ExportTaskEntity::getExpectCount, expectCount);
        this.update(updateWrapper);
    }
}
