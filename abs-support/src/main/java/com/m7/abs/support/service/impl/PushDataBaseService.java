package com.m7.abs.support.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.base.MoreObjects;
import com.m7.abs.common.annotation.NsqListener;
import com.m7.abs.common.constant.error.code.ErrorCodeConstant;
import com.m7.abs.common.constant.keys.CommonSessionKeys;
import com.m7.abs.common.domain.base.BaseRequest;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.bo.support.PushDataBO;
import com.m7.abs.common.domain.bo.support.RetryPushDataBO;
import com.m7.abs.common.domain.entity.DispatcherFailLog;
import com.m7.abs.common.domain.vo.support.PushDataVO;
import com.m7.abs.common.domain.vo.support.RetryPushDataVO;
import com.m7.abs.common.properties.nsq.NsqProperties;
import com.m7.abs.common.utils.FastJsonUtils;
import com.m7.abs.support.common.constant.AbsSupportProperties;
import com.m7.abs.support.common.constant.CommonConstant;
import com.m7.abs.support.common.enums.TaskExecuteResultEnum;
import com.m7.abs.support.common.util.OkHttpClientInstance;
import com.m7.abs.support.core.exception.PushDataFailedException;
import com.m7.abs.support.core.nsq.MessageHolder;
import com.m7.abs.support.core.nsq.NsqTaskExecutor;
import com.m7.abs.support.domain.NsqTask;
import com.m7.abs.support.domain.msg.PushDataMsg;
import com.m7.abs.support.mapper.DispatcherFailLogMapper;
import com.sproutsocial.nsq.Message;
import com.sproutsocial.nsq.Publisher;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.slf4j.MDC;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author zhuhf
 */
@Slf4j
@Service
public class PushDataBaseService extends NsqTaskBaseService {

    private final DispatcherFailLogMapper dispatcherFailLogMapper;
    private final Publisher publisher;

    public PushDataBaseService(
            DispatcherFailLogMapper dispatcherFailLogMapper,
            Publisher publisher,
            NsqTaskExecutor nsqTaskExecutor,
            JsonComponent jsonComponent,
            NsqProperties nsqProperties,
            AbsSupportProperties absSupportProperties) {
        super(
                nsqTaskExecutor,
                jsonComponent,
                absSupportProperties,
                nsqProperties,
                absSupportProperties.getPushData().getRetryIntervalTimeDeviation());
        this.dispatcherFailLogMapper = dispatcherFailLogMapper;
        this.publisher = publisher;
    }

    public BaseResponse<PushDataVO> pushData(BaseRequest<PushDataBO> pushDataReq) {
        PushDataBO param = pushDataReq.getParam();
        PushDataMsg msg = new PushDataMsg();
        msg.setTaskId(String.valueOf(snowflakeIdWorker.nextId()));
        msg.setRequestId(pushDataReq.getRequestId());
        BeanUtils.copyProperties(param, msg);
        if (msg.getRetryInterval() == null) {
            msg.setRetryInterval(CommonConstant.DEFAULT_RETRY_INTERVAL_SECOND);
        }
        byte[] data = jsonComponent.writeValueAsBytes(msg);
        String topic = absSupportProperties.getPushData().getTopic();
        if (param.getSendTime() == null) {
            publisher.publish(topic, data);
        } else {
            long delay = new Date(param.getSendTime()).getTime() - System.currentTimeMillis();
            if (delay > 0) {
                publisher.publishDeferred(
                        topic,
                        data,
                        delay,
                        TimeUnit.MILLISECONDS);
            } else {
                publisher.publish(topic, data);
            }
        }
        return BaseResponse.success(PushDataVO.builder().taskId(msg.getTaskId()).build());
    }

