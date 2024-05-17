package com.m7.abs.common.constant.keys;


/**
 * Redis keys
 * @author Kejie Peng
 */
public class RedisKeys {
    public static final String LOCK_MID_UPDATE_EXPIRED_LOGS = "lock-mid-updateExpiredLogs";
    public static final String LOCK_MID_REMOVE_LOGS = "lock-mid-removeLogs";
    /**
     * 小号报表
     */
    public static final String LOCK_MID_NUM_REPORT = "lock-mid-num-report";
    public static final String LOCK_MID_NUM_REPORT_SUM_TASK = "lock-mid-num-report-sum-task";
    public static final String LOCK_MID_NUM_REPORT_DAY_SUM_TASK = "lock-mid-num-report-day-sum-task";
    /**
     * 闪信报表
     */
    public static final String LOCK_FLASH_SM_REPORT = "lock-flash-sm-report";
    public static final String LOCK_FLASH_SM_REPORT_SUM_TASK = "lock-flash-sm-report-sum-task";
    public static final String LOCK_FLASH_SM_REPORT_DAY_SUM_TASK = "lock-flash-sm-report-day-sum-task";
}
