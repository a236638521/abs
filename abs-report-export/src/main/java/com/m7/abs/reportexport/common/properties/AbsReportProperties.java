package com.m7.abs.reportexport.common.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@RefreshScope
@ConfigurationProperties(prefix = "abs")
public class AbsReportProperties {
    @NestedConfigurationProperty
    private ThreadPoolProperties threadPool = new ThreadPoolProperties();
    @NestedConfigurationProperty
    private RecordingDumpProperties recordingDump = new RecordingDumpProperties();
    @NestedConfigurationProperty
    private ExportProperties exportExcel = new ExportProperties();
    @NestedConfigurationProperty
    private ApiProperties api = new ApiProperties();
    @NestedConfigurationProperty
    private TaskProperties task = new TaskProperties();

    @Getter
    @Setter
    public static class TaskProperties {
        /**
         * redis中锁过期时间  单位/毫秒
         */
        private int lockExpiration=60000;
        /**
         * 报表统计,没10分钟执行一次
         */
        private String reportSumCron = "0 0/10 * * * ?";
        /**
         * 报表统计,每日凌晨3点检查前一天统计报表
         */
        private String reportDaySumCron = "0 0 3 * * ?";
    }

    @Getter
    @Setter
    public static class ApiProperties {
        @NestedConfigurationProperty
        private ReportProperties report = new ReportProperties();

        @Getter
        @Setter
        public static class ReportProperties {
            /**
             * 小号报表NSQ Topic
             */
            private String midNumCdrReportTopic;
            /**
             * channel
             */
            private String midNumCdrReportChannel = "abs-report";
            /**
             * 闪信报表NSQ Topic
             */
            private String flashSmCdrReportTopic;
            /**
             * channel
             */
            private String flashSmCdrReportChannel = "abs-report";
        }
    }

    @Data
    public static class ExportProperties {
        /**
         * 导出文件临时文件目录
         */
        private String tempFileBasePath = "./temp/excel_temp";
        /**
         * 压缩文件临时目录
         */
        private String tempZipFileBasePath = "./temp/excel_zip";
        /**
         * 单次操作写入数量
         */
        private long maxSize = 10000L;
        /**
         * 每个文件最多写入多少条数据,超出则需要分文件
         */
        private long eachFileMaxSize = 50000L;

    }

    @Data
    public static class RecordingDumpProperties {
        /**
         * 文件上传临时文件目录
         */
        private String tempFileBasePath = "./temp/upload_temp";

        @NestedConfigurationProperty
        private EosProperties eos = new EosProperties();
    }

    /**
     * @author zhuhf
     */
    @Data
    public static class EosProperties {
        private String accessKey;
        private String secretKey;
        private String hostname;
        private String internalHostname;
        private Boolean pathStyleAccess = false;
        private String bucketName;
        private com.amazonaws.services.s3.model.CannedAccessControlList cannedAcl =
                com.amazonaws.services.s3.model.CannedAccessControlList.Private;
        private Boolean enable = false;

        public void setCannedAcl(String cannedAclName) {
            if (StringUtils.isNotEmpty(cannedAclName)) {
                switch (cannedAclName) {
                    case "PRIVATE":
                        this.cannedAcl = com.amazonaws.services.s3.model.CannedAccessControlList.Private;
                        break;
                    case "PUBLIC_READ":
                        this.cannedAcl = com.amazonaws.services.s3.model.CannedAccessControlList.PublicRead;
                        break;
                    case "PUBLIC_READ_WRITE":
                        this.cannedAcl = com.amazonaws.services.s3.model.CannedAccessControlList.PublicReadWrite;
                        break;
                    case "LOG_DELIVERY_WRITE":
                        this.cannedAcl = com.amazonaws.services.s3.model.CannedAccessControlList.LogDeliveryWrite;
                        break;
                    case "BUCKET_OWNER_READ":
                        this.cannedAcl = com.amazonaws.services.s3.model.CannedAccessControlList.BucketOwnerRead;
                        break;
                    case "BUCKET_OWNER_FULL_CONTROL":
                        this.cannedAcl = com.amazonaws.services.s3.model.CannedAccessControlList.BucketOwnerFullControl;
                        break;
                    case "AWS_EXEC_READ":
                        this.cannedAcl = com.amazonaws.services.s3.model.CannedAccessControlList.AwsExecRead;
                        break;

                }
            }
        }
    }

    @Getter
    @Setter
    public static class ThreadPoolProperties {

        @NestedConfigurationProperty
        private AsyncRequestProperties asyncCdrExport = new AsyncRequestProperties();


        @Getter
        @Setter
        public static class AsyncRequestProperties {
            private int corePoolSize;
            private int maxPoolSize;
            private int queueCapacity;
        }

    }

}
