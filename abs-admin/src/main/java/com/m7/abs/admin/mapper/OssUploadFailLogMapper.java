package com.m7.abs.admin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.m7.abs.common.domain.entity.OssUploadFailLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Description: 录音转存失败记录管理
 * @Author: yx
 * @date: 2021/12/30
 */
@Mapper
@Repository
@DS("abs_business")
public interface OssUploadFailLogMapper extends BaseMapper<OssUploadFailLogEntity> {
}
