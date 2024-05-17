package com.m7.abs.reportexport.domain.dto;

import com.m7.abs.common.constant.keys.RedisKeyConstant;
import com.m7.abs.common.utils.DateUtil;
import com.m7.abs.reportexport.common.properties.MidNumReportFields;
import com.m7.abs.reportexport.common.redisson.KeyStrValueLongCodec;
import com.m7.abs.reportexport.common.utils.RedissonUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.util.Date;
import java.util.Map;

/**
 * 小号报表
 *
 * @author Kejie Peng
 * @date 2023年 03月29日 16:15:28
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Slf4j
public class MidNumReportDTO {

    /**
     * 呼叫总数每半小时监控
     */
    private RMap<String, Long> callCountMonitor;

    /**
     * 呼叫成功数据每半小时监控
     */
    private RMap<String, Long> callCompleteMonitor;

    /**
     * 呼叫失败数据每半小时监控
     */
    private RMap<String, Long> callFailMonitor;

    /**
     * 主叫运营商统计
     */
    private RMap<String, Long> callerCarrierMonitor;
    /**
     * 被叫运营商统计
     */
    private RMap<String, Long> calledCarrierMonitor;
    /**
     * 通话时长，实际通话时长，（单位秒）
     */
    private RMap<String, Long> billDurationMonitor;

    /**
     * 计费通话时长，计费字段：60进1（单位：分钟）
     */
    private RMap<String, Long> rateDurationMonitor;

    public MidNumReportDTO(RedissonClient redissonClient, String accountId, String date) {
        /**
         * 缓存2天的数据
         */
        Date expireDate = DateUtil.parseStrToDate(date, DateUtil.DATE_FORMAT_YYYY_MM_DD);
        expireDate = DateUtil.addDate(expireDate, 0, 0, 2, 0, 0, 0, 0);
        log.info("new mid num report dto,expire date:{}", DateUtil.parseDateToStr(expireDate, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));
        callCountMonitor = RedissonUtil.getWriteThroughMap(redissonClient, KeyStrValueLongCodec.INSTANCE, RedisKeyConstant.MidNum.getReportKey(MidNumReportFields.CALL_COUNT + "-" + date, accountId));
        callCountMonitor.expireAt(expireDate);
        callCompleteMonitor = RedissonUtil.getWriteThroughMap(redissonClient, KeyStrValueLongCodec.INSTANCE, RedisKeyConstant.MidNum.getReportKey(MidNumReportFields.CALL_COMPLETE_COUNT + "-" + date, accountId));
        callCompleteMonitor.expireAt(expireDate);
        callFailMonitor = RedissonUtil.getWriteThroughMap(redissonClient, KeyStrValueLongCodec.INSTANCE, RedisKeyConstant.MidNum.getReportKey(MidNumReportFields.CALL_FAIL_COUNT + "-" + date, accountId));
        callFailMonitor.expireAt(expireDate);
        callerCarrierMonitor = RedissonUtil.getWriteThroughMap(redissonClient, KeyStrValueLongCodec.INSTANCE, RedisKeyConstant.MidNum.getReportKey(MidNumReportFields.CALLER_CARRIER_COUNT + "-" + date, accountId));
        callerCarrierMonitor.expireAt(expireDate);
        calledCarrierMonitor = RedissonUtil.getWriteThroughMap(redissonClient, KeyStrValueLongCodec.INSTANCE, RedisKeyConstant.MidNum.getReportKey(MidNumReportFields.CALLED_CARRIER_COUNT + "-" + date, accountId));
        calledCarrierMonitor.expireAt(expireDate);
        billDurationMonitor = RedissonUtil.getWriteThroughMap(redissonClient, KeyStrValueLongCodec.INSTANCE, RedisKeyConstant.MidNum.getReportKey(MidNumReportFields.BILL_DURATION_COUNT + "-" + date, accountId));
        billDurationMonitor.expireAt(expireDate);
        rateDurationMonitor = RedissonUtil.getWriteThroughMap(redissonClient, KeyStrValueLongCodec.INSTANCE, RedisKeyConstant.MidNum.getReportKey(MidNumReportFields.RATE_DURATION_COUNT + "-" + date, accountId));
        rateDurationMonitor.expireAt(expireDate);
    }

    public void addCallCount(String key) {
        callCountMonitor.addAndGet(key, 1L);
    }

    public void addCallComplete(String key) {
        callCompleteMonitor.addAndGet(key, 1L);
    }

    public void addCallFail(String key) {
        callFailMonitor.addAndGet(key, 1L);
    }

    public void addCallerCarrier(String key) {
        callerCarrierMonitor.addAndGet(key, 1L);
    }

    public void addCalledCarrier(String key) {
        calledCarrierMonitor.addAndGet(key, 1L);
    }

    public void addBillDuration(String key, long number) {
        billDurationMonitor.addAndGet(key, number);
    }

    public void addRateDuration(String key, long number) {
        rateDurationMonitor.addAndGet(key, number);
    }

    public Map<String, Long> getAllCallCount() {
        return callCountMonitor.readAllMap();
    }

    public Map<String, Long> getAllCallComplete() {
        return callCompleteMonitor.readAllMap();
    }

    public Map<String, Long> getAllCallFail() {
        return callFailMonitor.readAllMap();
    }

    public Map<String, Long> getAllCallerCarrier() {
        return callerCarrierMonitor.readAllMap();
    }

    public Map<String, Long> getAllCalledCarrier() {
        return calledCarrierMonitor.readAllMap();
    }

    public Map<String, Long> getAllBillDuration() {
        return billDurationMonitor.readAllMap();
    }

    public Map<String, Long> getAllRateDuration() {
        return rateDurationMonitor.readAllMap();
    }

    public String getKeyName(Date date) {
        Integer hour = DateUtil.getHour(date);
        Integer minute = DateUtil.getMinute(date);
        return (hour <= 9 ? "0" + hour : hour) + (minute >= 30 ? ":30" : ":00");
    }

}
