package com.m7.abs.support.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.base.MoreObjects;
import com.m7.abs.common.annotation.NsqListener;
import com.m7.abs.common.constant.error.code.ErrorCodeConstant;
import com.m7.abs.common.constant.keys.CommonSessionKeys;
import com.m7.abs.common.domain.base.BaseRequest;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.base.StorageFileBox;
import com.m7.abs.common.domain.bo.support.RecordingDumpBO;
import com.m7.abs.common.domain.bo.support.RetrySaveToOssBO;
import com.m7.abs.common.domain.entity.OssUploadFailLog;
import com.m7.abs.common.domain.vo.support.RecordingDumpVO;
import com.m7.abs.common.domain.vo.support.RetrySaveToOssVO;
import com.m7.abs.common.properties.nsq.NsqProperties;
import com.m7.abs.common.utils.DateUtil;
import com.m7.abs.support.common.constant.AbsSupportProperties;
import com.m7.abs.support.common.constant.CommonConstant;
import com.m7.abs.support.common.enums.TaskExecuteResultEnum;
import com.m7.abs.support.core.exception.SaveToOssFailedException;
import com.m7.abs.support.core.nsq.MessageHolder;
import com.m7.abs.support.core.nsq.NsqTaskExecutor;
import com.m7.abs.support.core.storage.MixCloudStorage;
import com.m7.abs.support.domain.NsqTask;
import com.m7.abs.support.domain.msg.SaveToOssMsg;
import com.m7.abs.support.mapper.OssUploadFailLogMapper;
import com.sproutsocial.nsq.Message;
import com.sproutsocial.nsq.Publisher;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
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
public class RecordingDumpService extends NsqTaskBaseService {
    private final OssUploadFailLogMapper ossUploadFailLogMapper;
    private final Publisher publisher;
    private final MixCloudStorage mixCloudStorage;
    /**
     * 读取数据超时时间 5 分钟
     */
    private final static int READ_TIME_OUT = 5 * 60 * 1000;
    /**
     * 链接超时时间
     */
    private final static int CONNECT_TIME_OUT = 10 * 1000;

    public RecordingDumpService(
            OssUploadFailLogMapper ossUploadFailLogMapper,
            Publisher publisher,
            NsqTaskExecutor nsqTaskExecutor,
            JsonComponent jsonComponent,
            NsqProperties nsqProperties,
            AbsSupportProperties absSupportProperties,
            MixCloudStorage mixCloudStorage) {
        super(
                nsqTaskExecutor,
                jsonComponent,
                absSupportProperties,
                nsqProperties,
                absSupportProperties.getRecordingDump().getRetryIntervalTimeDeviation());
        this.ossUploadFailLogMapper = ossUploadFailLogMapper;
        this.publisher = publisher;
        this.mixCloudStorage = mixCloudStorage;
    }

    public BaseResponse<RecordingDumpVO> recordingDump(BaseRequest<RecordingDumpBO> request) {
        RecordingDumpBO param = request.getParam();
        String ossKey = splicingOssKey(param);
        List<String> storageIds = param.getStorageIds();


        List<StorageFileBox> allFileHost = mixCloudStorage.getAllFileHost();
        if (storageIds != null) {
            allFileHost = allFileHost.stream().filter(item -> storageIds.contains(item.getStorageId())).collect(Collectors.toList());
        }

        if (StringUtils.hasLength(param.getFileUrl())) {
            SaveToOssMsg msg = new SaveToOssMsg();
            msg.setTaskId(String.valueOf(snowflakeIdWorker.nextId()));
            msg.setRequestId(request.getRequestId());

            msg.setOssKey(ossKey);
            BeanUtils.copyProperties(param, msg);
            if (msg.getRetryInterval() == null) {
                msg.setRetryInterval(CommonConstant.DEFAULT_RETRY_INTERVAL_SECOND);
            }
            byte[] data = jsonComponent.writeValueAsBytes(msg);

            publisher.publish(absSupportProperties.getRecordingDump().getTopic(), data);
            return BaseResponse.success(RecordingDumpVO.builder()
                    .storageConf(allFileHost)
                    .filePath(File.separator + ossKey)
                    .taskId(msg.getTaskId())
                    .build());
        }
        return BaseResponse.success(RecordingDumpVO.builder()
                .storageConf(allFileHost)
                .filePath(File.separator + ossKey)
                .build());
    }

