package com.m7.abs.support.core.nsq;

import com.m7.abs.support.common.constant.CommonConstant;
import com.sproutsocial.nsq.Message;
import java.util.concurrent.TimeUnit;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

/**
 * 方便消息完成或重排队
 *
 * @author zhuhf
 */
@Slf4j
public class MessageHolder {
    /**
     * 消息 requeue 毫秒数
     */
    public static final int MSG_REQUEUE_MILLS =
            (int) TimeUnit.SECONDS.toMillis(CommonConstant.DEFAULT_RETRY_INTERVAL_SECOND);

    private static final ThreadLocal<WrapperMessage> WRAPPER_MESSAGE_THREAD_LOCAL = ThreadLocal.withInitial(() -> null);

    public static void setMessage(Message message) {
        WRAPPER_MESSAGE_THREAD_LOCAL.set(WrapperMessage.builder().msg(message).build());
    }

    public static void remove() {
        WRAPPER_MESSAGE_THREAD_LOCAL.remove();
    }

    public static void finish() {
        WrapperMessage message = WRAPPER_MESSAGE_THREAD_LOCAL.get();
        if (message.finished || message.requeued) {
            return;
        }
        message.msg.finish();
        message.finished = true;
    }

    public static void requeue(int delaySeconds) {
        WrapperMessage message = WRAPPER_MESSAGE_THREAD_LOCAL.get();
        if (message.requeued || message.finished) {
            return;
        }
        log.info(
                "消息 [{}] 重排队，重排队时间: {}s",
                new String(WRAPPER_MESSAGE_THREAD_LOCAL.get().msg.getData()),
                delaySeconds);
        message.msg.requeue((int) TimeUnit.SECONDS.toMillis(delaySeconds));
        message.requeued = true;
    }

    public static void requeue() {
        WrapperMessage message = WRAPPER_MESSAGE_THREAD_LOCAL.get();
        if (message.requeued) {
            return;
        }
        log.info(
                "消息 [{}] 重排队，重排队时间: {}s",
                new String(WRAPPER_MESSAGE_THREAD_LOCAL.get().msg.getData()),
                TimeUnit.MILLISECONDS.toSeconds(MSG_REQUEUE_MILLS));
        message.msg.requeue(MSG_REQUEUE_MILLS);
        message.requeued = true;
    }

    @Builder
    static class WrapperMessage {
        private Message msg;
        private boolean finished;
        private boolean requeued;
    }
}
