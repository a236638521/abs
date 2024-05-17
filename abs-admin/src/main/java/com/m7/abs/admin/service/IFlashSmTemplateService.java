package com.m7.abs.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.domain.dto.FlashSmTemplatePageDTO;
import com.m7.abs.common.domain.entity.FlashSmTemplateEntity;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Kejie Peng
 * @since 2022-05-31
 */
public interface IFlashSmTemplateService extends IService<FlashSmTemplateEntity> {

    boolean saveOrUpdateFlashSmTemplate(FlashSmTemplateEntity record);

    IPage<FlashSmTemplatePageDTO> findFlashSmTemplateByPage(PageBean page, QueryWrapper queryWrapper);
}
