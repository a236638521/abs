package com.m7.abs.support.task;

import com.m7.abs.common.constant.keys.CommonSessionKeys;
import com.m7.abs.common.constant.keys.RedisKeys;
import com.m7.abs.common.utils.MyStringUtils;
import com.m7.abs.support.common.util.LockUtils;
import com.m7.abs.support.service.IMiddleNumberBindLogService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 中间号绑定记录定时任务
 *
 * @author kejie peng
 */
@Slf4j
@Component
public class MiddleNumberBindLogTask {
    /**
     * 更新已过期绑定记录
     */
    private static final String UPDATE_EXPIRED_LOGS = "UPDATE_EXPIRED_LOGS";
    /**
     * 删除无效的数据
     */
    private static final String REMOVE_INVALID_LOGS = "REMOVE_INVALID_LOGS";
    @Resource
    private LockUtils lockUtils;
    @Autowired
    private IMiddleNumberBindLogService middleNumberBindLogService;

    /**
     * 执行规则:每分钟执行一次
     * 任务内容:更新已经失效的绑定记录状态,
     * 每次最多1000条数据
     */
    @Scheduled(cron = "#{@absSupportProperties.midNumTask.updateExpiredLogsCron}")
    public void updateExpiredLogs() {
        long startTime = System.currentTimeMillis();
        String taskId = MyStringUtils.randomUUID();
        MDC.put(CommonSessionKeys.REQ_ID_KEY, taskId);
        boolean success = false;

        try {
            boolean lock = lockUtils.lock(RedisKeys.LOCK_MID_UPDATE_EXPIRED_LOGS, taskId);
            if (!lock) {
                log.info("[UPDATE] not get lock, ignore it");
            } else {
                success = middleNumberBindLogService.updateExpiredLogsStatus();
            }
        } catch (Exception e) {
            log.error("[" + UPDATE_EXPIRED_LOGS + "] error.", e);
        } finally {
            if (success) {
                log.info("End task:" + UPDATE_EXPIRED_LOGS + ",cost:[" + (System.currentTimeMillis() - startTime) + "]ms");
            }
            MDC.remove(CommonSessionKeys.REQ_ID_KEY);
            lockUtils.unLock(RedisKeys.LOCK_MID_UPDATE_EXPIRED_LOGS, taskId);
        }
    }


    /**
     * 每天凌晨3点执行,清理已经无效的数据
     * 数据内容:状态修改时间超过7天的无效绑定记录,避免绑定记录表数据堆积
     * 删除的状态:失效,解绑
     */
    @Scheduled(cron = "#{@absSupportProperties.midNumTask.removeLogsCron}")
    public void removeLogs() {
        long startTime = System.currentTimeMillis();
        String taskId = MyStringUtils.randomUUID();
        MDC.put(CommonSessionKeys.REQ_ID_KEY, taskId);
        log.info("Start task:" + REMOVE_INVALID_LOGS);

        try {
            boolean lock = lockUtils.lock(RedisKeys.LOCK_MID_REMOVE_LOGS, taskId);
            if (!lock) {
                log.info("[REMOVE] not get lock, ignore it");
            } else {
                middleNumberBindLogService.removeInvalidLogs();
            }
        } catch (Exception e) {
            log.error("[" + REMOVE_INVALID_LOGS + "] error.", e);
        } finally {
            log.info("End task:" + REMOVE_INVALID_LOGS + ",cost:[" + (System.currentTimeMillis() - startTime) + "]ms");
            MDC.remove(CommonSessionKeys.REQ_ID_KEY);
            lockUtils.unLock(RedisKeys.LOCK_MID_REMOVE_LOGS, taskId);
        }
    }
}
