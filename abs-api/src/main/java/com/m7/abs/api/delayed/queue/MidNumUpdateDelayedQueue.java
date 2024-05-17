package com.m7.abs.api.delayed.queue;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.m7.abs.api.delayed.bean.RetryBean;
import com.m7.abs.api.delayed.bean.UpdateMidNumCdrOssIdBean;
import com.m7.abs.api.service.IMiddleNumberCdrService;
import com.m7.abs.common.constant.keys.CommonSessionKeys;
import com.m7.abs.common.constant.keys.RedisKeyConstant;
import com.m7.abs.common.domain.entity.MiddleNumberCdrEntity;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Kejie Peng
 * @date 2023i 04月14日 09:55:10
 */
@Slf4j
@Component
public class MidNumUpdateDelayedQueue implements ApplicationListener<ApplicationStartedEvent>, Runnable {
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private IMiddleNumberCdrService middleNumberCdrService;
    private RBlockingDeque<RetryBean> updateQueue;
    private RDelayedQueue<RetryBean> delayedQueue;
    private final static String BLOCK_QUEUE_NAME = "mid-num-update-ossId";
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors() * 2;
    private static final int QUEUE_SIZE = 5000;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        initQueue();
        initThread();
    }

    private void initQueue() {
        updateQueue = redissonClient.getBlockingDeque(RedisKeyConstant.MidNum.getDelayedQueueKeyFormatNoAccount(BLOCK_QUEUE_NAME));
        delayedQueue = redissonClient.getDelayedQueue(updateQueue);
    }

    private void initThread(){
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                THREAD_COUNT,
                THREAD_COUNT,
                30L,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(QUEUE_SIZE),
                new ThreadFactoryBuilder().setNameFormat(BLOCK_QUEUE_NAME+"-%d").build(),
                new ThreadPoolExecutor.AbortPolicy());

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(this);
        }

    }

    /**
     * 添加消息至延迟队列
     *
     * @param data
     * @param delay
     * @param timeUnit
     */
    public void addDelayedMsg(RetryBean data, long delay, TimeUnit timeUnit) {
        delayedQueue.offer(data, delay, timeUnit);
    }


    @Override
    public void run() {
        while (true) {
            try {
                RetryBean retryBean = updateQueue.take();
                if (retryBean != null) {
                    MDC.put(CommonSessionKeys.REQ_ID_KEY, retryBean.getTraceId());
                    int currentCount = retryBean.getCurrentCount();
                    log.info("第{}次执行延迟任务:{}", currentCount, Thread.currentThread().getName());
                    this.updateCdrOSSTaskId(retryBean);
                }
            } catch (Exception e) {
                log.error("延迟任务执行失败", e);
            } finally {
                MDC.remove(CommonSessionKeys.REQ_ID_KEY);
            }
        }
    }


    private boolean updateCdrOSSTaskId(RetryBean retryBean) {
        if (retryBean == null) {
            return false;
        }

        int currentCount = retryBean.getCurrentCount();
        int maxTime = retryBean.getMaxTime();
        UpdateMidNumCdrOssIdBean data = retryBean.getData();
        String ossTaskId = data.getOssTaskId();
        String channelRecordId = data.getChannelRecordId();
        log.info("update oss task id:" + ossTaskId + ",channelRecordId:" + channelRecordId);
        boolean update = false;
        LambdaQueryWrapper<MiddleNumberCdrEntity> bindLogWrapper = new LambdaQueryWrapper<>();
        bindLogWrapper.eq(MiddleNumberCdrEntity::getChannelRecordId, channelRecordId);
        bindLogWrapper.orderByDesc(MiddleNumberCdrEntity::getCreateTime);
        bindLogWrapper.last("LIMIT 1");
        MiddleNumberCdrEntity middleNumberCdrEntity = middleNumberCdrService.getOne(bindLogWrapper);
        if (middleNumberCdrEntity != null) {
            middleNumberCdrEntity.setOssTaskId(ossTaskId);
            update = middleNumberCdrService.updateById(middleNumberCdrEntity);
            log.info("update oss task result:" + update + ",cdrId:" + middleNumberCdrEntity.getId());
        }

        if (!update) {

            if (currentCount < maxTime) {
                log.info("Update fail,add into retry queue.");
                retryBean.setCurrentCount(++currentCount);
                this.addDelayedMsg(retryBean, 1, TimeUnit.MINUTES);
            } else {
                log.info("Exceeded the maximum number of times, give up.currentCount:{},maxTime:{}", currentCount, maxTime);
            }

        }

        return update;
    }
}
