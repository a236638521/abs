package com.m7.abs.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.domain.dto.MiddleNumberPoolPageDTO;
import com.m7.abs.admin.mapper.MiddleNumberPoolMapper;
import com.m7.abs.admin.service.IMiddleNumberPoolService;
import com.m7.abs.common.domain.entity.MiddleNumberPoolEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 小号池 服务实现类
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-14
 */
@Service
public class MiddleNumberPoolServiceImpl extends ServiceImpl<MiddleNumberPoolMapper, MiddleNumberPoolEntity> implements IMiddleNumberPoolService {
    @Autowired
    private MiddleNumberPoolMapper middleNumberPoolMapper;

    @Override
    public IPage<MiddleNumberPoolPageDTO> findByPage(PageBean page, QueryWrapper queryWrapper) {
        return middleNumberPoolMapper.findByPage(page, page.getSqlConditionList());
    }
}
