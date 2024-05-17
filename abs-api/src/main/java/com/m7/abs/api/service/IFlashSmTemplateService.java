package com.m7.abs.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
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

    FlashSmTemplateEntity getFlashSmTemplateForDeliver(String accountId, String templateNum);
}
