package com.m7.abs.support.core.config;

import com.m7.abs.support.common.constant.AbsSupportProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
@EnableAsync
public class ExecutorConfig {
    @Resource
    private AbsSupportProperties absSupportProperties;

    /**
     * 发送异步请求的线程队列
     * @return
     */
    @Bean
    public Executor asyncUploadExecutor() {
        log.info("start async upload service");

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(absSupportProperties.getThreadPool().getAsyncUpload().getCorePoolSize());
        //配置最大线程数
        executor.setMaxPoolSize(absSupportProperties.getThreadPool().getAsyncUpload().getMaxPoolSize());
        //配置队列大小
        executor.setQueueCapacity(absSupportProperties.getThreadPool().getAsyncUpload().getQueueCapacity());
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("Async-Upload-");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }

}
