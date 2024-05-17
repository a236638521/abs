package com.m7.abs.api.projectHandler.midNum;

import com.m7.abs.api.domain.dto.midNum.MidMumConfigDto;
import com.m7.abs.api.domain.dto.midNum.MidNumRecordRespDto;
import com.m7.abs.api.domain.dto.midNum.MidNumSmsRecordRespDto;
import com.m7.abs.api.domain.vo.midNum.*;
import com.m7.abs.common.domain.base.BaseResponse;

import java.util.List;
import java.util.Map;

public interface IMidNumHandler {
    /**
     * AXB绑定
     *
     * @param midMumConfigDto
     * @param requestVO
     * @return
     */
    BaseResponse<BindResponseVO> axbBind(MidMumConfigDto midMumConfigDto, BindAXBRequestVO requestVO);

    /**
     * AX绑定
     *
     * @param midMumConfigDto
     * @param requestVO
     * @return
     */
    BaseResponse<BindResponseVO> axBind(MidMumConfigDto midMumConfigDto, BindAXRequestVO requestVO);

    /**
     * AXB解绑
     *
     * @param accountDto
     * @param requestVO
     * @return
     */
    BaseResponse unBindAXB(MidMumConfigDto accountDto, UnBindRequestVO requestVO);

    /**
     * AX解绑
     *
     * @param accountDto
     * @param requestVO
     * @return
     */
    BaseResponse axUnBind(MidMumConfigDto accountDto, UnBindRequestVO requestVO);

    /**
     * 话单参数转换
     *
     * @param requestVO
     * @return
     */
    BaseResponse<MidNumRecordRespDto> translateRecord(Map<String, Object> requestVO);

    /**
     * 通话录音参数转换
     * 有些通道,话单和录音是分两次推送的,因此录音地址需要单独处理
     *
     * @param requestVO
     * @return
     */
    BaseResponse<MidNumRecordRespDto> translateRecordUrl(Map<String, Object> requestVO);

    /**
     * 处理通话振铃时间事件数据
     *
     * @param requestVO
     * @return
     */
    BaseResponse<MidNumRecordRespDto> translateRingTime(Map<String, Object> requestVO);

    /**
     * AX延长绑定
     *
     * @param midMumConfigDto
     * @param requestVO
     * @return
     */
    BaseResponse<BindResponseVO> axDelay(MidMumConfigDto midMumConfigDto, DelayAxRequestVO requestVO);

    /**
     * AXB延长绑定
     *
     * @param midMumConfigDto
     * @param requestVO
     * @return
     */
    BaseResponse<BindResponseVO> axbDelay(MidMumConfigDto midMumConfigDto, DelayAXBRequestVO requestVO);

    /**
     * gxb绑定
     *
     * @param midMumConfigDto
     * @param requestVO
     * @return
     */
    BaseResponse<BindResponseVO> gxbBind(MidMumConfigDto midMumConfigDto, BindGXBRequestVO requestVO);

    /**
     * gxb解绑
     *
     * @param midMumConfigDto
     * @param requestVO
     * @return
     */
    BaseResponse<BindResponseVO> gxbUnBind(MidMumConfigDto midMumConfigDto, UnBindRequestVO requestVO);

    /**
     * 新建号码组
     *
     * @param midMumConfigDto
     * @return
     */
    BaseResponse groupInsert(MidMumConfigDto midMumConfigDto);

    /**
     * 清空号码组
     *
     * @param midMumConfigDto
     * @param requestVO
     * @return
     */
    BaseResponse groupDel(MidMumConfigDto midMumConfigDto, GroupDelRequestVO requestVO);

    /**
     * 号码组新增号码
     *
     * @param midMumConfigDto
     * @param requestVO
     * @param oldNumber
     * @return
     */
    BaseResponse groupAddNumbers(MidMumConfigDto midMumConfigDto, GroupAddNumbersRequestVO requestVO, List<String> oldNumber);

    /**
     * 号码组删除号码
     *
     * @param midMumConfigDto
     * @param requestVO
     * @param oldNumber
     * @return
     */
    BaseResponse groupDelNumbers(MidMumConfigDto midMumConfigDto, GroupDelNumbersRequestVO requestVO, List<String> oldNumber);



    /**
     * 短信话单参数转换
     * @param requestVO
     * @return
     */
    BaseResponse<MidNumSmsRecordRespDto> translateSmsRecord(Map<String, Object> requestVO);
}