    public BaseResponse<RetrySaveToOssVO> retrySubmitTask(List<RetrySaveToOssBO> bo) {
        List<String> successfulOrExecutingTask = new ArrayList<>(bo.size());
        List<String> retrySubmitTask = new ArrayList<>(bo.size());
        for (RetrySaveToOssBO saveToOssBO : bo) {
            OssUploadFailLog log = ossUploadFailLogMapper.selectById(saveToOssBO.getTaskId());
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
        String topic = absSupportProperties.getRecordingDump().getTopic();
        try {
            List<byte[]> data = bo.stream()
                    .filter(retrySaveToOssBO -> retrySubmitTask.contains(retrySaveToOssBO.getTaskId()))
                    .map(retryPushDataBO -> {
                        OssUploadFailLog log = ossUploadFailLogMapper.selectById(retryPushDataBO.getTaskId());
                        log.setMaxTimes(log.getMaxTimes()
                                + absSupportProperties.getRecordingDump().getMaxRetryTimes());
                        ossUploadFailLogMapper.updateById(log);

                        SaveToOssMsg msg = SaveToOssMsg.builder()
                                .taskId(log.getId())
                                .requestId(log.getRequestId())
                                .accountId(log.getAccountId())
                                .projectCode(log.getProjectCode())
                                .fileUrl(log.getFileUrl())
                                .fileName(log.getFileName())
                                .fileSuffix(log.getFileSuffix())
                                .ossKey(log.getOssKey())
                                .retryInterval(MoreObjects.firstNonNull(
                                        retryPushDataBO.getRetryInterval(), log.getIntervalTime()))
                                .build();
                        return jsonComponent.writeValueAsBytes(msg);
                    })
                    .collect(Collectors.toList());
            if (data.isEmpty()) {
                return BaseResponse.success(RetrySaveToOssVO.builder()
                        .retrySubmitTask(retrySubmitTask)
                        .successfulOrExecutingTask(successfulOrExecutingTask)
                        .build());
            }
            publisher.publish(topic, data);
            return BaseResponse.success(RetrySaveToOssVO.builder()
                    .retrySubmitTask(retrySubmitTask)
                    .successfulOrExecutingTask(successfulOrExecutingTask)
                    .build());
        } catch (Exception e) {
            log.error("", e);
            return BaseResponse.error(ErrorCodeConstant.CommonErrorCode.SYSTEM_EXCEPTION);
        }
    }

    @NsqListener(
            topic = "#{@absSupportProperties.recordingDump.topic}",
            channel = "#{@absSupportProperties.recordingDump.channel}")
    protected final void recordingDump(Message message) {
        try {

            byte[] data = message.getData();
            SaveToOssMsg msg = null;
            try {
                msg = jsonComponent.readValue(data, SaveToOssMsg.class);
            } catch (Exception e) {
                log.error("无法解析recordingDump数据,退出队列: {}", new String(data), e);
                return;
            }

            if (msg == null) {
                return;
            }
            MDC.put(CommonSessionKeys.REQ_ID_KEY, msg.getRequestId());
            log.info("接收到文件转存消息: {}", new String(data));


            Long retryInterval = getRetryInterval(msg.getRetryInterval());

            SaveToOssMsg finalMsg = msg;
            nsqTaskExecutor.submit(NsqTask.<SaveToOssMsg>builder()
                    .taskId(msg.getTaskId())
                    .requestId(msg.getRequestId())
                    .taskName("saveToOss")
                    .data(msg)
                    .task(saveToOssTask(msg))
                    // 比 nsq msg-timeout 小一分钟
                    .timeout(
                            (nsqProperties.getConfig().getMsgTimeout() == null
                                    ? 5 * 60 * 1000L
                                    : (long) nsqProperties.getConfig().getMsgTimeout() - 1000 * 60L))
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
            log.error("recordingDump error", e);
            message.requeue((int) TimeUnit.SECONDS.toMillis(CommonConstant.DEFAULT_RETRY_INTERVAL_SECOND));
        }
    }

    private <T> NsqTask.EventNotifier<T> failed(Long retryInterval, SaveToOssMsg msg) {
        return event -> {
            OssUploadFailLog ossUploadFailLog = updatePushResult(TaskExecuteResultEnum.FAILED, msg);
            if (ossUploadFailLog != null && ossUploadFailLog.getRetryTimes() >= ossUploadFailLog.getMaxTimes()) {
                log.info("任务 {} 已达最大重试次数[{}]，停止重试执行任务", msg.getTaskId(), ossUploadFailLog.getMaxTimes());
                MessageHolder.finish();
                return;
            }
            MessageHolder.requeue(retryInterval.intValue());
        };
    }

    private Runnable saveToOssTask(SaveToOssMsg msg) {
        return () -> {
            MDC.put(CommonSessionKeys.REQ_ID_KEY, msg.getRequestId());
            long start = System.currentTimeMillis();
            try {
                mixCloudStorage.multipartUploadFile(msg.getFileUrl(), msg.getOssKey(), msg.getStorageIds());
            } catch (Exception e) {
                log.error("任务 {} 转存文件失败", msg.getTaskId(), e);
                throw new SaveToOssFailedException(e);
            } finally {
                long end = System.currentTimeMillis();
                log.info("转存文件耗时:{}ms", end - start);
                MDC.remove(CommonSessionKeys.REQ_ID_KEY);
            }
        };
    }


    private String splicingOssKey(RecordingDumpBO param) {
        StringBuilder sb = new StringBuilder();
        sb.append(param.getProjectCode())
                .append("/")
                .append(param.getAccountId())
                .append("/")
                .append(DateUtil.parseDateToStr(new Date(param.getFileTime()), DateUtil.DATE_FORMAT_YYYY_MM_DD))
                .append("/");

        if (param.getFileName() != null) {
            sb.append(param.getFileName());
            return sb.toString();
        }
        if (StringUtils.isEmpty(param.getFileUrl())) {
            log.error("fileName 和 FileUrl 都为空");
            throw new IllegalArgumentException("fileName 和 FileUrl 不能都为空");
        }
        String[] tmp = param.getFileUrl().split("/");
        String urlFileName = tmp[tmp.length - 1];
        if (param.getFileSuffix() != null) {
            sb.append(urlFileName).append(".").append(param.getFileSuffix());
            return sb.toString();
        }
        sb.append(urlFileName);
        return sb.toString();
    }

    private OssUploadFailLog updatePushResult(TaskExecuteResultEnum resultEnum, SaveToOssMsg msg) {

        OssUploadFailLog log = ossUploadFailLogMapper.selectById(msg.getTaskId());
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
        LambdaUpdateWrapper<OssUploadFailLog> wrapper = Wrappers.lambdaUpdate();
        wrapper.set(OssUploadFailLog::getResult, resultEnum.getCode())
                .set(OssUploadFailLog::getRetryTimes, firstExecute ? 0 : log.getRetryTimes() + 1)
                .set(OssUploadFailLog::getLastUpdateTime, now);
        // 已达最大重试次数，不再重试，下次执行时间置空
        if (log.getRetryTimes() >= log.getMaxTimes()) {
            wrapper.set(OssUploadFailLog::getNextExecuteTime, null);
        } else {
            wrapper.set(OssUploadFailLog::getNextExecuteTime, nextExecuteTime);
        }
        ossUploadFailLogMapper.update(null, wrapper.eq(OssUploadFailLog::getId, log.getId()));
        return ossUploadFailLogMapper.selectById(log.getId());
    }

    private OssUploadFailLog insertRecord(SaveToOssMsg msg) {
        Date createTime = new Date();
        OssUploadFailLog log = OssUploadFailLog.builder()
                .id(msg.getTaskId())
                .requestId(msg.getRequestId())
                .accountId(msg.getAccountId())
                .projectCode(msg.getProjectCode())
                .fileUrl(msg.getFileUrl())
                .fileName(msg.getFileName())
                .fileSuffix(msg.getFileSuffix())
                .ossKey(msg.getOssKey())
                .retryTimes(0)
                .maxTimes(absSupportProperties.getPushData().getMaxRetryTimes())
                .createTime(createTime)
                .lastUpdateTime(createTime)
                .intervalTime(msg.getRetryInterval())
                .build();
        ossUploadFailLogMapper.insert(log);
        return log;
    }

}
