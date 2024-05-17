package com.m7.abs.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.m7.abs.admin.domain.vo.enterprise.EnterpriseSaveOrUpdateVO;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.EnterpriseEntity;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Kejie Peng
 * @since 2022-02-28
 */
public interface IEnterpriseService extends IService<EnterpriseEntity> {

    BaseResponse enterpriseSaveOrUpdate(EnterpriseSaveOrUpdateVO record);
}
