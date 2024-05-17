package com.m7.abs.reportexport.controller;

import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.MiddleNumberCdrReportEntity;
import com.m7.abs.common.domain.vo.reportexport.MidNumCdrExportRequestVO;
import com.m7.abs.common.domain.vo.reportexport.MidNumCdrExportResponseVO;
import com.m7.abs.common.utils.DateUtil;
import com.m7.abs.reportexport.common.properties.AbsReportProperties;
import com.m7.abs.reportexport.domain.dto.ExportChannelDTO;
import com.m7.abs.reportexport.service.ICdrBatchExportService;
import com.m7.abs.reportexport.service.IMiddleNumberCdrReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Kejie Peng
 */
@Slf4j
@RestController
@RequestMapping("/midNum")
public class MidNumCdrController {
    @Autowired
    private ICdrBatchExportService cdrBatchExportService;
    @Autowired
    private AbsReportProperties absReportProperties;

    @WebAspect(injectReqId = true, logDesc = "api->cdr->export")
    @PostMapping(value = "/export/excel")
    public BaseResponse<MidNumCdrExportResponseVO> export(@Valid @RequestBody MidNumCdrExportRequestVO requestVO) {
        BaseResponse<MidNumCdrExportResponseVO> checkResult = cdrBatchExportService.checkRequestVO(requestVO);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        MidNumCdrExportResponseVO checkResultData = checkResult.getData();
        BaseResponse<MidNumCdrExportResponseVO> response = cdrBatchExportService.getExpectInfo(requestVO);
        if (!response.isSuccess()) {
            return response;
        }
        requestVO.setBillAccountId(checkResultData.getBillAccountId());
        cdrBatchExportService.sumExpectCount(requestVO);

        MidNumCdrExportResponseVO data = response.getData();

        ExportChannelDTO.StorageInfo storageInfo = new ExportChannelDTO.StorageInfo();
        storageInfo.setKey(data.getKey());
        storageInfo.setZipFileName(data.getZipFileName());

        ExportChannelDTO channelDTO = ExportChannelDTO.builder()
                .requestVO(requestVO)
                .storage(storageInfo)
                .count(0L)
                .recordCount(0L)
                .eachFileRecordCount(absReportProperties.getExportExcel().getEachFileMaxSize())
                .newFileCount(1L)
                .build();
        cdrBatchExportService.batchExport(channelDTO);
        return BaseResponse.success(data);
    }

}
