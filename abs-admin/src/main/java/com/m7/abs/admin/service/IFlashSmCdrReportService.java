package com.m7.abs.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.m7.abs.admin.domain.dto.FlashSmReportDTO;
import com.m7.abs.admin.domain.vo.report.ReportSearchVO;
import com.m7.abs.common.domain.entity.FlashSmCdrReportEntity;
import com.m7.abs.common.domain.entity.MiddleNumberCdrReportEntity;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Kejie Peng
 * @since 2023-04-10
 */
public interface IFlashSmCdrReportService extends IService<FlashSmCdrReportEntity> {

    /**
     * 查询报表
     * @param searchVO
     * @return
     */
    List<FlashSmReportDTO> search(ReportSearchVO searchVO);
}
