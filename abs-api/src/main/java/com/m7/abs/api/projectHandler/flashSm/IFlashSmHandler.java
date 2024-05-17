package com.m7.abs.api.projectHandler.flashSm;

import com.m7.abs.api.domain.dto.flashSm.FlashSmConfigDto;
import com.m7.abs.api.domain.dto.flashSm.FlashSmRecordRespDto;
import com.m7.abs.api.domain.vo.flashSm.DeliverRequestVO;
import com.m7.abs.api.domain.vo.flashSm.DeliverResponseVO;
import com.m7.abs.common.domain.base.BaseResponse;

import java.util.Map;

public interface IFlashSmHandler {
    /**
     * 发送闪信
     *
     * @param configDto
     * @param requestVO
     * @return
     */
    BaseResponse<DeliverResponseVO> deliver(FlashSmConfigDto configDto, DeliverRequestVO requestVO);

    /**
     * 转译不同渠道返回的闪信发送结果
     * @param requestVO
     * @return
     */
    BaseResponse<FlashSmRecordRespDto> translateRecord(Map<String, Object> requestVO);
}
