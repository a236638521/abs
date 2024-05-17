package com.m7.abs.reportexport;

import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.vo.reportexport.MidNumCdrExportRequestVO;
import com.m7.abs.common.domain.vo.reportexport.MidNumCdrExportResponseVO;
import com.m7.abs.common.utils.MyStringUtils;
import com.m7.abs.reportexport.domain.dto.ExportChannelDTO;
import com.m7.abs.reportexport.service.ICdrBatchExportService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

/**
 * @author Kejie Peng
 * @date 2023年 04月18日 11:39:50
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExportTest {
    @Autowired
    private ICdrBatchExportService cdrBatchExportService;

    @Test
    public void test() {
        MidNumCdrExportRequestVO requestVO= MidNumCdrExportRequestVO.builder()
                .taskId(MyStringUtils.randomUUID())
                .accountId("1641022657340379138")
                .startTime("2023-01-01 00:00:00")
                .endTime("2023-04-17 17:57:57")
                .type("csv")
                .build();

        BaseResponse<MidNumCdrExportResponseVO> checkResult = cdrBatchExportService.checkRequestVO(requestVO);
        if (!checkResult.isSuccess()) {
            return;
        }
        MidNumCdrExportResponseVO checkResultData = checkResult.getData();
        BaseResponse<MidNumCdrExportResponseVO> response = cdrBatchExportService.getExpectInfo(requestVO);
        if (!response.isSuccess()) {
            return;
        }

        MidNumCdrExportResponseVO data = response.getData();

        ExportChannelDTO.StorageInfo storageInfo = new ExportChannelDTO.StorageInfo();
        storageInfo.setKey(data.getKey());
        storageInfo.setZipFileName(data.getZipFileName());

        ExportChannelDTO channelDTO = ExportChannelDTO.builder()
                .requestVO(requestVO)
                .storage(storageInfo)
                .build();
        cdrBatchExportService.batchExport(channelDTO);
    }
}
