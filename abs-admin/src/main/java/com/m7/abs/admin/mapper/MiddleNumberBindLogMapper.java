package com.m7.abs.admin.mapper;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.m7.abs.common.domain.entity.MiddleNumberBindLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Description: 小号池绑定管理
 * @Author: yx
 * @date: 2021/12/23
 */
@Mapper
@Repository
@DS("abs_business")
public interface MiddleNumberBindLogMapper extends BaseMapper<MiddleNumberBindLogEntity> {
}
