package com.m7.abs.reportexport.common.properties;

/**
 * @author Kejie Peng
 * @date 2023年 03月28日 17:39:02
 */
public interface FlashSmReportFields {
     /**
      * 发送量
      */
     String TOTAL_COUNT = "total-count";
     /**
      * 成功量
      */
     String COMPLETE_COUNT = "complete-count";
     /**
      * 失败量
      */
     String FAIL_COUNT = "fail-count";
     /**
      * 运营商统计
      */
     String CARRIER_COUNT = "carrier-count";
     /**
      * 当前需要统计的账户信息
      */
     String CURRENT_NEED_REPORT_ACCOUNT = "current-need-report-account";
     /**
      * 次日凌晨需要统计的账户信息
      */
     String NEXT_DAY_NEED_REPORT_ACCOUNT = "next-day-need-report-account";
}
