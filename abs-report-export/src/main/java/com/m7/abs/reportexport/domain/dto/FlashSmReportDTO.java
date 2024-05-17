package com.m7.abs.reportexport.domain.dto;

import com.m7.abs.common.constant.keys.RedisKeyConstant;
import com.m7.abs.common.utils.DateUtil;
import com.m7.abs.reportexport.common.properties.FlashSmReportFields;
import com.m7.abs.reportexport.common.redisson.KeyStrValueLongCodec;
import com.m7.abs.reportexport.common.utils.RedissonUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.util.Date;
import java.util.Map;

/**
 * 闪信报表
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
public class FlashSmReportDTO {

    /**
     * 发送总数每半小时监控
     */
    private RMap<String, Long> totalCountMonitor;

    /**
     * 发送成功数据每半小时监控
     */
    private RMap<String, Long> completeMonitor;

    /**
     * 发送失败数据每半小时监控
     */
    private RMap<String, Long> failMonitor;

    /**
     * 成功接收运营商统计
     */
    private RMap<String, Long> carrierMonitor;


    public FlashSmReportDTO(RedissonClient redissonClient, String accountId, String date) {
        /**
         * 缓存2天的数据
         */
        Date expireDate = DateUtil.parseStrToDate(date, DateUtil.DATE_FORMAT_YYYY_MM_DD);
        expireDate = DateUtil.addDate(expireDate, 0, 0, 2, 0, 0, 0, 0);

        totalCountMonitor = RedissonUtil.getWriteThroughMap(redissonClient, KeyStrValueLongCodec.INSTANCE, RedisKeyConstant.FlashSms.getReportKey(FlashSmReportFields.TOTAL_COUNT + "-" + date, accountId));
        totalCountMonitor.expireAt(expireDate);
        completeMonitor = RedissonUtil.getWriteThroughMap(redissonClient, KeyStrValueLongCodec.INSTANCE, RedisKeyConstant.FlashSms.getReportKey(FlashSmReportFields.COMPLETE_COUNT + "-" + date, accountId));
        completeMonitor.expireAt(expireDate);
        failMonitor = RedissonUtil.getWriteThroughMap(redissonClient, KeyStrValueLongCodec.INSTANCE, RedisKeyConstant.FlashSms.getReportKey(FlashSmReportFields.FAIL_COUNT + "-" + date, accountId));
        failMonitor.expireAt(expireDate);
        carrierMonitor = RedissonUtil.getWriteThroughMap(redissonClient, KeyStrValueLongCodec.INSTANCE, RedisKeyConstant.FlashSms.getReportKey(FlashSmReportFields.CARRIER_COUNT + "-" + date, accountId));
        carrierMonitor.expireAt(expireDate);
    }

    public void addTotalCount(String key) {
        totalCountMonitor.addAndGet(key, 1L);
    }

    public void addComplete(String key) {
        completeMonitor.addAndGet(key, 1L);
    }

    public void addFail(String key) {
        failMonitor.addAndGet(key, 1L);
    }

    public void addCarrier(String key) {
        carrierMonitor.addAndGet(key, 1L);
    }

    public Map<String, Long> getAllTotalCount() {
        return totalCountMonitor.readAllMap();
    }

    public Map<String, Long> getAllComplete() {
        return completeMonitor.readAllMap();
    }

    public Map<String, Long> getAllFail() {
        return failMonitor.readAllMap();
    }

    public Map<String, Long> getAllCarrier() {
        return carrierMonitor.readAllMap();
    }


    public String getKeyName(Date date) {
        Integer hour = DateUtil.getHour(date);
        Integer minute = DateUtil.getMinute(date);
        return (hour <= 9 ? "0" + hour : hour) + (minute >= 30 ? ":30" : ":00");
    }

}
