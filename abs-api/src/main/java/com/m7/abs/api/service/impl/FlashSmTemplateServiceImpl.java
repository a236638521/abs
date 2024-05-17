package com.m7.abs.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.api.mapper.FlashSmTemplateMapper;
import com.m7.abs.api.service.IFlashSmTemplateService;
import com.m7.abs.common.domain.entity.FlashSmTemplateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Kejie Peng
 * @since 2022-05-31
 */
@Service
public class FlashSmTemplateServiceImpl extends ServiceImpl<FlashSmTemplateMapper, FlashSmTemplateEntity> implements IFlashSmTemplateService {
    @Autowired
    private FlashSmTemplateMapper flashSmTemplateMapper;

    @Override
    public FlashSmTemplateEntity getFlashSmTemplateForDeliver(String accountId, String templateNum) {
        return flashSmTemplateMapper.getFlashSmTemplateForDeliver(accountId, templateNum);
    }
}
