package com.m7.abs.reportexport.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.m7.abs.common.domain.entity.MiddleNumberCdrReportEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Kejie Peng
 * @since 2023-04-03
 */
 @Mapper
 @Repository
 @DS("abs_business")
public interface MiddleNumberCdrReportMapper extends BaseMapper<MiddleNumberCdrReportEntity> {

}
