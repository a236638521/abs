package com.m7.abs.reportexport.service;

import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.vo.reportexport.MidNumCdrExportRequestVO;
import com.m7.abs.common.domain.vo.reportexport.MidNumCdrExportResponseVO;
import com.m7.abs.reportexport.domain.dto.ExportChannelDTO;

/**
 * 话单批量导出
 *
 * @author Kejie Peng
 * @date 2023年 03月20日 13:46:34
 */
public interface ICdrBatchExportService {
    /**
     * 批量导出
     * @param channelDTO
     */
    void batchExport(ExportChannelDTO channelDTO);

    /**
     * 获取预期值
     * @param requestVO
     * @return
     */
    BaseResponse<MidNumCdrExportResponseVO> getExpectInfo(MidNumCdrExportRequestVO requestVO);

    /**
     * 计算预计下载量
     * @param requestVO
     */
    void sumExpectCount(MidNumCdrExportRequestVO requestVO);

    /**
     * 预检RequestVO值
     * @param requestVO
     * @return
     */
    BaseResponse<MidNumCdrExportResponseVO> checkRequestVO(MidNumCdrExportRequestVO requestVO);
}
