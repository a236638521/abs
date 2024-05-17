package com.m7.abs.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.domain.dto.FlashSmTemplatePageDTO;
import com.m7.abs.admin.mapper.FlashSmTemplateMapper;
import com.m7.abs.admin.service.IFlashSmTemplateService;
import com.m7.abs.common.domain.entity.FlashSmTemplateEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
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
@Slf4j
@Service
public class FlashSmTemplateServiceImpl extends ServiceImpl<FlashSmTemplateMapper, FlashSmTemplateEntity> implements IFlashSmTemplateService {
    @Autowired
    private FlashSmTemplateMapper flashSmTemplateMapper;

    @Override
    public boolean saveOrUpdateFlashSmTemplate(FlashSmTemplateEntity record) {
        String id = record.getId();
        if (StringUtils.isEmpty(id)) {//新增
            record.setTemplateNumber(getTemplateNumber());
        }
        return this.saveOrUpdate(record);
    }

    @Override
    public IPage<FlashSmTemplatePageDTO> findFlashSmTemplateByPage(PageBean page, QueryWrapper queryWrapper) {
        return flashSmTemplateMapper.findFlashSmTemplateByPage(page, queryWrapper);
    }

    private String getTemplateNumber() {
        String templateNumber = "FT_" + RandomStringUtils.randomAlphanumeric(15);
        LambdaQueryWrapper<FlashSmTemplateEntity> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(FlashSmTemplateEntity::getTemplateNumber, templateNumber);
        lambdaQueryWrapper.last("LIMIT 1");
        FlashSmTemplateEntity smsTemplateEntity = flashSmTemplateMapper.selectOne(lambdaQueryWrapper);
        if (smsTemplateEntity != null) {
            log.warn("have the same sms templateNum.retry");
            return getTemplateNumber();
        } else {
            return templateNumber;
        }
    }


}
