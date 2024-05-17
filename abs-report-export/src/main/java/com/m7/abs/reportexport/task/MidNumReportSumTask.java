package com.m7.abs.reportexport.task;

import com.m7.abs.common.constant.keys.CommonSessionKeys;
import com.m7.abs.common.constant.keys.RedisKeys;
import com.m7.abs.common.utils.DateUtil;
import com.m7.abs.common.utils.MyStringUtils;
import com.m7.abs.reportexport.common.properties.MidNumReportFields;
import com.m7.abs.common.constant.keys.RedisKeyConstant;
import com.m7.abs.reportexport.common.utils.LockUtils;
import com.m7.abs.reportexport.service.IMiddleNumberCdrReportService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RSetCache;
import org.redisson.api.RedissonClient;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

/**
 * 报表统计任务
 *
 * @author kejie peng
 */
@Slf4j
@Component
public class MidNumReportSumTask {
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private IMiddleNumberCdrReportService cdrReportService;
    @Resource
    private LockUtils lockUtils;

    /**
     * 执行规则:每10分钟执行一次
     * 任务内容:统计当前报表数据
     */
    @Scheduled(cron = "#{@absReportProperties.task.reportSumCron}")
    public void midNumReportSumTask() {
        String traceId = MyStringUtils.randomUUID();
        MDC.put(CommonSessionKeys.REQ_ID_KEY, traceId);
        log.info("Start mid num report sum task.");
        midNumReportSum(traceId, RedisKeys.LOCK_MID_NUM_REPORT_SUM_TASK, MidNumReportFields.CURRENT_NEED_REPORT_ACCOUNT, new Date());
    }


    /**
     * 执行规则:次日凌晨统计之前的数据
     * 任务内容:次日凌晨统计之前的数据
     */
    @Scheduled(cron = "#{@absReportProperties.task.reportDaySumCron}")
    public void midNumReportDaySumTask() {
        String traceId = MyStringUtils.randomUUID();
        MDC.put(CommonSessionKeys.REQ_ID_KEY, traceId);
        log.info("Start mid num day report sum task.");
        midNumReportSum(traceId, RedisKeys.LOCK_MID_NUM_REPORT_DAY_SUM_TASK, MidNumReportFields.NEXT_DAY_NEED_REPORT_ACCOUNT, DateUtil.addDate(new Date(), Calendar.DATE, -1));
        cdrReportService.clearReportRedissonCache();
    }

    /**
     * 小号报表统计
     *
     * @param lockKey
     * @param accountKey
     * @param date
     */
    private void midNumReportSum(String traceId, String lockKey, String accountKey, Date date) {
        try {
            boolean lock = lockUtils.lock(RedisKeys.LOCK_MID_NUM_REPORT, traceId);
            if (!lock) {
                log.info("not get lock, ignore it");
                return;
            }

            RSetCache<String> accountSet = redissonClient.getSetCache(RedisKeyConstant.MidNum.getReportKeyFormatNoAccount(accountKey));
            if (accountSet != null && accountSet.size() > 0) {
                Set<String> accounts = accountSet.readAll();
                accounts.stream().forEach(account -> {
                    cdrReportService.sumCurrentMidNumCdrReport(traceId, account, date);
                });
            }
            /**
             * 休眠60秒,60秒锁安全期
             */
            Thread.sleep(60000);
        } catch (Exception e) {
            log.error("get lock fail,lockKey:{}, cause:{} ", lockKey, e.getMessage(), e);
        } finally {
            MDC.remove(CommonSessionKeys.REQ_ID_KEY);
            lockUtils.unLock(RedisKeys.LOCK_MID_NUM_REPORT, traceId);
        }
    }


}
