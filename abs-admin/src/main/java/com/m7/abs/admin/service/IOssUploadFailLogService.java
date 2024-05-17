package com.m7.abs.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.m7.abs.common.domain.base.BaseRequest;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.bo.support.RetrySaveToOssBO;
import com.m7.abs.common.domain.entity.OssUploadFailLogEntity;

import java.util.List;

public interface IOssUploadFailLogService extends IService<OssUploadFailLogEntity> {
    BaseResponse heavyDownload(BaseRequest<List<RetrySaveToOssBO>> request);
}
