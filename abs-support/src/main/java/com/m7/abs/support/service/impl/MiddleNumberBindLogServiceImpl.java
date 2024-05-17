package com.m7.abs.support.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.common.constant.common.BindStatusEnum;
import com.m7.abs.common.domain.entity.MiddleNumberBindLogBackupEntity;
import com.m7.abs.common.domain.entity.MiddleNumberBindLogEntity;
import com.m7.abs.common.utils.BeanUtil;
import com.m7.abs.common.utils.DateUtil;
import com.m7.abs.common.utils.FastJsonUtils;
import com.m7.abs.support.common.constant.AbsSupportProperties;
import com.m7.abs.support.mapper.MiddleNumberBindLogBackupMapper;
import com.m7.abs.support.mapper.MiddleNumberBindLogMapper;
import com.m7.abs.support.service.IMiddleNumberBindLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-21
 */
@Slf4j
@Service
public class MiddleNumberBindLogServiceImpl extends ServiceImpl<MiddleNumberBindLogMapper, MiddleNumberBindLogEntity> implements IMiddleNumberBindLogService {
    @Autowired
    private MiddleNumberBindLogMapper middleNumberBindLogMapper;
    @Autowired
    private AbsSupportProperties absSupportProperties;
    @Autowired
    private MiddleNumberBindLogBackupMapper middleNumberBindLogBackupMapper;

    private final static int LIMIT = 1000;


    @Override
    public boolean updateExpiredLogsStatus() {
        List<MiddleNumberBindLogEntity> expiredLogs = middleNumberBindLogMapper.getExpiredLogs(LIMIT);

        List<MiddleNumberBindLogEntity> needUpdateLogs = new ArrayList<>();
        if (expiredLogs != null && expiredLogs.size() > 0) {
            for (int i = 0; i < expiredLogs.size(); i++) {
                MiddleNumberBindLogEntity bindLogEntity = expiredLogs.get(i);
                Long expiration = bindLogEntity.getExpiration();
                Date createTime = bindLogEntity.getCreateTime();
                Date expireTime = bindLogEntity.getExpireTime();
                if (createTime != null && expiration != null) {//检测绑定关系是否过期
                    long distanceTime = DateUtil.getDistanceTimeBySec(createTime, new Date());
                    if (distanceTime > expiration) {
                        log.info("[" + bindLogEntity.getId() + "] bind log expired,createTime:" + createTime + " expireTime:" + expireTime + " expiration:" + expiration + " distanceTime:" + distanceTime);
                        bindLogEntity.setStatus(BindStatusEnum.EXPIRED.getType());
                        bindLogEntity.setLastUpdateTime(new Date());
                        needUpdateLogs.add(bindLogEntity);
                    }
                }
            }
        }
        if (needUpdateLogs != null && needUpdateLogs.size() > 0) {
            boolean b = this.updateBatchById(needUpdateLogs);
            if (b) {
                log.info("update success,count:" + needUpdateLogs.size());
                return true;
            }
        }
        return false;
    }

    @Override
    public void removeInvalidLogs() {
        int timeLimit = absSupportProperties.getMidNumTask().getRetentionPeriod();
        List<MiddleNumberBindLogEntity> invalidLogs = middleNumberBindLogMapper.getInvalidLogs(timeLimit, LIMIT);

        List<String> needRemoveLogs = new ArrayList<>();
        if (invalidLogs != null && invalidLogs.size() > 0) {
            log.info("find need clear logs,count:" + invalidLogs.size());
            for (int i = 0; i < invalidLogs.size(); i++) {
                boolean removed = false;
                MiddleNumberBindLogEntity bindLogEntity = invalidLogs.get(i);
                Date lastUpdateTime = bindLogEntity.getLastUpdateTime();
                String status = bindLogEntity.getStatus();
                long distanceTime = 0L;
                if (lastUpdateTime != null) {
                    distanceTime = DateUtil.getDistanceTimeBySec(lastUpdateTime, new Date());
                }
                if (StringUtils.isNotEmpty(status) && (BindStatusEnum.UNBIND.getType().equals(status) || BindStatusEnum.EXPIRED.getType().equals(status))) {
                    //允许5秒误差
                    if (distanceTime > timeLimit - 5) {
                        /**
                         * TODO 备份数据
                         */
                        MiddleNumberBindLogBackupEntity backupEntity = new MiddleNumberBindLogBackupEntity();
                        BeanUtil.setVOToVO(bindLogEntity, backupEntity);
                        backupEntity.setBackupTime(new Date());
                        try {
                            int insert = middleNumberBindLogBackupMapper.insert(backupEntity);
                            if (insert >= 1) {
                                /**
                                 * 删除数据
                                 */
                                log.info("[" + bindLogEntity.getId() + "] remove logs,status:" + status + " lastUpdateTime:" + lastUpdateTime + " distanceTime:" + distanceTime);
                                needRemoveLogs.add(bindLogEntity.getId());
                                removed = true;
                            }
                        } catch (Exception e) {
                            log.error("backup mid num log fail,{}",FastJsonUtils.toJSONString(bindLogEntity), e);
                        }

                    }
                }

                if (!removed) {
                    log.info("[" + bindLogEntity.getId() + "] ignore logs,status:" + status + " lastUpdateTime:" + lastUpdateTime + " distanceTime:" + distanceTime);
                }
            }
        }

        if (needRemoveLogs != null) {
            try {
                boolean b = this.removeByIds(needRemoveLogs);
                if (b) {
                    log.info("remove success,count:" + needRemoveLogs.size());
                }
            } catch (Exception e) {
                log.error("remove log fail,{}", FastJsonUtils.toJSONString(needRemoveLogs), e);
            }
        }

        if (invalidLogs != null && needRemoveLogs.size() == LIMIT) {
            log.info("handle size eq " + LIMIT + " , RETRY.");
            this.removeInvalidLogs();
        }
    }

    @Override
    public boolean updateBatchById(List<MiddleNumberBindLogEntity> needUpdateLogs) {
        return middleNumberBindLogMapper.updateBatchById(needUpdateLogs);
    }
}
