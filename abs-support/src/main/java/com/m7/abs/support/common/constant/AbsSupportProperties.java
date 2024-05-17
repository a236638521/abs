package com.m7.abs.support.common.constant;

import com.aliyun.oss.model.CannedAccessControlList;
import com.obs.services.model.AccessControlList;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author zhuhf
 */
@Component
@ConfigurationProperties(prefix = "abs.support")
@Data
@RefreshScope
public class AbsSupportProperties {
    @NestedConfigurationProperty
    private MidNumTask midNumTask = new MidNumTask();

    @NestedConfigurationProperty
    private NsqTask nsqTask = new NsqTask();

    @NestedConfigurationProperty
    private PushDataProperties pushData = new PushDataProperties();

    @NestedConfigurationProperty
    private RecordingDumpProperties recordingDump = new RecordingDumpProperties();

    @NestedConfigurationProperty
    private ThreadPoolProperties threadPool = new ThreadPoolProperties();

    @Data
    public static class NsqTask {
        /**
         * nsq最大重试次数,避免死循环
         */
        private int maxRetryTime = 30;
    }

    @Data
    public static class MidNumTask {
        /**
         * redis中锁过期时间  单位/毫秒
         */
        private int lockExpiration;
        /**
         * 失效绑定记录保留期限,默认为一天;单位/秒
         */
        private int retentionPeriod = 86400;
        /**
         * 更新过期时间cron表达式,默认每分钟执行一次
         */
        private String updateExpiredLogsCron = "0 0/1 * * * ?";
        /**
         * 删除无效绑定记录cron表达式,默认每天凌晨3点执行一次
         */
        private String removeLogsCron = "0 0 0/3 * * ?";
    }

    @Data
    public static class PushDataProperties {
        /**
         * 最大重试次数
         */
        private int maxRetryTimes = 5;

        private String topic;
        private String channel = "abs-support";
        private Integer retryIntervalTimeDeviation = 20;
    }

    @Data
    public static class RecordingDumpProperties {
        private String topic;
        private String channel = "abs-support";
        /**
         * 下载文件读取数据超时时间 5 分钟
         */
        private int downloadFileReadTimeOut = 5 * 60 * 1000;
        /**
         * 下载文件链接超时时间
         */
        private int downloadFileConnectTimeOut = 10 * 1000;
        /**
         * 录音临时文件目录
         */
        private String tempFileBasePath = "./";
        /**
         * 最大重试次数
         */
        private int maxRetryTimes = 5;

        @Max(value = 60 * 60)
        @Min(value = 0)
        private Integer retryIntervalTimeDeviation = 20;

        @NestedConfigurationProperty
        private AliyunOssProperties aliyunOss = new AliyunOssProperties();
        @NestedConfigurationProperty
        private EosProperties eos = new EosProperties();
        @NestedConfigurationProperty
        private ObsProperties obs = new ObsProperties();
    }

    @Data
    public static class AliyunOssProperties {
        private String endpoint;
        private String internalEndpoint;
        private String accessKeyId;
        private String accessKeySecret;
        private String bucketName;
        private CannedAccessControlList cannedAcl = CannedAccessControlList.Private;
        private Boolean enable = false;

        public void setCannedAcl(String cannedAclName) {
            if (StringUtils.isNotEmpty(cannedAclName)) {
                switch (cannedAclName) {
                    case "DEFAULT":
                        this.cannedAcl = CannedAccessControlList.Default;
                        break;
                    case "PRIVATE":
                        this.cannedAcl = CannedAccessControlList.Private;
                        break;
                    case "PUBLIC_READ":
                        this.cannedAcl = CannedAccessControlList.PublicRead;
                        break;
                    case "PUBLIC_READ_WRITE":
                        this.cannedAcl = CannedAccessControlList.PublicReadWrite;
                        break;
                }
            }
        }
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

    /**
     * 华为云配置
     */
    @Data
    public static class ObsProperties {
        private String endpoint;
        private String internalEndpoint;
        private String accessKey;
        private String secretKey;
        private String bucketName;
        private AccessControlList cannedAcl = AccessControlList.REST_CANNED_PUBLIC_READ;
        private Boolean enable = false;

        public void setCannedAcl(String cannedAclName) {
            if (StringUtils.isNotEmpty(cannedAclName)) {
                switch (cannedAclName) {
                    case "PRIVATE":
                        this.cannedAcl = AccessControlList.REST_CANNED_PRIVATE;
                        break;
                    case "PUBLIC_READ":
                        this.cannedAcl = AccessControlList.REST_CANNED_PUBLIC_READ;
                        break;
                    case "PUBLIC_READ_WRITE":
                        this.cannedAcl = AccessControlList.REST_CANNED_PUBLIC_READ_WRITE;
                        break;
                    case "PUBLIC_READ_DELIVERED":
                        this.cannedAcl = AccessControlList.REST_CANNED_PUBLIC_READ_DELIVERED;
                        break;
                    case "PUBLIC_READ_WRITE_DELIVERED":
                        this.cannedAcl = AccessControlList.REST_CANNED_PUBLIC_READ_WRITE_DELIVERED;
                        break;
                    case "AUTHENTICATED_READ":
                        this.cannedAcl = AccessControlList.REST_CANNED_AUTHENTICATED_READ;
                        break;
                    case "BUCKET_OWNER_READ":
                        this.cannedAcl = AccessControlList.REST_CANNED_BUCKET_OWNER_READ;
                        break;
                    case "BUCKET_OWNER_FULL_CONTROL":
                        this.cannedAcl = AccessControlList.REST_CANNED_BUCKET_OWNER_FULL_CONTROL;
                        break;
                    case "LOG_DELIVERY_WRITE":
                        this.cannedAcl = AccessControlList.REST_CANNED_LOG_DELIVERY_WRITE;
                        break;
                }
            }
        }
    }

    @Getter
    @Setter
    public static class ThreadPoolProperties {

        @NestedConfigurationProperty
        private AsyncUploadProperties asyncUpload = new AsyncUploadProperties();
        @NestedConfigurationProperty
        private NsqTaskProperties nsqTask = new NsqTaskProperties();

        @Getter
        @Setter
        public static class AsyncUploadProperties {
            private int corePoolSize;
            private int maxPoolSize;
            private int queueCapacity;
        }

        @Getter
        @Setter
        public static class NsqTaskProperties {
            private int queueSize = 500;
            private long keepAliveTime = 30L;
        }
    }
}