    public BaseResponse<RetryPushDataVO> retrySubmitTask(List<RetryPushDataBO> bo) {
        String topic = absSupportProperties.getPushData().getTopic();
        List<String> successfulOrExecutingTask = new ArrayList<>(bo.size());
        List<String> retrySubmitTask = new ArrayList<>(bo.size());
        for (RetryPushDataBO saveToOssBO : bo) {
            DispatcherFailLog log = dispatcherFailLogMapper.selectById(saveToOssBO.getTaskId());
            if (log == null) {
                // 正在第一次执行中或者第一次执行就成功了
                successfulOrExecutingTask.add(saveToOssBO.getTaskId());
            } else {
                if (TaskExecuteResultEnum.SUCCESS.getCode().equals(log.getResult())) {
                    // 任务已执行成功
                    successfulOrExecutingTask.add(saveToOssBO.getTaskId());
                } else {
                    // 正在执行中
                    if (log.getRetryTimes() < log.getMaxTimes()) {
                        successfulOrExecutingTask.add(saveToOssBO.getTaskId());
                    } else {
                        // 任务达到最大重试次数且执行结果为失败，提交任务重试
                        retrySubmitTask.add(saveToOssBO.getTaskId());
                    }
                }
            }
        }
        try {
            List<PushDataMsg> msgList = bo.stream()
                    .filter(retrySaveToOssBO -> retrySubmitTask.contains(retrySaveToOssBO.getTaskId()))
                    .map(retryPushDataBO -> {
                        DispatcherFailLog log = dispatcherFailLogMapper.selectById(retryPushDataBO.getTaskId());
                        log.setMaxTimes(log.getMaxTimes()
                                + absSupportProperties.getPushData().getMaxRetryTimes());
                        Long sendTime = retryPushDataBO.getSendTime();
                        if (sendTime != null) {
                            log.setNextExecuteTime(new Date(sendTime));
                        }

                        dispatcherFailLogMapper.updateById(log);
                        return PushDataMsg.builder()
                                .taskId(log.getId())
                                .requestId(log.getRequestId())
                                .accountId(log.getAccountId())
                                .projectCode(log.getProjectCode())
                                .url(log.getUrl())
                                .data(log.getData())
                                .method(log.getMethod())
                                .contentType(log.getContentType())
                                .sendTime(sendTime)
                                .retryInterval(MoreObjects.firstNonNull(
                                        retryPushDataBO.getRetryInterval(), log.getIntervalTime()))
                                .build();
                    })
                    .collect(Collectors.toList());

            /**
             * 遍历推送
             */
            msgList.parallelStream()
                    .filter(msg -> msg.getSendTime() != null)
                    .forEach(msg -> {
                        Long sendTime = msg.getSendTime();
                        if (sendTime != null) {
                            //延迟推送
                            long delay = new Date(sendTime).getTime() - System.currentTimeMillis();
                            if (delay > 0) {
                                log.info("延迟推送至NSQ [" + new Date(sendTime) + "] data:{}", FastJsonUtils.toJSONString(msg));
                                publisher.publishDeferred(
                                        topic,
                                        jsonComponent.writeValueAsBytes(msg),
                                        delay,
                                        TimeUnit.MILLISECONDS
                                );
                            } else {
                                //直接推送
                                msg.setSendTime(null);
                                log.info("直接推送至NSQ:{}", FastJsonUtils.toJSONString(msg));
                                publisher.publish(topic, jsonComponent.writeValueAsBytes(msg));
                            }
                        } else {
                            //直接推送
                            log.info("直接推送至NSQ:{}", FastJsonUtils.toJSONString(msg));
                            publisher.publish(topic, jsonComponent.writeValueAsBytes(msg));
                        }
                    });


            return BaseResponse.success(RetryPushDataVO.builder()
                    .retrySubmitTask(retrySubmitTask)
                    .successfulOrExecutingTask(successfulOrExecutingTask)
                    .build());
        } catch (Exception e) {
            log.error("", e);
            return BaseResponse.error(ErrorCodeConstant.CommonErrorCode.SYSTEM_EXCEPTION);
        }
    }

