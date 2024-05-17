package com.m7.abs.reportexport.core.config;


import com.m7.abs.common.constant.common.StorageType;
import com.m7.abs.reportexport.common.properties.AbsReportProperties;
import com.m7.abs.reportexport.core.storage.CloudStorage;
import com.m7.abs.reportexport.core.storage.MobileEos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhuhf
 */
@Slf4j
@Configuration
public class OssAutoConfiguration {

    @Bean(name = StorageType.YD_EOS_STORAGE, destroyMethod = "shutdown")
    @ConditionalOnProperty(value = "abs.recording-dump.eos.enable", havingValue = "true")
    public CloudStorage ydCloudStorage(AbsReportProperties absReportProperties) {
        AbsReportProperties.RecordingDumpProperties properties = absReportProperties.getRecordingDump();
        return new MobileEos(properties.getEos());
    }


}
