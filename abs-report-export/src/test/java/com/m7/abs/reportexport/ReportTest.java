package com.m7.abs.reportexport;

import com.m7.abs.common.domain.entity.MiddleNumberCdrReportEntity;
import com.m7.abs.common.utils.DateUtil;
import com.m7.abs.reportexport.service.IMiddleNumberCdrReportService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Kejie Peng
 * @date 2023年 04月18日 11:39:50
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ReportTest {
    @Autowired
    private IMiddleNumberCdrReportService middleNumberCdrReportService;

    @Test
    public void importData() {
        long start = System.currentTimeMillis();
        String accountId = "1515967219528634369";
        List<Map<String, MiddleNumberCdrReportEntity>> dayList = new ArrayList<>();

        Date startDate = DateUtil.parseStrToDate("2023-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
        ThreadLocalRandom current = ThreadLocalRandom.current();

        for (int i = 0; i < 365; i++) {
            Map<String, MiddleNumberCdrReportEntity> dateTimeMap = new HashMap<>();
            for (int j = 0; j < 48; j++) {
                String key = DateUtil.parseDateToStr(startDate, DateUtil.DATE_FORMAT_YYYY_MM_DD) + " " + getKeyName(startDate);
                long callCount = current.nextLong(5000, 10000);
                long callComplete = current.nextLong(4000, callCount);
                long billDurationCount = current.nextLong(10000, 50000);
                long rateDurationCount = getRateDuration(billDurationCount);
                MiddleNumberCdrReportEntity reportEntity = MiddleNumberCdrReportEntity.builder()
                        .callCount(callCount)
                        .callComplete(callComplete)
                        .callFail(callCount - callComplete)
                        .billDurationCount(billDurationCount)
                        .rateDurationCount(rateDurationCount)
                        .calledCarrierMobile(current.nextLong(10000, 20000))
                        .calledCarrierTelecom(current.nextLong(10000, 20000))
                        .calledCarrierUnicom(current.nextLong(10000, 20000))
                        .calledCarrierUnknown(current.nextLong(10000, 20000))
                        .build();
                dateTimeMap.put(key, reportEntity);
                startDate = DateUtil.addDate(startDate, Calendar.MINUTE, 30);
            }
            dayList.add(dateTimeMap);
        }

        dayList.parallelStream().forEach(item -> {
            middleNumberCdrReportService.batchSaveCdrReport(accountId, item);
        });

        long end = System.currentTimeMillis();
        log.info("耗时:{}ms", start - end);
    }


    public String getKeyName(Date date) {
        Integer hour = DateUtil.getHour(date);
        Integer minute = DateUtil.getMinute(date);
        return (hour <= 9 ? "0" + hour : hour) + (minute >= 30 ? ":30" : ":00");
    }

    /**
     * 以60秒为单位,计算分钟数,不足60秒按照1分钟计算
     *
     * @param duration
     * @return
     */
    private long getRateDuration(long duration) {
        if (duration == 0) {
            return 0;
        }
        long a = duration / 60;
        long b = duration % 60;
        if (b > 0) {
            a++;
        }
        return a;
    }
}
