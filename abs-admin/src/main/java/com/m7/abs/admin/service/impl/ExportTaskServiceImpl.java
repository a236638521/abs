package com.m7.abs.admin.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.admin.domain.vo.export.ExportTaskVO;
import com.m7.abs.admin.feignClient.export.ExportClient;
import com.m7.abs.admin.mapper.ExportTaskMapper;
import com.m7.abs.admin.service.IExportTaskService;
import com.m7.abs.common.constant.common.ExportStatusEnum;
import com.m7.abs.common.constant.common.ProjectExportType;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.base.StorageFileBox;
import com.m7.abs.common.domain.entity.ExportTaskEntity;
import com.m7.abs.common.domain.vo.reportexport.MidNumCdrExportRequestVO;
import com.m7.abs.common.domain.vo.reportexport.MidNumCdrExportResponseVO;
import com.m7.abs.common.utils.MyStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

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
    @Autowired
    private ExportClient exportClient;

    @Override
    @DS("abs_business")
    public BaseResponse createExportTask(ExportTaskVO taskVO) {
        List<String> beginTime = taskVO.getBeginTime();
        if (beginTime == null || beginTime.size() != 2) {
            return BaseResponse.fail("请输入时间范围");
        }
        String taskId = MyStringUtils.randomUUID();
        MidNumCdrExportRequestVO exportRVO = MidNumCdrExportRequestVO.builder()
                .taskId(taskId)
                .accountId(taskVO.getAccountId())
                .type(taskVO.getType())
                .startTime(beginTime.get(0))
                .endTime(beginTime.get(1))
                .build();

        ExportTaskEntity record = ExportTaskEntity.builder()
                .id(taskId)
                .accountId(taskVO.getAccountId())
                .remarks(taskVO.getRemarks())
                .projectCode(ProjectExportType.MID_NUM_CDR)
                .status(ExportStatusEnum.INITIAL.getCode())
                .build();

        this.save(record);
        BaseResponse<MidNumCdrExportResponseVO> exportResponse = null;
        try {
            exportResponse = exportClient.exportExcel(exportRVO);
            if (exportResponse.isSuccess()) {
                MidNumCdrExportResponseVO data = exportResponse.getData();
                if (data != null) {
                    List<StorageFileBox> storageConf = data.getStorageConf();
                    String fileHost = "";
                    if (storageConf != null && storageConf.size() > 0) {
                        StorageFileBox storageFileBox = storageConf.get(0);
                        fileHost = storageFileBox.getFileHost();
                    }

                    record.setDownloadUrl(fileHost + File.separator + data.getKey());
                    record.setExpectCount(data.getExpectCount());

                    return BaseResponse.success(this.updateById(record));
                }
            } else {
                this.removeById(record.getId());
            }
        } catch (Exception e) {
            this.removeById(record.getId());
        }

        return exportResponse;
    }
}
