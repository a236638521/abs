package com.m7.abs.admin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.domain.dto.MiddleNumberCdrDTO;
import com.m7.abs.common.domain.entity.MiddleNumberCdrEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
     * 分页查询
     *
     * @param page
     * @param queryWrapper
     * @return
     */
    IPage<MiddleNumberCdrDTO> findMidNumByPage(PageBean page, @Param(Constants.WRAPPER) QueryWrapper queryWrapper);
}
