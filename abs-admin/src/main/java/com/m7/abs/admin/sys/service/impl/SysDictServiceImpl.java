package com.m7.abs.admin.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.admin.sys.mapper.SysDictMapper;
import com.m7.abs.admin.sys.service.ISysDictService;
import com.m7.abs.common.domain.entity.SysDictEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDictEntity> implements ISysDictService {

    @Autowired
    private SysDictMapper sysDictMapper;


    @Override
    public List<SysDictEntity> findByLabel(String lable) {
        return sysDictMapper.findByLabel(lable);
    }

}
