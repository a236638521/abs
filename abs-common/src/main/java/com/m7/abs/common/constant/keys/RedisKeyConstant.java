package com.m7.abs.common.constant.keys;

/**
 * @author Kejie Peng
 * @date 2023年 03月28日 17:33:35
 */
public interface RedisKeyConstant {
    String MID_NUM_COMMON_PREFIX = "abs:mid-num:";
    String FLASH_SM_COMMON_PREFIX = "abs:flash-sm:";

    class MidNum {
        private static final String DELAYED_QUEUE_KEY_FORMAT_NO_ACCOUNT = MID_NUM_COMMON_PREFIX + "delayed-queue:%s";
        private static final String REPORT_KEY_FORMAT = MID_NUM_COMMON_PREFIX + "report:cdr:%s:%s";
        private static final String REPORT_KEY_FORMAT_NO_ACCOUNT = MID_NUM_COMMON_PREFIX + "report:cdr:%s";

        /**
         * 延迟队列
         * @param key
         * @return
         */
        public static String getDelayedQueueKeyFormatNoAccount(String key) {
            return String.format(DELAYED_QUEUE_KEY_FORMAT_NO_ACCOUNT, key);
        }

        /**
         * 报表 no account
         * @param key
         * @return
         */
        public static String getReportKeyFormatNoAccount(String key) {
            return String.format(REPORT_KEY_FORMAT_NO_ACCOUNT, key);
        }

        /**
         * 获取统计报表key
         *
         * @param key
         * @param accountId
         * @return
         */
        public static String getReportKey(String key, String accountId) {
            return String.format(REPORT_KEY_FORMAT, accountId, key);
        }
    }
    class FlashSms {
        private static final String REPORT_KEY_FORMAT = FLASH_SM_COMMON_PREFIX + "report:cdr:%s:%s";
        private static final String REPORT_KEY_FORMAT_NO_ACCOUNT = FLASH_SM_COMMON_PREFIX + "report:cdr:%s";

        public static String getReportKeyFormatNoAccount(String key) {
            return String.format(REPORT_KEY_FORMAT_NO_ACCOUNT, key);
        }

        /**
         * 获取统计报表key
         *
         * @param key
         * @param accountId
         * @return
         */
        public static String getReportKey(String key, String accountId) {
            return String.format(REPORT_KEY_FORMAT, accountId, key);
        }
    }
}
