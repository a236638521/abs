package com.m7.abs.api.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Kejie Peng
 */
@Getter
@Setter
@Component
@RefreshScope
@ConfigurationProperties(prefix = "abs")
public class AbsApiProperties {
    @NestedConfigurationProperty
    private SupportProperties support = new SupportProperties();
    @NestedConfigurationProperty
    private ApiProperties api = new ApiProperties();
    @NestedConfigurationProperty
    private ThreadPoolProperties threadPool = new ThreadPoolProperties();

    @Getter
    @Setter
    public static class SupportProperties {
        private String serverUrl;
    }


    @Getter
    @Setter
    public static class ApiProperties {
        /**
         * API服务地址
         */
        private String serverUrl;
        /**
         * 小号录音地址只推送path
         */
        private boolean midNumPushFilePathOnly = false;
        @NestedConfigurationProperty
        private OssProxyProperties ossProxy = new OssProxyProperties();
        @NestedConfigurationProperty
        private OssProperties oss = new OssProperties();
        @NestedConfigurationProperty
        private ReportProperties report = new ReportProperties();

        @Getter
        @Setter
        public static class ReportProperties {
            private boolean enable = true;
            /**
             * 小号报表NSQ Topic
             */
            private String midNumCdrReportTopic;
            /**
             * 闪信报表NSQ Topic
             */
            private String flashSmCdrReportTopic;
        }

        @Getter
        @Setter
        public static class OssProxyProperties {
            private boolean enable = false;
            private Map<String, String> mapping;
        }

        @Getter
        @Setter
        public static class OssProperties {
            /**
             * 需要入库的storage id
             */
            private String saveCdrStorageId;
            /**
             * 需要推送的storage id
             */
            private String pushStorageId;
        }

    }

    @Getter
    @Setter
    public static class ThreadPoolProperties {

        @NestedConfigurationProperty
        private AsyncRequestProperties asyncRequest = new AsyncRequestProperties();
        @NestedConfigurationProperty
        private RecordFileRequestProperties recordFile = new RecordFileRequestProperties();
        @NestedConfigurationProperty
        private FlashSmsRequestProperties flashSms = new FlashSmsRequestProperties();

        @Getter
        @Setter
        public static class AsyncRequestProperties {
            private int corePoolSize;
            private int maxPoolSize;
            private int queueCapacity;
        }

        @Getter
        @Setter
        public static class RecordFileRequestProperties {
            private int corePoolSize;
            private int maxPoolSize;
            private int queueCapacity;
        }

        @Getter
        @Setter
        public static class FlashSmsRequestProperties {
            private int corePoolSize;
            private int maxPoolSize;
            private int queueCapacity;
        }
    }

}
