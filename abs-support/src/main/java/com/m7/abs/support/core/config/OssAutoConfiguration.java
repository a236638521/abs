package com.m7.abs.support.core.config;

import com.m7.abs.common.constant.common.StorageType;
import com.m7.abs.support.common.constant.AbsSupportProperties;
import com.m7.abs.support.common.constant.AbsSupportProperties.RecordingDumpProperties;
import com.m7.abs.support.core.storage.AliyunOss;
import com.m7.abs.support.core.storage.CloudStorage;
import com.m7.abs.support.core.storage.HuaweiObs;
import com.m7.abs.support.core.storage.MobileEos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhuhf
 */
@Configuration
public class OssAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(OssAutoConfiguration.class);

    /*@Bean(destroyMethod = "shutdown")
    public CloudStorage cloudStorage(AbsSupportProperties absSupportProperties) {
        RecordingDumpProperties properties = absSupportProperties.getRecordingDump();
        if (properties.getAliyunOss().getEnable()) {
            return aliyunOss(properties.getAliyunOss());
        }
        if (properties.getEos().getEnable()) {
            return new MobileEos(properties.getEos());
        }
        throw new IllegalArgumentException("未指定启用的云存储类型");
    }*/


    @Bean(name = StorageType.ALI_OSS_STORAGE, destroyMethod = "shutdown")
    @ConditionalOnProperty(value = "abs.support.recording-dump.aliyun-oss.enable", havingValue = "true")
    public CloudStorage aliCloudStorage(AbsSupportProperties absSupportProperties) {
        RecordingDumpProperties properties = absSupportProperties.getRecordingDump();
        return new AliyunOss(properties.getAliyunOss());
    }

    @Bean(name = StorageType.YD_EOS_STORAGE, destroyMethod = "shutdown")
    @ConditionalOnProperty(value = "abs.support.recording-dump.eos.enable", havingValue = "true")
    public CloudStorage ydCloudStorage(AbsSupportProperties absSupportProperties) {
        RecordingDumpProperties properties = absSupportProperties.getRecordingDump();
        return new MobileEos(properties.getEos());
    }

    @Bean(name = StorageType.HW_OBS_STORAGE, destroyMethod = "shutdown")
    @ConditionalOnProperty(value = "abs.support.recording-dump.obs.enable", havingValue = "true")
    public CloudStorage hwCloudStorage(AbsSupportProperties absSupportProperties) {
        RecordingDumpProperties properties = absSupportProperties.getRecordingDump();
        return new HuaweiObs(properties.getObs());
    }


}
