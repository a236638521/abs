package com.m7.abs.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.m7.abs.admin.domain.dto.MidNumReportDTO;
import com.m7.abs.admin.domain.vo.report.ReportSearchVO;
import com.m7.abs.common.domain.entity.MiddleNumberCdrReportEntity;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Kejie Peng
 * @since 2023-04-03
 */
public interface IMiddleNumberCdrReportService extends IService<MiddleNumberCdrReportEntity> {
    /**
     * 查询报表
     * @param searchVO
     * @return
     */
    List<MidNumReportDTO> search(ReportSearchVO searchVO);
}
