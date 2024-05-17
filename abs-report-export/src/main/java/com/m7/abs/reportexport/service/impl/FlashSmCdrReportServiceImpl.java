package com.m7.abs.reportexport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.common.constant.common.CarrierTypeEnum;
import com.m7.abs.common.constant.common.FlashSmDeliveryResultEnum;
import com.m7.abs.common.constant.keys.CommonSessionKeys;
import com.m7.abs.common.constant.keys.RedisKeyConstant;
import com.m7.abs.common.domain.base.PhoneInfo;
import com.m7.abs.common.domain.dto.FlashSmDeliveryReportResultDto;
import com.m7.abs.common.domain.dto.FlashSmReportDto;
import com.m7.abs.common.domain.dto.NsqMsgDTO;
import com.m7.abs.common.domain.entity.FlashSmCdrReportEntity;
import com.m7.abs.common.utils.DateUtil;
import com.m7.abs.common.utils.FastJsonUtils;
import com.m7.abs.common.utils.PhoneUtil;
import com.m7.abs.reportexport.common.properties.FlashSmReportFields;
import com.m7.abs.reportexport.domain.dto.FlashSmReportDTO;
import com.m7.abs.reportexport.mapper.FlashSmCdrReportMapper;
import com.m7.abs.reportexport.service.IFlashSmCdrReportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RSetCache;
import org.redisson.api.RedissonClient;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 话单报表
 * <p>
 * 账户（支持单选多选），运营商（支持单选多选），快捷时间选择、时间区间选择
 * 报表（统计）字段：初步拟定：外呼总数、外呼成功数、外呼失败数、成功率、通话总时长，通话平均时长，通话以分钟计算费用（计费字段：60进1）。
 * 统计的数据表头维度：以半小时和一小时时间维度来统计
 *
 * @author Kejie Peng
 * @date 2023年 03月28日 16:03:15
 */
@Slf4j
@Service
public class FlashSmCdrReportServiceImpl extends ServiceImpl<FlashSmCdrReportMapper, FlashSmCdrReportEntity> implements IFlashSmCdrReportService {
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private FlashSmCdrReportMapper flashSmCdrReportMapper;

    private static ConcurrentHashMap<String, FlashSmReportDTO> reportCache = new ConcurrentHashMap<>();
    private static RSetCache<String> accountSet;
    private static RSetCache<String> nextDayAccountSet;

    public FlashSmCdrReportServiceImpl(
            RedissonClient redissonClient,
            FlashSmCdrReportMapper flashSmCdrReportMapper
    ) {
        this.redissonClient = redissonClient;
        this.flashSmCdrReportMapper = flashSmCdrReportMapper;
        accountSet = this.redissonClient.getSetCache(RedisKeyConstant.FlashSms.getReportKeyFormatNoAccount(FlashSmReportFields.CURRENT_NEED_REPORT_ACCOUNT));
        nextDayAccountSet = this.redissonClient.getSetCache(RedisKeyConstant.FlashSms.getReportKeyFormatNoAccount(FlashSmReportFields.NEXT_DAY_NEED_REPORT_ACCOUNT));
    }

    @Override
    @Async("midNumReportNsqExecutor")
    public void handleFlashSmCdrMsg(NsqMsgDTO<FlashSmReportDto> msg) {
        long time1 = System.currentTimeMillis();
        MDC.put(CommonSessionKeys.REQ_ID_KEY, msg.getTraceId());
        FlashSmReportDto data = msg.getData();
        if (data != null) {
            String accountId = data.getAccountId();
            if (StringUtils.isEmpty(accountId)) {
                log.warn("accountId is empty.");
                return;
            }

            List<FlashSmDeliveryReportResultDto> deliveryResult = data.getDeliveryResult();

            if (deliveryResult != null) {
                Date createTime = data.getCreateTime();
                String dateStr = DateUtil.parseDateToStr(createTime, DateUtil.DATE_FORMAT_YYYY_MM_DD);
                accountSet.add(accountId, 1, TimeUnit.HOURS);
                nextDayAccountSet.add(accountId, 32, TimeUnit.HOURS);

                FlashSmReportDTO flashSmReportDTO = getReportRedissonCache(accountId, dateStr);
                String keyName = flashSmReportDTO.getKeyName(createTime);
                deliveryResult.parallelStream().forEach(item -> {
                    String status = item.getStatus();
                    String target = item.getTarget();

                    FlashSmDeliveryResultEnum deliveryResultEnum = FlashSmDeliveryResultEnum.getInstance(status);
                    flashSmReportDTO.addTotalCount(keyName);
                    switch (deliveryResultEnum) {
                        case DELIVERY_SUCCEED:
                            flashSmReportDTO.addComplete(keyName);
                            if (StringUtils.isNotEmpty(target)) {
                                PhoneInfo calleePhoneInfo = null;
                                try {
                                    calleePhoneInfo = PhoneUtil.getPhoneInfo(target, 86);
                                } catch (Exception e) {
                                    log.error("[{}] get phone area info error", target, e);
                                }
                                if (calleePhoneInfo != null) {
                                    CarrierTypeEnum carrier = calleePhoneInfo.getCarrier();
                                    String targetCarrier = Optional.ofNullable(carrier.getType()).orElse(CarrierTypeEnum.UNKNOWN.getType());
                                    flashSmReportDTO.addCarrier(targetCarrier + "@" + keyName);
                                }
                            }
                            break;
                        default:
                            flashSmReportDTO.addFail(keyName);
                            break;
                    }

                });
                long time2 = System.currentTimeMillis();
                log.info("插入闪信记录===> taskId:{},keyName:{},cost:{}ms", data.getTaskId(), keyName, time2 - time1);
            }

        }

    }

