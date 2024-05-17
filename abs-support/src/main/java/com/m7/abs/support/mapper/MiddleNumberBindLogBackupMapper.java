package com.m7.abs.support.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.m7.abs.common.domain.entity.MiddleNumberBindLogBackupEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-21
 */
@Mapper
@Repository
public interface MiddleNumberBindLogBackupMapper extends BaseMapper<MiddleNumberBindLogBackupEntity> {
}
