package com.m7.abs.admin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.m7.abs.admin.domain.dto.FlashSmReportDTO;
import com.m7.abs.common.domain.entity.FlashSmCdrReportEntity;
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
 * @since 2023-04-10
 */
@Mapper
@Repository
@DS("abs_business")
public interface FlashSmCdrReportMapper extends BaseMapper<FlashSmCdrReportEntity> {

    /**
     * 按时统计
     *
     * @param wrapper
     * @return
     */
    List<FlashSmReportDTO> selectReportByHour(@Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 按天统计
     *
     * @param wrapper
     * @return
     */
    List<FlashSmReportDTO> selectReportByDay(@Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 按月统计
     *
     * @param wrapper
     * @return
     */
    List<FlashSmReportDTO> selectReportByMonth(@Param(Constants.WRAPPER) Wrapper wrapper);
}
