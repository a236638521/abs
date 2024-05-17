package com.m7.abs.reportexport.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.m7.abs.common.domain.entity.MiddleNumberCdrEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Kejie Peng
 * @since 2022-03-16
 */
@Mapper
@Repository
@DS("abs_business")
public interface MiddleNumberCdrMapper extends BaseMapper<MiddleNumberCdrEntity> {
    /**
     *
     * @return
     */
    List<MiddleNumberCdrEntity> streamByExample();

    /**
     *
     * @return
     */
    int streamByExampleCount();

}
