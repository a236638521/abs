package com.m7.abs.admin.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.m7.abs.common.domain.entity.SysDeptEntity;

import java.util.List;

/**
 * 机构管理
 *
 * @author Louis
 * @date Oct 29, 2018
 */
public interface ISysDeptService extends IService<SysDeptEntity> {

    /**
     * 查询机构树
     *
     * @return
     */
    List<SysDeptEntity> findTree();
}
