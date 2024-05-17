package com.m7.abs.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.m7.abs.common.domain.entity.FlashSmTemplateEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Kejie Peng
 * @since 2022-05-31
 */
@Mapper
@Repository
public interface FlashSmTemplateMapper extends BaseMapper<FlashSmTemplateEntity> {

    FlashSmTemplateEntity getFlashSmTemplateForDeliver(String accountId, String templateNum);
}
