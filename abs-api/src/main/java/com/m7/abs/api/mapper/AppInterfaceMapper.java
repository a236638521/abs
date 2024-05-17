package com.m7.abs.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.m7.abs.common.domain.entity.AppInterfaceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 对接接口列表 Mapper 接口
 * </p>
 *
 * @author Kejie Peng
 * @since 2022-02-28
 */
@Mapper
@Repository
public interface AppInterfaceMapper extends BaseMapper<AppInterfaceEntity> {
    String getAppInterFaceByAccessKey(String accessKey);
}
