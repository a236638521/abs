package com.m7.abs.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.api.mapper.MiddleNumberBindLogMapper;
import com.m7.abs.api.service.IMiddleNumberBindLogService;
import com.m7.abs.common.constant.common.BindStatusEnum;
import com.m7.abs.common.constant.common.BindTypeEnum;
import com.m7.abs.common.constant.error.code.ErrorCodeConstant;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.MiddleNumberBindLogEntity;
import com.m7.abs.common.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-21
 */
@Service
public class MiddleNumberBindLogServiceImpl extends ServiceImpl<MiddleNumberBindLogMapper, MiddleNumberBindLogEntity> implements IMiddleNumberBindLogService {
    @Autowired
    private MiddleNumberBindLogMapper middleNumberBindLogMapper;

    @Override
    public BaseResponse<MiddleNumberBindLogEntity> getUnbindLog(String mappingId) {
        LambdaQueryWrapper<MiddleNumberBindLogEntity> bindLogWrapper = new LambdaQueryWrapper<>();
        bindLogWrapper.eq(MiddleNumberBindLogEntity::getId, mappingId);
        bindLogWrapper.ne(MiddleNumberBindLogEntity::getStatus, BindStatusEnum.UNBIND.getType());
        bindLogWrapper.last("limit 1");
        MiddleNumberBindLogEntity bindInfo = this.getOne(bindLogWrapper);
        if (bindInfo != null) {
            Long expiration = bindInfo.getExpiration();
            Date createTime = bindInfo.getCreateTime();
            if (createTime != null && expiration != null) {//检测绑定关系是否过期
                long distanceTime = DateUtil.getDistanceTimeBySec(createTime, new Date());
                if (distanceTime > expiration) {
                    return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.BIND_EXPIRED);
                }
            }
            return BaseResponse.success(bindInfo);
        } else {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.BIND_NOT_FOUND);
        }
    }

    @Override
    public void delayExpiration(String mappingId, Integer extraTime) {
        LambdaQueryWrapper<MiddleNumberBindLogEntity> bindLogWrapper = new LambdaQueryWrapper<>();
        bindLogWrapper.eq(MiddleNumberBindLogEntity::getId, mappingId);
        bindLogWrapper.last("limit 1");
        MiddleNumberBindLogEntity bindInfo = this.getOne(bindLogWrapper);
        if (bindInfo != null) {
            Long expiration = bindInfo.getExpiration();
            if (expiration != null) {
                expiration += extraTime;
            }
            bindInfo.setExpiration(expiration);
            this.updateById(bindInfo);
        }
    }

    /**
     * @param account
     * @param telA
     * @param telB
     */
    @Override
    public MiddleNumberBindLogEntity checkReusable(String account, String telA, String telB) {
        Date date = new Date();
        LambdaQueryWrapper<MiddleNumberBindLogEntity> bindLogWrapper = new LambdaQueryWrapper<>();
        bindLogWrapper.eq(MiddleNumberBindLogEntity::getBindType, BindTypeEnum.AXB.getType());
        bindLogWrapper.eq(MiddleNumberBindLogEntity::getStatus, BindStatusEnum.BINDING.getType());
        bindLogWrapper.and(orWrapper->{
            orWrapper.eq(MiddleNumberBindLogEntity::getAccountId, account);
            orWrapper.or();
            orWrapper.eq(MiddleNumberBindLogEntity::getBillAccountId, account);
        });
        bindLogWrapper.eq(MiddleNumberBindLogEntity::getTelA, telA);
        bindLogWrapper.eq(MiddleNumberBindLogEntity::getTelB, telB);
        bindLogWrapper.gt(MiddleNumberBindLogEntity::getExpireTime, date);
        bindLogWrapper.between(MiddleNumberBindLogEntity::getCreateTime, DateUtil.getDayBeginTime(date), date);
        bindLogWrapper.orderByDesc(MiddleNumberBindLogEntity::getCreateTime);
        bindLogWrapper.last("limit 1");
        MiddleNumberBindLogEntity bindInfo = this.getOne(bindLogWrapper);
        return bindInfo;
    }
}
