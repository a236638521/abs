package com.m7.abs.admin.sys.service.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.admin.sys.mapper.SysDeptMapper;
import com.m7.abs.admin.sys.service.ISysDeptService;
import com.m7.abs.common.domain.entity.SysDeptEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDeptEntity> implements ISysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;


    @Override
    public List<SysDeptEntity> findTree() {
        List<SysDeptEntity> sysDepts = new ArrayList<>();
        List<SysDeptEntity> depts = sysDeptMapper.selectList(Wrappers.emptyWrapper());
        for (SysDeptEntity dept : depts) {
            if (StringUtils.isEmpty(dept.getParentId())) {
                dept.setLevel(0);
                sysDepts.add(dept);
            }
        }
        findChildren(sysDepts, depts);
        return sysDepts;
    }

    private void findChildren(List<SysDeptEntity> sysDepts, List<SysDeptEntity> depts) {
        for (SysDeptEntity sysDept : sysDepts) {
            List<SysDeptEntity> children = new ArrayList<>();
            for (SysDeptEntity dept : depts) {
                if (sysDept.getId() != null && sysDept.getId().equals(dept.getParentId())) {
                    dept.setParentName(sysDept.getName());
                    dept.setLevel(sysDept.getLevel() + 1);
                    children.add(dept);
                }
            }
            sysDept.setChildren(children);
            findChildren(children, depts);
        }
    }

}