    @NsqListener(
            topic = "#{@absSupportProperties.pushData.topic}",
            channel = "#{@absSupportProperties.pushData.channel}")
    private void subscribePushData(Message message) {
        try {
            byte[] data = message.getData();
            PushDataMsg msg = null;
            try {
                msg = jsonComponent.readValue(data, PushDataMsg.class);
            } catch (Exception e) {
                log.error("无法解析push data数据,退出队列: {}", new String(data), e);
                message.finish();
                return;
            }
            if (msg == null) {
                message.finish();
                return;
            }
            MDC.put(CommonSessionKeys.REQ_ID_KEY, msg.getRequestId());
            log.info("接收到NSQ消息:{}", FastJsonUtils.toJSONString(msg));


            if (msg.getSendTime() != null) {
                Date now = new Date();
                Date sendTime = new Date(msg.getSendTime());
                // 当前时间小于发送时间，就不发送
                if (sendTime.after(now)) {
                    long retryInterval = sendTime.getTime() - now.getTime();
                    if ((int) retryInterval != retryInterval) {
                        retryInterval = Integer.MAX_VALUE;
                    }
                    message.requeue((int) TimeUnit.SECONDS.toMillis(retryInterval));
                    return;
                }
            }

            Long retryInterval = getRetryInterval(msg.getRetryInterval());

            PushDataMsg finalMsg = msg;
            nsqTaskExecutor.submit(NsqTask.<PushDataMsg>builder()
                    .taskId(msg.getTaskId())
                    .requestId(msg.getRequestId())
                    .taskName("pushData")
                    .data(msg)
                    .task(pushDataTask(msg))
                    // 比 nsq msg-timeout 小一分钟
                    .timeout(null)
                    .timeoutUnit(TimeUnit.MILLISECONDS)
                    .onRejected(event -> message.requeue((int) TimeUnit.SECONDS.toMillis(retryInterval)))
                    .onTimeout(failed(retryInterval, msg))
                    .onInterrupted(failed(retryInterval, msg))
                    .onStart(event -> MessageHolder.setMessage(message))
                    .onException(failed(retryInterval, msg))
                    .onSuccessful(event -> {
                        updatePushResult(TaskExecuteResultEnum.SUCCESS, finalMsg);
                        MessageHolder.finish();
                    })
                    .onCompleted(event -> MessageHolder.remove())
                    .build());
        } catch (Exception e) {
            log.error("push data NSQ error", e);
            message.requeue((int) TimeUnit.SECONDS.toMillis(CommonConstant.DEFAULT_RETRY_INTERVAL_SECOND));
        } finally {
            MDC.remove(CommonSessionKeys.REQ_ID_KEY);
        }
    }

    private <T> NsqTask.EventNotifier<T> failed(Long retryInterval, PushDataMsg msg) {
        return event -> {
            DispatcherFailLog dispatcherFailLog = updatePushResult(TaskExecuteResultEnum.FAILED, msg);
            if (dispatcherFailLog != null && dispatcherFailLog.getRetryTimes() >= dispatcherFailLog.getMaxTimes()) {
                log.info("已达最大重试次数[{}]，停止重试执行任务", dispatcherFailLog.getMaxTimes());
                MessageHolder.finish();
                return;
            }
            MessageHolder.requeue(retryInterval.intValue());
        };
    }

