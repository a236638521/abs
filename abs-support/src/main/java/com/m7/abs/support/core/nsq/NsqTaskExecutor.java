package com.m7.abs.support.core.nsq;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.m7.abs.common.constant.keys.CommonSessionKeys;
import com.m7.abs.support.common.constant.AbsSupportProperties;
import com.m7.abs.support.domain.NsqTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.*;

import static com.m7.abs.support.common.util.LambdaExceptionUtil.consumerExceptionWrapper;

/**
 * @author zhuhf
 */
@Component
public class NsqTaskExecutor {
    private static final Logger logger = LoggerFactory.getLogger(NsqTaskExecutor.class);
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors() * 2;
    private static final int QUEUE_SIZE = 5000;
    @Autowired
    private AbsSupportProperties absSupportProperties;

    private final ThreadPoolExecutor pushDataExecutor = new ThreadPoolExecutor(
            THREAD_COUNT,
            THREAD_COUNT,
            30L,
            TimeUnit.MINUTES,
            // nsq 消息自动 requeue 时间: min(max-msg-timeout(default 15m0s), msg-timeout(default 1m0s))
            new LinkedBlockingQueue<>(QUEUE_SIZE),
            new ThreadFactoryBuilder().setNameFormat("pool-nsq-task-%d").build(),
            new ThreadPoolExecutor.AbortPolicy());
    private static final ConcurrentMap<String, NsqTask<Object>> CURRENT_PUSH_TASK_MAP = new ConcurrentHashMap<>(QUEUE_SIZE);


    private final ThreadPoolExecutor dumpFileExecutor = new ThreadPoolExecutor(
            THREAD_COUNT,
            THREAD_COUNT,
            30L,
            TimeUnit.MINUTES,
            // nsq 消息自动 requeue 时间: min(max-msg-timeout(default 15m0s), msg-timeout(default 1m0s))
            new LinkedBlockingQueue<>(QUEUE_SIZE),
            new ThreadFactoryBuilder().setNameFormat("pool-nsq-task-%d").build(),
            new ThreadPoolExecutor.AbortPolicy());
    private static final ConcurrentMap<String, NsqTask<Object>> CURRENT_DUMP_TASK_MAP = new ConcurrentHashMap<>(QUEUE_SIZE);

