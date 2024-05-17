package com.m7.abs.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.admin.feignClient.support.RecordingDumpClient;
import com.m7.abs.admin.mapper.OssUploadFailLogMapper;
import com.m7.abs.admin.service.IOssUploadFailLogService;
import com.m7.abs.common.domain.base.BaseRequest;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.bo.support.RetrySaveToOssBO;
import com.m7.abs.common.domain.entity.OssUploadFailLogEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: abs
 * @description: 录音转存失败管理
 * @author: yx
 * @create: 2021-12-30 14:49
 */
@Service
@Slf4j
public class OssUploadFailLogServiceImpl extends ServiceImpl<OssUploadFailLogMapper, OssUploadFailLogEntity> implements IOssUploadFailLogService {

    @Autowired
    private RecordingDumpClient recordingDumpClient;

    @Override
    public BaseResponse heavyDownload(BaseRequest<List<RetrySaveToOssBO>> request) {
        return recordingDumpClient.saveToOssRetry(request);
    }
}
