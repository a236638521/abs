package com.m7.abs.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.MiddleNumberBindLogEntity;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-21
 */
public interface IMiddleNumberBindLogService extends IService<MiddleNumberBindLogEntity> {

    /**
     * 获取未解绑的记录
     * @param mappingId
     * @return
     */
    BaseResponse<MiddleNumberBindLogEntity>  getUnbindLog(String mappingId);

    /**
     * 延长绑定时长,更新绑定记录
     * @param mappingId 记录ID
     * @param extraTime 延长时间,单位为秒
     */
    void delayExpiration(String mappingId, Integer extraTime);

    /**
     * 检查是否可复用小号
     * @param account
     * @param telA
     * @param telB
     * @return
     */
    MiddleNumberBindLogEntity checkReusable(String account, String telA, String telB);
}
