package com.m7.abs.admin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.m7.abs.admin.domain.dto.MidNumReportDTO;
import com.m7.abs.admin.domain.vo.report.ReportSearchVO;
import com.m7.abs.common.domain.entity.MiddleNumberCdrReportEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Kejie Peng
 * @since 2023-04-03
 */
@Mapper
@Repository
@DS("abs_business")
public interface MiddleNumberCdrReportMapper extends BaseMapper<MiddleNumberCdrReportEntity> {
    /**
     * 按时统计
     * @param wrapper
     * @return
     */
    List<MidNumReportDTO> selectReportByHour(@Param(Constants.WRAPPER) Wrapper wrapper);
    /**
     * 按天统计
     * @param wrapper
     * @return
     */
    List<MidNumReportDTO> selectReportByDay(@Param(Constants.WRAPPER) Wrapper wrapper);
    /**
     * 按月统计
     * @param wrapper
     * @return
     */
    List<MidNumReportDTO> selectReportByMonth(@Param(Constants.WRAPPER) Wrapper wrapper);
}
