package com.m7.abs.reportexport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.common.constant.common.CarrierTypeEnum;
import com.m7.abs.common.constant.common.MidNumCallStatusEnum;
import com.m7.abs.common.constant.keys.CommonSessionKeys;
import com.m7.abs.common.constant.keys.RedisKeyConstant;
import com.m7.abs.common.domain.dto.MiddleNumberCdrReport;
import com.m7.abs.common.domain.dto.NsqMsgDTO;
import com.m7.abs.common.domain.entity.AccountEntity;
import com.m7.abs.common.domain.entity.MiddleNumberCdrReportEntity;
import com.m7.abs.common.utils.DateUtil;
import com.m7.abs.common.utils.FastJsonUtils;
import com.m7.abs.reportexport.common.properties.MidNumReportFields;
import com.m7.abs.reportexport.domain.dto.MidNumReportDTO;
import com.m7.abs.reportexport.mapper.MiddleNumberCdrReportMapper;
import com.m7.abs.reportexport.service.IAccountService;
import com.m7.abs.reportexport.service.IMiddleNumberCdrReportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RSetCache;
import org.redisson.api.RedissonClient;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Kejie Peng
 * @since 2023-04-03
 */
@Slf4j
@Service
public class MiddleNumberCdrReportServiceImpl extends ServiceImpl<MiddleNumberCdrReportMapper, MiddleNumberCdrReportEntity> implements IMiddleNumberCdrReportService {
    private MiddleNumberCdrReportMapper middleNumberCdrReportMapper;
    private IAccountService accountService;
    private RedissonClient redissonClient;
    private static ConcurrentHashMap<String, MidNumReportDTO> reportCache = new ConcurrentHashMap<>();
    private static RSetCache<String> accountSet;
    private static RSetCache<String> nextDayAccountSet;

    public MiddleNumberCdrReportServiceImpl(
            RedissonClient redissonClient,
            IAccountService accountService,
            MiddleNumberCdrReportMapper middleNumberCdrReportMapper
    ) {
        this.accountService = accountService;
        this.middleNumberCdrReportMapper = middleNumberCdrReportMapper;
        this.redissonClient = redissonClient;
        accountSet = this.redissonClient.getSetCache(RedisKeyConstant.MidNum.getReportKeyFormatNoAccount(MidNumReportFields.CURRENT_NEED_REPORT_ACCOUNT));
        nextDayAccountSet = this.redissonClient.getSetCache(RedisKeyConstant.MidNum.getReportKeyFormatNoAccount(MidNumReportFields.NEXT_DAY_NEED_REPORT_ACCOUNT));
    }

    @Override
    @Async("midNumReportNsqExecutor")
    public void handleMidNumCdrMsg(NsqMsgDTO<MiddleNumberCdrReport> msg) {
        long time1 = System.currentTimeMillis();
        MDC.put(CommonSessionKeys.REQ_ID_KEY, msg.getTraceId());
        MiddleNumberCdrReport data = msg.getData();
        if (data != null) {
            Date beginTime = data.getBeginTime();
            String accountId = data.getAccountId();
            if (StringUtils.isEmpty(accountId)) {
                log.warn("accountId is empty.");
                return;
            }
            AccountEntity accountEntity = accountService.getAccountById(accountId);
            if (accountEntity == null) {
                log.warn("account is not found.");
                return;
            }
            accountId = accountEntity.getId();
            if (beginTime == null) {
                log.warn("beginTime is null.");
                return;
            }

            String dateStr = DateUtil.parseDateToStr(beginTime, DateUtil.DATE_FORMAT_YYYY_MM_DD);

            accountSet.add(accountId, 1, TimeUnit.HOURS);
            nextDayAccountSet.add(accountId, 32, TimeUnit.HOURS);
            MidNumReportDTO midNumReportDTO = getReportRedissonCache(accountId, dateStr);
            String keyName = midNumReportDTO.getKeyName(beginTime);
            midNumReportDTO.addCallCount(keyName);

            String result = data.getResult();
            if (result != null && MidNumCallStatusEnum.NORMAL_HANGUP.equals(MidNumCallStatusEnum.getInstance(result))) {
                midNumReportDTO.addCallComplete(keyName);
                /**
                 * 正常挂断计算运营商
                 */
                String callerCarrier = Optional.ofNullable(data.getCallerCarrier()).orElse(CarrierTypeEnum.UNKNOWN.getType());
                String calledCarrier = Optional.ofNullable(data.getCalledCarrier()).orElse(CarrierTypeEnum.UNKNOWN.getType());
                midNumReportDTO.addCallerCarrier(callerCarrier + "@" + keyName);
                midNumReportDTO.addCalledCarrier(calledCarrier + "@" + keyName);
            } else {
                midNumReportDTO.addCallFail(keyName);
            }

            long duration = data.getBillDuration();
            if (duration != 0L) {
                midNumReportDTO.addBillDuration(keyName, duration);
            }
            long rateDuration = data.getRateDuration();
            if (rateDuration != 0L) {
                midNumReportDTO.addRateDuration(keyName, rateDuration);
            }
            long time2 = System.currentTimeMillis();
            log.info("插入小号记录===> callId:{},keyName:{},cost:{}ms", data.getId(), keyName, time2 - time1);
        }

    }

