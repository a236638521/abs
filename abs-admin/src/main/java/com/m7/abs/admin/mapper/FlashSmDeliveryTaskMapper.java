package com.m7.abs.admin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.m7.abs.common.domain.entity.FlashSmDeliveryTaskEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Kejie Peng
 * @since 2022-06-06
 */
@Mapper
@Repository
@DS("abs_business")
public interface FlashSmDeliveryTaskMapper extends BaseMapper<FlashSmDeliveryTaskEntity> {

}
