package com.m7.abs.support.domain;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.concurrent.TimeUnit;

/**
 * @author zhuhf
 * @see com.m7.abs.support.core.nsq.NsqTaskExecutor#submit(NsqTask)
 */
@Data
@Accessors(fluent = true)
@Builder(toBuilder = true)
public class NsqTask<T> {
    private String taskId;

    /**
     * requestId
     */
    private String requestId;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 任务
     */
    @ToString.Exclude
    private Runnable task;
    /**
     * 任务消息
     */
    private T data;
    /**
     * 线程任务超时时间
     */
    private Long timeout;
    /**
     * 线程任务超时时间单位
     */
    private TimeUnit timeoutUnit;
    /**
     * 任务重试次数
     */
    private Integer retryTime;
    /**
     * 开始之前任务前调用
     */
    @ToString.Exclude
    private EventNotifier<T> onStart;
    /**
     * 任务执行异常时调用
     */
    @ToString.Exclude
    private EventNotifier<ExceptionInfo<T>> onException;
    /**
     * 任务成功执行完毕后调用
     */
    @ToString.Exclude
    private EventNotifier<T> onSuccessful;
    /**
     * 任务执行完成后调用，不管任务执行结果
     */
    @ToString.Exclude
    private EventNotifier<T> onCompleted;
    /**
     * 拒绝执行时调用
     */
    @ToString.Exclude
    private EventNotifier<T> onRejected;
    /**
     * 任务执行超时时调用
     */
    @ToString.Exclude
    private EventNotifier<T> onTimeout;
    /**
     * 任务中断时调用
     */
    @ToString.Exclude
    private EventNotifier<T> onInterrupted;

    @Data
    @Accessors(fluent = true)
    @Builder(toBuilder = true)
    public static class ExceptionInfo<T> {
        private T data;
        private Exception exception;
    }

    @Data
    @Accessors(fluent = true)
    @Builder(toBuilder = true)
    public static class TaskEvent<T> {
        private String taskName;
        private T data;
    }

    @FunctionalInterface
    public interface EventNotifier<T> {

        /**
         * 发生事件时调用
         *
         * @param event 事件
         */
        void onEvent(TaskEvent<T> event);
    }

    public int addRetryTime() {
        if (this.retryTime == null) {
            this.retryTime = 0;
        }
        this.retryTime++;
        return this.retryTime;
    }
}
