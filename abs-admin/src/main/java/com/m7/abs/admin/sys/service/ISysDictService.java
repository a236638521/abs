package com.m7.abs.admin.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.m7.abs.common.domain.entity.SysDictEntity;

import java.util.List;

/**
 * 字典管理
 *
 * @author Louis
 * @date Oct 29, 2018
 */
public interface ISysDictService extends IService<SysDictEntity> {

    /**
     * 根据名称查询
     *
     * @param lable
     * @return
     */
    List<SysDictEntity> findByLabel(String lable);
}