    @Override
    @Async("midNumReportHandleExecutor")
    public void sumCurrentMidNumCdrReport(String traceId, String accountId, Date date) {
        MDC.put(CommonSessionKeys.REQ_ID_KEY, traceId);
        String currentDate = DateUtil.parseDateToStr(date, DateUtil.DATE_FORMAT_YYYY_MM_DD);
        log.info("{} start handle [{}] mid num report.", currentDate, accountId);
        long time1 = System.currentTimeMillis();
        MidNumReportDTO midNumReportDTO = getReportRedissonCache(accountId, currentDate);
        if (midNumReportDTO != null) {
            Map<String, Long> callCount = midNumReportDTO.getAllCallCount();
            Map<String, Long> callComplete = midNumReportDTO.getAllCallComplete();
            Map<String, Long> callFail = midNumReportDTO.getAllCallFail();
            Map<String, Long> callerCarrier = midNumReportDTO.getAllCallerCarrier();
            Map<String, Long> calledCarrier = midNumReportDTO.getAllCalledCarrier();
            Map<String, Long> billDurationMonitor = midNumReportDTO.getBillDurationMonitor();
            Map<String, Long> rateDurationMonitor = midNumReportDTO.getRateDurationMonitor();

            long time2 = System.currentTimeMillis();
            log.info("get countInfo,cost:{}ms", time2 - time1);
            Map<String, MiddleNumberCdrReportEntity> reportMap = new HashMap<>();
            callCount.forEach((key, value) -> {
                key = currentDate + " " + key;
                MiddleNumberCdrReportEntity report = getReport(key, reportMap);
                report.setCallCount(value);
                reportMap.put(key, report);
            });
            callComplete.forEach((key, value) -> {
                key = currentDate + " " + key;
                MiddleNumberCdrReportEntity report = getReport(key, reportMap);
                report.setCallComplete(value);
                reportMap.put(key, report);
            });
            callFail.forEach((key, value) -> {
                key = currentDate + " " + key;
                MiddleNumberCdrReportEntity report = getReport(key, reportMap);
                report.setCallFail(value);
                reportMap.put(key, report);
            });
            billDurationMonitor.forEach((key, value) -> {
                key = currentDate + " " + key;
                MiddleNumberCdrReportEntity report = getReport(key, reportMap);
                report.setBillDurationCount(value);
                reportMap.put(key, report);
            });
            rateDurationMonitor.forEach((key, value) -> {
                key = currentDate + " " + key;
                MiddleNumberCdrReportEntity report = getReport(key, reportMap);
                report.setRateDurationCount(value);
                reportMap.put(key, report);
            });
            callerCarrier.forEach((key, value) -> {

                String[] split = key.split("@");
                if (split != null && split.length == 2) {
                    String carrier = split[0];
                    String mapKey = split[1];
                    mapKey = currentDate + " " + mapKey;
                    MiddleNumberCdrReportEntity report = getReport(mapKey, reportMap);
                    switch (CarrierTypeEnum.getInstance(carrier)) {
                        case CHINA_UNICOM:
                            report.setCallerCarrierUnicom(value);
                            break;
                        case CHINA_MOBILE:
                            report.setCallerCarrierMobile(value);
                            break;
                        case CHINA_TELECOM:
                            report.setCallerCarrierTelecom(value);
                            break;
                        default:
                            report.setCallerCarrierUnknown(value);
                            break;
                    }
                    reportMap.put(mapKey, report);
                }
            });
            calledCarrier.forEach((key, value) -> {
                String[] split = key.split("@");
                if (split != null && split.length == 2) {
                    String carrier = split[0];
                    String mapKey = split[1];
                    mapKey = currentDate + " " + mapKey;
                    MiddleNumberCdrReportEntity report = getReport(mapKey, reportMap);
                    switch (CarrierTypeEnum.getInstance(carrier)) {
                        case CHINA_UNICOM:
                            report.setCalledCarrierUnicom(value);
                            break;
                        case CHINA_MOBILE:
                            report.setCalledCarrierMobile(value);
                            break;
                        case CHINA_TELECOM:
                            report.setCalledCarrierTelecom(value);
                            break;
                        default:
                            report.setCalledCarrierUnknown(value);
                            break;
                    }
                    reportMap.put(mapKey, report);
                }
            });
            long time3 = System.currentTimeMillis();
            log.info("Organize data,cost:{}ms", time3 - time2);

            log.info("[{}] cost:{}ms, save report info:{}", accountId, time3 - time1, FastJsonUtils.toJSONString(reportMap));

            this.batchSaveCdrReport(accountId, reportMap);
        }

        MDC.remove(CommonSessionKeys.REQ_ID_KEY);
    }


