package com.m7.abs.admin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.m7.abs.common.domain.entity.DispatcherFailLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @program: abs
 * @description: 推送失败记录管理
 * @author: yx
 * @create: 2021-12-30 14:18
 */
@Mapper
@Repository
@DS("abs_business")
public interface DispatcherFailLogMapper extends BaseMapper<DispatcherFailLogEntity> {
}
