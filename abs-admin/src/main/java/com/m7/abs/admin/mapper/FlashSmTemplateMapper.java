package com.m7.abs.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.domain.dto.FlashSmTemplatePageDTO;
import com.m7.abs.common.domain.entity.FlashSmTemplateEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    IPage<FlashSmTemplatePageDTO> findFlashSmTemplateByPage(PageBean page, @Param(Constants.WRAPPER) QueryWrapper queryWrapper);
}