    /**
     * {@link NsqTask#onRejected()} 在任务提交者线程中执行，其他事件方法在任务执行线程中调用
     * 事件方法调用顺序
     * <ul>
     *     <li>{@link NsqTask#onRejected()}，仅在任务被拒绝时调用，调用后终止任务执行</li>
     *     <li>{@link NsqTask#onTimeout()} 在任务执行超时时调用，调用后停止后续事件方法调用</li>
     *     <li>{@link NsqTask#onInterrupted()} 在任务执行被中断时调用，调用后停止后续事件方法调用</li>
     *     <li>{@link NsqTask#onStart()}</li>
     *     <li>{@link NsqTask#onSuccessful()} 或者 {@link NsqTask#onException()}</li>
     *     <li>{@link NsqTask#onCompleted()}</li>
     * </ul>
     *
     * @param task task
     */
    @SuppressWarnings("unchecked")
    public <T> void submit(NsqTask<T> task) {
        String taskName = task.taskName();
        ConcurrentMap<String, NsqTask<Object>> CURRENT_TASK_MAP = null;
        try {

            ThreadPoolExecutor executor = null;


            switch (taskName) {
                case "pushData":
                    CURRENT_TASK_MAP = CURRENT_PUSH_TASK_MAP;
                    executor = pushDataExecutor;
                    break;

                case "saveToOss":
                    CURRENT_TASK_MAP = CURRENT_DUMP_TASK_MAP;
                    executor = dumpFileExecutor;
                    break;

            }

            int activeCount = executor.getActiveCount();
            int size = executor.getQueue().size();
            logger.info("taskName:{},activeCount:{},queueSize:{},mapSize:{}", taskName, activeCount, size, CURRENT_TASK_MAP.size());


            if (CURRENT_TASK_MAP.containsKey(task.taskId())) {
                NsqTask<Object> memeryTask = CURRENT_TASK_MAP.get(task.taskId());
                logger.warn("重复提交任务，任务正在执行，任务被拒绝：{}", memeryTask);
                int maxRetryTime = absSupportProperties.getNsqTask().getMaxRetryTime();
                int retryTime = memeryTask.addRetryTime();
                if (retryTime <= maxRetryTime) {
                    CURRENT_TASK_MAP.put(task.taskId(), memeryTask);
                    Optional.ofNullable(task.onRejected())
                            .ifPresent(
                                    consumerExceptionWrapper(eventNotifier -> eventNotifier.onEvent(messageEvent(task))));
                } else {
                    logger.warn("超过最大重试次数{}次：{}", maxRetryTime, task);
                    CURRENT_TASK_MAP.remove(task.taskId());
                }
                return;
            }


            ConcurrentMap<String, NsqTask<Object>> finalCURRENT_TASK_MAP = CURRENT_TASK_MAP;
            executor.submit(() -> {
                boolean success = false;
                long startTime = System.currentTimeMillis();
                try {
                    MDC.put(CommonSessionKeys.REQ_ID_KEY, task.requestId());

                    logger.info("开始执行任务: {}", task);
                    Optional.ofNullable(task.onStart())
                            .ifPresent(consumerExceptionWrapper(
                                    eventNotifier -> eventNotifier.onEvent(messageEvent(task))));
                    if (task.timeout() != null && task.timeoutUnit() != null) {
                        CompletableFuture.runAsync(task.task()).get(task.timeout(), task.timeoutUnit());
                    } else {
                        task.task().run();
                    }
                    logger.info("任务执行成功: {}", task.taskId());
                    Optional.ofNullable(task.onSuccessful())
                            .ifPresent(consumerExceptionWrapper(
                                    eventNotifier -> eventNotifier.onEvent(messageEvent(task))));
                    success = true;
                } catch (TimeoutException e) {
                    logger.info(
                            "任务执行超时. Time: {}ms, Task: {}", task.timeoutUnit().toMillis(task.timeout()), task.taskId());
                    Optional.ofNullable(task.onTimeout())
                            .ifPresent(consumerExceptionWrapper(
                                    eventNotifier -> eventNotifier.onEvent(messageEvent(task))));
                } catch (Exception e) {
                    if (e instanceof InterruptedException) {
                        logger.info("任务被中断，task: {}", task.taskId());
                        Optional.ofNullable(task.onInterrupted())
                                .ifPresent(consumerExceptionWrapper(
                                        eventNotifier -> eventNotifier.onEvent(messageEvent(task))));
                        return;
                    }
                    logger.error("任务执行期间发生异常：{}", task.taskId(), e);
                    Optional.ofNullable(task.onException())
                            .ifPresent(consumerExceptionWrapper(eventNotifier ->
                                    eventNotifier.onEvent(NsqTask.TaskEvent.<NsqTask.ExceptionInfo<T>>builder()
                                            .taskName(task.taskName())
                                            .data(NsqTask.ExceptionInfo.<T>builder()
                                                    .exception(e)
                                                    .data(task.data())
                                                    .build())
                                            .build())));
                } finally {
                    long endTime = System.currentTimeMillis();
                    logger.info("任务执行完成(是否成功: {}), 耗时[{}ms]：{}", success, endTime - startTime, task.taskId());
                    finalCURRENT_TASK_MAP.remove(task.taskId());
                    Optional.ofNullable(task.onCompleted())
                            .ifPresent(consumerExceptionWrapper(
                                    eventNotifier -> eventNotifier.onEvent(messageEvent(task))));
                }
            });

            CURRENT_TASK_MAP.put(task.taskId(), (NsqTask<Object>) task);
        } catch (RejectedExecutionException rejectedExecutionException) {
            logger.info("队列已满，[{}]任务被拒绝：{}", taskName, task.taskId());
            CURRENT_TASK_MAP.remove(task.taskId());
            Optional.ofNullable(task.onRejected())
                    .ifPresent(consumerExceptionWrapper(eventNotifier -> eventNotifier.onEvent(messageEvent(task))));
        }
    }

    private <T> NsqTask.TaskEvent<T> messageEvent(NsqTask<T> task) {
        return NsqTask.TaskEvent.<T>builder()
                .taskName(task.taskName())
                .data(task.data())
                .build();
    }
}
