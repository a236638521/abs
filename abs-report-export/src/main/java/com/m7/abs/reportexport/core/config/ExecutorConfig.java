package com.m7.abs.reportexport.core.config;

import com.m7.abs.reportexport.common.properties.AbsReportProperties;
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
    private AbsReportProperties absReportProperties;

    /**
     * 异步导出数据线程池
     * @return
     */
    @Bean
    public Executor cdrExportExecutor() {
        log.info("start async cdr export service");

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(absReportProperties.getThreadPool().getAsyncCdrExport().getCorePoolSize());
        //配置最大线程数
        executor.setMaxPoolSize(absReportProperties.getThreadPool().getAsyncCdrExport().getMaxPoolSize());
        //配置队列大小
        executor.setQueueCapacity(absReportProperties.getThreadPool().getAsyncCdrExport().getQueueCapacity());
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("Async-CdrExport-");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }


    /**
     * 消费小号话单nsq线程池
     * @return
     */
    @Bean
    public Executor midNumReportNsqExecutor() {
        log.info("start async mid num report nsq service");

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(absReportProperties.getThreadPool().getAsyncCdrExport().getCorePoolSize());
        //配置最大线程数
        executor.setMaxPoolSize(absReportProperties.getThreadPool().getAsyncCdrExport().getMaxPoolSize());
        //配置队列大小
        executor.setQueueCapacity(absReportProperties.getThreadPool().getAsyncCdrExport().getQueueCapacity());
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("Async-midNumReport-nsq-");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }

    /**
     * 统计小号话单报表
     * @return
     */
    @Bean
    public Executor midNumReportHandleExecutor() {
        log.info("start async mid num report service");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(absReportProperties.getThreadPool().getAsyncCdrExport().getCorePoolSize());
        //配置最大线程数
        executor.setMaxPoolSize(absReportProperties.getThreadPool().getAsyncCdrExport().getMaxPoolSize());
        //配置队列大小
        executor.setQueueCapacity(absReportProperties.getThreadPool().getAsyncCdrExport().getQueueCapacity());
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("Async-midNumReport-handle-");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }

}
