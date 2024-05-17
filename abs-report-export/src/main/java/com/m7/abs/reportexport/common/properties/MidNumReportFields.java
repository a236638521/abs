package com.m7.abs.reportexport.common.properties;

/**
 * @author Kejie Peng
 * @date 2023年 03月28日 17:39:02
 */
public interface MidNumReportFields {
     /**
      * 呼叫量
      */
     String CALL_COUNT = "call-count";
     /**
      * 呼叫成功量
      */
     String CALL_COMPLETE_COUNT = "call-complete-count";
     /**
      * 呼叫失败量
      */
     String CALL_FAIL_COUNT = "call-fail-count";
     /**
      * 主叫运营商统计
      */
     String CALLER_CARRIER_COUNT = "caller-carrier-count";
     /**
      * 被叫运营商统计
      */
     String CALLED_CARRIER_COUNT = "called-carrier-count";
     /**
      * 通话时长统计
      */
     String BILL_DURATION_COUNT = "bill-duration-count";
     /**
      * 计费时长统计
      */
     String RATE_DURATION_COUNT = "rate-duration-count";
     /**
      * 当前需要统计的账户信息
      */
     String CURRENT_NEED_REPORT_ACCOUNT = "current-need-report-account";
     /**
      * 次日凌晨需要统计的账户信息
      */
     String NEXT_DAY_NEED_REPORT_ACCOUNT = "next-day-need-report-account";
}