    private Runnable pushDataTask(PushDataMsg msg) {
        return () -> {
            MDC.put(CommonSessionKeys.REQ_ID_KEY, msg.getRequestId());
            long begin = System.currentTimeMillis();
            OkHttpClient httpClient = OkHttpClientInstance.getInstance();
            try (Response response = httpClient
                    .newCall(new Request.Builder()
                            .url(msg.getUrl())
                            .method(
                                    msg.getMethod(),
                                    RequestBody.create(MediaType.parse(msg.getContentType()), msg.getData()))
                            .build())
                    .execute()) {
                ResponseBody body = response.body();
                String bodyStr;
                if (body == null) {
                    bodyStr = "";
                } else {
                    bodyStr = body.string();
                }
                if (response.isSuccessful()) {
                    log.info(
                            "服务端响应成功, Response{protocol={}, code={}, message={}, url={}, body={}",
                            response.protocol(),
                            response.code(),
                            response.message(),
                            response.request().url(),
                            bodyStr);

                    JSONObject jsonObject = FastJsonUtils.parseObject(bodyStr);

                    if ("0".equals(jsonObject.getString("code"))) {
                        log.info("服务端返回数据校验成功,推送结束.");
                    } else {
                        throw new PushDataFailedException("校验服务端响应,服务端响应失败.");
                    }


                } else {
                    log.error(
                            "数据推送失败, Response{protocol={}, code={}, message={}, url={}, body={}",
                            response.protocol(),
                            response.code(),
                            response.message(),
                            response.request().url(),
                            bodyStr);
                    throw new PushDataFailedException("推送地址HTTP响应码不等于 200");
                }
            } catch (IOException e) {
                throw new PushDataFailedException(e);
            } finally {
                long end = System.currentTimeMillis();
                log.info("push data 总耗时：{} ms", (end - begin));
            }
        };
    }

    private DispatcherFailLog updatePushResult(TaskExecuteResultEnum resultEnum, PushDataMsg msg) {
        DispatcherFailLog log = dispatcherFailLogMapper.selectById(msg.getTaskId());
        // 第一次执行任务就成功了的，不记录日志
        if (TaskExecuteResultEnum.SUCCESS.equals(resultEnum) && log == null) {
            return null;
        }
        boolean firstExecute = log == null;
        if (firstExecute) {
            log = insertRecord(msg);
        }
        Date now = new Date();
        Date nextExecuteTime = Date.from(LocalDateTime.ofInstant(now.toInstant(), ZoneId.systemDefault())
                .plusNanos(TimeUnit.SECONDS.toNanos(log.getIntervalTime()))
                .atZone(ZoneId.systemDefault())
                .toInstant());
        LambdaUpdateWrapper<DispatcherFailLog> wrapper = Wrappers.lambdaUpdate();
        wrapper.set(DispatcherFailLog::getResult, resultEnum.getCode())
                .set(DispatcherFailLog::getRetryTimes, firstExecute ? 0 : log.getRetryTimes() + 1)
                .set(DispatcherFailLog::getLastUpdateTime, now);
        // 已达最大重试次数，不再重试，下次执行时间置空
        if (log.getRetryTimes() >= log.getMaxTimes()) {
            wrapper.set(DispatcherFailLog::getNextExecuteTime, null);
        } else {
            wrapper.set(DispatcherFailLog::getNextExecuteTime, nextExecuteTime);
        }
        wrapper.eq(DispatcherFailLog::getId, log.getId());
        dispatcherFailLogMapper.update(null, wrapper);
        return dispatcherFailLogMapper.selectById(log.getId());
    }

    private DispatcherFailLog insertRecord(PushDataMsg msg) {
        Date createTime = new Date();
        DispatcherFailLog log = DispatcherFailLog.builder()
                .id(msg.getTaskId())
                .requestId(msg.getRequestId())
                .accountId(msg.getAccountId())
                .projectCode(msg.getProjectCode())
                .url(msg.getUrl())
                .data(msg.getData())
                .method(msg.getMethod())
                .contentType(msg.getContentType())
                .retryTimes(0)
                .maxTimes(absSupportProperties.getPushData().getMaxRetryTimes())
                .createTime(createTime)
                .lastUpdateTime(createTime)
                .intervalTime(msg.getRetryInterval())
                .build();
        dispatcherFailLogMapper.insert(log);
        return log;
    }
}
