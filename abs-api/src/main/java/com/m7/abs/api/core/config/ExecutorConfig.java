package com.m7.abs.api.core.config;

import com.m7.abs.api.common.properties.AbsApiProperties;
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
    private AbsApiProperties absApiProperties;

    /**
     * 发送异步请求的线程队列
     * @return
     */
    @Bean
    public Executor cdrRequestExecutor() {
        log.info("start async cdr request service");

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(absApiProperties.getThreadPool().getAsyncRequest().getCorePoolSize());
        //配置最大线程数
        executor.setMaxPoolSize(absApiProperties.getThreadPool().getAsyncRequest().getMaxPoolSize());
        //配置队列大小
        executor.setQueueCapacity(absApiProperties.getThreadPool().getAsyncRequest().getQueueCapacity());
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("Async-CdrRequest-");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }


    @Bean
    public Executor recordFileRequestExecutor() {
        log.info("start async recordFile service");

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(absApiProperties.getThreadPool().getRecordFile().getCorePoolSize());
        //配置最大线程数
        executor.setMaxPoolSize(absApiProperties.getThreadPool().getRecordFile().getMaxPoolSize());
        //配置队列大小
        executor.setQueueCapacity(absApiProperties.getThreadPool().getRecordFile().getQueueCapacity());
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("Async-RecordFile-");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }

    @Bean
    public Executor flashSmsExecutor() {
        log.info("start flash sms service");

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(absApiProperties.getThreadPool().getFlashSms().getCorePoolSize());
        //配置最大线程数
        executor.setMaxPoolSize(absApiProperties.getThreadPool().getFlashSms().getMaxPoolSize());
        //配置队列大小
        executor.setQueueCapacity(absApiProperties.getThreadPool().getFlashSms().getQueueCapacity());
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("Async-FlashSms-");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }
}
