package com.m7.abs.admin.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.admin.sys.mapper.SysLogMapper;
import com.m7.abs.admin.sys.service.ISysLogService;
import com.m7.abs.common.domain.entity.SysLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLogEntity> implements ISysLogService {

    @Autowired
    private SysLogMapper sysLogMapper;


}
