package com.m7.abs.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.domain.dto.MiddleNumberCdrDTO;
import com.m7.abs.common.domain.entity.MiddleNumberCdrEntity;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Kejie Peng
 * @since 2022-03-16
 */
public interface IMiddleNumberCdrService extends IService<MiddleNumberCdrEntity> {
    /**
     * 导出数据
     * @param page
     * @param response
     */
    void exportExcel(PageBean page, HttpServletResponse response);

    /**
     * 分页查询小号
     * @param page
     * @return
     */
    IPage<MiddleNumberCdrDTO> findMidNumByPage(PageBean page);
}