    private MiddleNumberCdrReportEntity getReport(String key, Map<String, MiddleNumberCdrReportEntity> reportMap) {
        MiddleNumberCdrReportEntity reportEntity = reportMap.get(key);
        if (reportEntity == null) {
            reportEntity = MiddleNumberCdrReportEntity.builder().build();
        }
        return reportEntity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSaveCdrReport(String accountId, Map<String, MiddleNumberCdrReportEntity> dateTimeMap) {
        if (dateTimeMap == null || dateTimeMap.size() == 0) {
            return;
        }
        Set<String> dateTimeSet = dateTimeMap.keySet();
        LambdaQueryWrapper<MiddleNumberCdrReportEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MiddleNumberCdrReportEntity::getAccountId, accountId);
        queryWrapper.in(MiddleNumberCdrReportEntity::getDateTime, dateTimeSet);
        List<MiddleNumberCdrReportEntity> existReports = middleNumberCdrReportMapper.selectList(queryWrapper);
        /**
         * updateList
         */
        if (existReports != null && existReports.size() > 0) {
            existReports.stream().forEach(item -> {
                MiddleNumberCdrReportEntity newData = dateTimeMap.remove(item.getDateTime());
                if (newData != null) {
                    item.setCallCount(newData.getCallCount());
                    item.setCallComplete(newData.getCallComplete());
                    item.setCallFail(newData.getCallFail());
                    item.setCalledCarrierMobile(newData.getCalledCarrierMobile());
                    item.setCalledCarrierTelecom(newData.getCalledCarrierTelecom());
                    item.setCalledCarrierUnicom(newData.getCalledCarrierUnicom());
                    item.setCalledCarrierUnknown(newData.getCalledCarrierUnknown());
                    item.setCallerCarrierMobile(newData.getCallerCarrierMobile());
                    item.setCallerCarrierTelecom(newData.getCallerCarrierTelecom());
                    item.setCallerCarrierUnicom(newData.getCallerCarrierUnicom());
                    item.setCallerCarrierUnknown(newData.getCallerCarrierUnknown());
                    item.setBillDurationCount(newData.getBillDurationCount());
                    item.setRateDurationCount(newData.getRateDurationCount());
                    item.setLastUpdateTime(new Date());
                }
            });
            log.info("update mid num cdr report,size:{}", existReports.size());
            this.updateBatchById(existReports);
        }
        List<MiddleNumberCdrReportEntity> insertReports = new ArrayList<>();
        dateTimeMap.forEach((key, value) -> {
            value.setDateTime(key);
            value.setCreateTime(new Date());
            value.setAccountId(accountId);
            insertReports.add(value);
        });

        if (insertReports != null && insertReports.size() > 0) {
            log.info("insert mid num cdr report,size:{}", insertReports.size());
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
    private MidNumReportDTO getReportRedissonCache(String accountId, String dateStr) {
        String key = accountId + "_" + dateStr;
        MidNumReportDTO midNumReportDTO = reportCache.get(key);
        if (midNumReportDTO == null) {
            midNumReportDTO = new MidNumReportDTO(redissonClient, accountId, dateStr);
            reportCache.put(key, midNumReportDTO);
        }
        return midNumReportDTO;
    }

    /**
     * 清除缓存
     */
    @Override
    public void clearReportRedissonCache() {
        reportCache.clear();
    }
}