    @Override
    @Async("midNumReportHandleExecutor")
    public void sumCurrentFlashSmCdrReport(String traceId, String accountId, Date date) {
        MDC.put(CommonSessionKeys.REQ_ID_KEY, traceId);
        String currentDate = DateUtil.parseDateToStr(date, DateUtil.DATE_FORMAT_YYYY_MM_DD);
        log.info("{} start handle [{}] flash sm report.", currentDate, accountId);
        long time1 = System.currentTimeMillis();
        FlashSmReportDTO reportDTO = getReportRedissonCache(accountId, currentDate);
        if (reportDTO != null) {
            Map<String, Long> totalCount = reportDTO.getAllTotalCount();
            Map<String, Long> complete = reportDTO.getAllComplete();
            Map<String, Long> fail = reportDTO.getAllFail();
            Map<String, Long> allCarrier = reportDTO.getAllCarrier();

            long time2 = System.currentTimeMillis();
            log.info("get countInfo,cost:{}ms", time2 - time1);
            Map<String, FlashSmCdrReportEntity> reportMap = new HashMap<>();
            totalCount.forEach((key, value) -> {
                key = currentDate + " " + key;
                FlashSmCdrReportEntity report = getReport(key, reportMap);
                report.setTotalCount(value);
                reportMap.put(key, report);
            });
            complete.forEach((key, value) -> {
                key = currentDate + " " + key;
                FlashSmCdrReportEntity report = getReport(key, reportMap);
                report.setComplete(value);
                reportMap.put(key, report);
            });
            fail.forEach((key, value) -> {
                key = currentDate + " " + key;
                FlashSmCdrReportEntity report = getReport(key, reportMap);
                report.setFail(value);
                reportMap.put(key, report);
            });


            allCarrier.forEach((key, value) -> {
                String[] split = key.split("@");
                if (split != null && split.length == 2) {
                    String carrier = split[0];
                    String mapKey = split[1];
                    mapKey = currentDate + " " + mapKey;
                    FlashSmCdrReportEntity report = getReport(mapKey, reportMap);
                    switch (CarrierTypeEnum.getInstance(carrier)) {
                        case CHINA_UNICOM:
                            report.setCarrierUnicom(value);
                            break;
                        case CHINA_MOBILE:
                            report.setCarrierMobile(value);
                            break;
                        case CHINA_TELECOM:
                            report.setCarrierTelecom(value);
                            break;
                        default:
                            report.setCarrierUnknown(value);
                            break;
                    }
                    reportMap.put(mapKey, report);
                }
            });

            long time3 = System.currentTimeMillis();
            log.info("Organize data,cost:{}ms", time3 - time2);

            log.info("[{}] cost:{}ms, save flash sm report info:{}", accountId, time3 - time1, FastJsonUtils.toJSONString(reportMap));

            this.batchSaveCdrReport(accountId, reportMap);
        }

        MDC.remove(CommonSessionKeys.REQ_ID_KEY);
    }


    private FlashSmCdrReportEntity getReport(String key, Map<String, FlashSmCdrReportEntity> reportMap) {
        FlashSmCdrReportEntity reportEntity = reportMap.get(key);
        if (reportEntity == null) {
            reportEntity = FlashSmCdrReportEntity.builder().build();
        }
        return reportEntity;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSaveCdrReport(String accountId, Map<String, FlashSmCdrReportEntity> dateTimeMap) {
        if (dateTimeMap == null || dateTimeMap.size() == 0) {
            return;
        }
        Set<String> dateTimeSet = dateTimeMap.keySet();
        LambdaQueryWrapper<FlashSmCdrReportEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlashSmCdrReportEntity::getAccountId, accountId);
        queryWrapper.in(FlashSmCdrReportEntity::getDateTime, dateTimeSet);
        List<FlashSmCdrReportEntity> existReports = flashSmCdrReportMapper.selectList(queryWrapper);
        /**
         * updateList
         */
        if (existReports != null && existReports.size() > 0) {
            existReports.stream().forEach(item -> {
                FlashSmCdrReportEntity newData = dateTimeMap.remove(item.getDateTime());
                if (newData != null) {
                    item.setTotalCount(newData.getTotalCount());
                    item.setComplete(newData.getComplete());
                    item.setFail(newData.getFail());
                    item.setCarrierMobile(newData.getCarrierMobile());
                    item.setCarrierTelecom(newData.getCarrierTelecom());
                    item.setCarrierUnicom(newData.getCarrierUnicom());
                    item.setCarrierUnknown(newData.getCarrierUnknown());
                    item.setLastUpdateTime(new Date());
                }
            });
            log.info("update flash sm cdr report,size:{}", existReports.size());
            this.updateBatchById(existReports);
        }
        List<FlashSmCdrReportEntity> insertReports = new ArrayList<>();
        dateTimeMap.forEach((key, value) -> {
            value.setDateTime(key);
            value.setCreateTime(new Date());
            value.setAccountId(accountId);
            insertReports.add(value);
        });

        if (insertReports != null && insertReports.size() > 0) {
            log.info("insert flash sm cdr report,size:{}", insertReports.size());
            this.saveBatch(insertReports);
        }
    }

    /**
     * 获取redisson 对象
     *
     * @param accountId
     * @param dateStr
     * @return
     */
    private FlashSmReportDTO getReportRedissonCache(String accountId, String dateStr) {
        String key = accountId + "_" + dateStr;
        FlashSmReportDTO reportDTO = reportCache.get(key);
        if (reportDTO == null) {
            reportDTO = new FlashSmReportDTO(redissonClient, accountId, dateStr);
            reportCache.put(key, reportDTO);
        }
        return reportDTO;
    }

    /**
     * 清除缓存
     */
    @Override
    public void clearReportRedissonCache() {
        reportCache.clear();
    }
}
