package com.m7.abs.api.projectHandler.midNum.impl;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.m7.abs.api.domain.dto.midNum.MidMumConfigDto;
import com.m7.abs.api.domain.dto.midNum.MidNumRecordRespDto;
import com.m7.abs.api.domain.dto.midNum.MidNumSmsRecordRespDto;
import com.m7.abs.api.domain.dto.midNum.MidNumTranslateDto;
import com.m7.abs.api.domain.vo.midNum.*;
import com.m7.abs.api.projectHandler.midNum.IMidNumHandler;
import com.m7.abs.api.projectHandler.midNum.MIdNumType;
import com.m7.abs.api.projectHandler.midNum.model.lianlian.*;
import com.m7.abs.common.constant.common.MidNumCallStatusEnum;
import com.m7.abs.common.constant.common.MidNumType;
import com.m7.abs.common.constant.error.code.ErrorCodeConstant;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.protocol.HTTP;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 运营商：联联小号
 * 对接人：彭科杰
 * 对接时间：2022年01月08日
 */
@Slf4j
@MIdNumType(channel = MidNumType.LIAN_LIAN)
public class MidNumHandlerLianLian implements IMidNumHandler {

    @Override
    public BaseResponse<BindResponseVO> axbBind(MidMumConfigDto midMumConfigDto, BindAXBRequestVO requestVO) {
        try {
            String url = midMumConfigDto.getBasePath() + "/number/axb/binding";

            String requestId = MyStringUtils.randomUUID();
            String ts = String.valueOf(new Date().getTime() / 1000);


            LianLianAXBReq axbReqLianLian = LianLianAXBReq.builder()
                    .corp_key(midMumConfigDto.getAccessKey())
                    .ts(ts)
                    .request_id(requestId)
                    .expiration(requestVO.getExpiration())
                    .tel_a(requestVO.getTelA())
                    .tel_b(requestVO.getTelB())
                    .tel_x(requestVO.getTelX()).build();
            Integer icDisplayFlag = requestVO.getIcDisplayFlag();
            if (icDisplayFlag != null && icDisplayFlag.equals(0)) {
                axbReqLianLian.setA_call_x_showout(requestVO.getTelA());
            }

            Map<String, String> headers = new HashedMap();
            headers.put("merchantNo", midMumConfigDto.getAccessKey());
            headers.put("ts", ts);

            String resultStr = sendRequest(midMumConfigDto, url, axbReqLianLian, headers);
            if (StringUtils.isNotEmpty(resultStr)) {
                LianLianResp<LianLianAXBRespData> result = FastJsonUtils.toBean(resultStr, new TypeReference<LianLianResp<LianLianAXBRespData>>() {
                });
                Integer responseCode = result.getCode();
                if (responseCode != null && responseCode.equals(0)) {
                    BindResponseVO bindResponseVO = new BindResponseVO();
                    bindResponseVO.setMappingId(result.getData().getBind_id());
                    return BaseResponse.success(bindResponseVO);
                } else {
                    return BaseResponse.fail(result.getMessage());
                }
            }
            return BaseResponse.fail("[" + MidNumType.LIAN_LIAN + "] BindAXB FAIL");
        } catch (Exception e) {
            log.error("BindAXB Error", e);
            return BaseResponse.error("BindAXB Error");
        }
    }

    @Override
    public BaseResponse<BindResponseVO> axBind(MidMumConfigDto midMumConfigDto, BindAXRequestVO requestVO) {
        try {
            String url = midMumConfigDto.getBasePath() + "/number/ax2/binding";

            String requestId = MyStringUtils.randomUUID();
            String ts = String.valueOf(new Date().getTime() / 1000);

            Long expiration = requestVO.getExpiration();
            if (expiration == null) {
                expiration = 60L;
            }

            LianLianXBReq axbReqLianLian = LianLianXBReq.builder()
                    .corp_key(midMumConfigDto.getAccessKey())
                    .ts(ts)
                    .request_id(requestId)
                    .expiration(expiration)
                    .model(2)
                    .tel(requestVO.getTelA())
                    .tel_x(requestVO.getTelX()).build();


            Map<String, String> headers = new HashedMap();
            headers.put("merchantNo", midMumConfigDto.getAccessKey());
            headers.put("ts", ts);

            String resultStr = sendRequest(midMumConfigDto, url, axbReqLianLian, headers);
            if (StringUtils.isNotEmpty(resultStr)) {
                LianLianResp<LianLianXBRespData> result = FastJsonUtils.toBean(resultStr, LianLianResp.class);
                Integer responseCode = result.getCode();
                if (responseCode != null && responseCode.equals(0)) {
                    BindResponseVO bindResponseVO = new BindResponseVO();
                    bindResponseVO.setMappingId(result.getData().getBind_id());
                    return BaseResponse.success(bindResponseVO);
                } else {
                    return BaseResponse.fail(result.getMessage());
                }
            }
            return BaseResponse.fail("[" + MidNumType.LIAN_LIAN + "] BindXB FAIL");
        } catch (Exception e) {
            log.error("[" + MidNumType.LIAN_LIAN + "] BindXB Error", e);
            return BaseResponse.error("[" + MidNumType.LIAN_LIAN + "] BindXB Error");
        }
    }

    @Override
    public BaseResponse unBindAXB(MidMumConfigDto midMumConfigDto, UnBindRequestVO requestVO) {
        try {
            String url = midMumConfigDto.getBasePath() + "/number/axb/unbind";

            String ts = String.valueOf(new Date().getTime() / 1000);

            LianLianAXBUnbindReq axbUnbindReq = LianLianAXBUnbindReq.builder()
                    .corp_key(midMumConfigDto.getAccessKey())
                    .ts(ts)
                    .bind_id(requestVO.getMappingId()).build();


            Map<String, String> headers = new HashedMap();
            headers.put("merchantNo", midMumConfigDto.getAccessKey());
            headers.put("ts", ts);

            String resultStr = sendRequest(midMumConfigDto, url, axbUnbindReq, headers);
            if (StringUtils.isNotEmpty(resultStr)) {
                LianLianResp result = FastJsonUtils.toBean(resultStr, LianLianResp.class);
                Integer responseCode = result.getCode();
                if (responseCode != null && responseCode.equals(0)) {
                    return BaseResponse.success();
                } else {
                    return BaseResponse.fail(result.getMessage());
                }
            }
            return BaseResponse.fail("[" + MidNumType.LIAN_LIAN + "] unBindAXB FAIL");
        } catch (Exception e) {
            log.error("[" + MidNumType.LIAN_LIAN + "] unBindAXB Error", e);
            return BaseResponse.error("[" + MidNumType.LIAN_LIAN + "] unBindAXB Error");
        }
    }

    @Override
    public BaseResponse axUnBind(MidMumConfigDto midMumConfigDto, UnBindRequestVO requestVO) {
        try {
            String url = midMumConfigDto.getBasePath() + "/number/ax2/unbind";

            String ts = String.valueOf(new Date().getTime() / 1000);

            LianLianXBUnbindReq xbUnbindReq = LianLianXBUnbindReq.builder()
                    .corp_key(midMumConfigDto.getAccessKey())
                    .ts(ts)
                    .tel_x(requestVO.getTelX())
                    .bind_id(requestVO.getMappingId()).build();

            Map<String, String> headers = new HashedMap();
            headers.put("merchantNo", midMumConfigDto.getAccessKey());
            headers.put("ts", ts);

            String resultStr = sendRequest(midMumConfigDto, url, xbUnbindReq, headers);
            if (StringUtils.isNotEmpty(resultStr)) {
                LianLianResp result = FastJsonUtils.toBean(resultStr, LianLianResp.class);
                Integer responseCode = result.getCode();
                if (responseCode != null && responseCode.equals(0)) {
                    return BaseResponse.success();
                } else {
                    return BaseResponse.fail(result.getMessage());
                }
            }
            return BaseResponse.fail("[" + MidNumType.LIAN_LIAN + "] unBindAx FAIL");
        } catch (Exception e) {
            log.error("[" + MidNumType.LIAN_LIAN + "] unBindAx Error", e);
            return BaseResponse.error("[" + MidNumType.LIAN_LIAN + "] unBindAx Error");
        }
    }

    @Override
    public BaseResponse<BindResponseVO> axDelay(MidMumConfigDto midMumConfigDto, DelayAxRequestVO requestVO) {
        return BaseResponse.error(ErrorCodeConstant.CommonErrorCode.NOT_SUPPORT);
    }

    @Override
    public BaseResponse<BindResponseVO> axbDelay(MidMumConfigDto midMumConfigDto, DelayAXBRequestVO requestVO) {
        return BaseResponse.error(ErrorCodeConstant.CommonErrorCode.NOT_SUPPORT);
    }

    @Override
    public BaseResponse<BindResponseVO> gxbBind(MidMumConfigDto midMumConfigDto, BindGXBRequestVO requestVO) {
        return BaseResponse.error(ErrorCodeConstant.CommonErrorCode.NOT_SUPPORT);
    }

    @Override
    public BaseResponse<BindResponseVO> gxbUnBind(MidMumConfigDto midMumConfigDto, UnBindRequestVO requestVO) {
        return BaseResponse.error(ErrorCodeConstant.CommonErrorCode.NOT_SUPPORT);
    }

    @Override
    public BaseResponse groupInsert(MidMumConfigDto midMumConfigDto) {
        return BaseResponse.error(ErrorCodeConstant.CommonErrorCode.NOT_SUPPORT);
    }

    @Override
    public BaseResponse groupDel(MidMumConfigDto midMumConfigDto, GroupDelRequestVO requestVO) {
        return BaseResponse.error(ErrorCodeConstant.CommonErrorCode.NOT_SUPPORT);
    }

    @Override
    public BaseResponse groupAddNumbers(MidMumConfigDto midMumConfigDto, GroupAddNumbersRequestVO requestVO, List<String> oldNumber) {
        return BaseResponse.error(ErrorCodeConstant.CommonErrorCode.NOT_SUPPORT);
    }

    @Override
    public BaseResponse groupDelNumbers(MidMumConfigDto midMumConfigDto, GroupDelNumbersRequestVO requestVO, List<String> oldNumber) {
        return BaseResponse.error(ErrorCodeConstant.CommonErrorCode.NOT_SUPPORT);
    }

    @Override
    public BaseResponse<MidNumSmsRecordRespDto> translateSmsRecord(Map<String, Object> requestVO) {
        return BaseResponse.error(ErrorCodeConstant.CommonErrorCode.NOT_SUPPORT);
    }

    @Override
    public BaseResponse<MidNumRecordRespDto> translateRecord(Map<String, Object> requestVO) {
        if (requestVO != null) {
            String reqJsonStr = FastJsonUtils.toJSONString(requestVO);
            LianLianRecordReq recordReq = FastJsonUtils.toBean(reqJsonStr, LianLianRecordReq.class);
            String record_file_url = recordReq.getRecord_file_url();

            boolean callRecording = false;
            if (StringUtils.isNotEmpty(record_file_url)) {
                callRecording = true;
            }
            String bill_duration = recordReq.getBill_duration();
            long duration = 0l;
            duration = StringUtils.isNotEmpty(bill_duration) ? Long.valueOf(bill_duration) : 0l;

            MidNumTranslateDto recordDto = MidNumTranslateDto.builder()
                    .channelBindId(recordReq.getBind_id())
                    .channelRecordId(recordReq.getRecorder_id())
                    .caller(recordReq.getCaller())
                    .callee(recordReq.getCalled())
                    .callerShow(recordReq.getCaller_show())
                    .calledShow(recordReq.getCalled_show())
                    .telX(recordReq.getCalled_show())
                    .billDuration(duration)
                    .result(translateCallStatus(duration, recordReq.getCall_result()))
                    .beginTime(DateUtil.parseStrToDate(recordReq.getBegin_time(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS))
                    .alertingTime(DateUtil.parseStrToDate(recordReq.getAlerting_time(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS))
                    .connectTime(DateUtil.parseStrToDate(recordReq.getConnect_time(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS))
                    .releaseTime(DateUtil.parseStrToDate(recordReq.getRelease_time(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS))
                    .callRecording(callRecording)
                    .recordFileUrl(record_file_url)
                    .build();

            Map<String, Object> respMap = new HashMap<>();
            respMap.put("code", 0);
            respMap.put("message", "success");
            MidNumRecordRespDto respDto = MidNumRecordRespDto.builder()
                    .translateDto(recordDto)
                    .respData(respMap)
                    .build();
            return BaseResponse.success(respDto);
        }

        return BaseResponse.fail("translate record fail.");
    }

    private String translateCallStatus(long callDuration, String status) {

        if (StringUtils.isNotEmpty(status)) {
            if (callDuration > 0 || "ANSWERED".equals(status)) {
                return MidNumCallStatusEnum.NORMAL_HANGUP.getValue();
            }

            switch (status) {
                case "BUSY":
                    return MidNumCallStatusEnum.CALLED_BUSY.getValue();
                case "NO_ANSWER":
                    return MidNumCallStatusEnum.CALLED_NO_ANSWER.getValue();
                case "REJECT":
                    return MidNumCallStatusEnum.REFUSE.getValue();
                case "HANGUP":
                    return MidNumCallStatusEnum.CALLER_ABANDONMENT.getValue();
                case "INVALID_NUMBER":
                    return MidNumCallStatusEnum.EMPTY_NUMBER.getValue();
                case "POWER_OFF":
                    return MidNumCallStatusEnum.POWER_OFF.getValue();
                case "UNAVAILABLE":
                    return MidNumCallStatusEnum.CALLED_BUSY.getValue();
                case "SUSPEND":
                    return MidNumCallStatusEnum.OUT_OF_SERVICE.getValue();
            }
        }
        return MidNumCallStatusEnum.CALL_FAIL.getValue();
    }

    @Override
    public BaseResponse<MidNumRecordRespDto> translateRecordUrl(Map<String, Object> requestVO) {
        return null;
    }

    @Override
    public BaseResponse<MidNumRecordRespDto> translateRingTime(Map<String, Object> requestVO) {
        return null;
    }

    /**
     * 发送Http请求
     *
     * @param midMumConfigDto
     * @param url
     * @param data
     * @param headers
     * @return
     */
    private String sendRequest(MidMumConfigDto midMumConfigDto, String url, Object data, Map<String, String> headers) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8");
        headers.put("Accept", "application/json;charset=UTF-8");

        String orderStr = BeanUtil.entityToUrlParam(data, true, true);
        orderStr = orderStr + "&corp_secret=" + midMumConfigDto.getSecretKey();
        String sign = MD5Util.hash(orderStr).toUpperCase();
        JSONObject jsonObject = FastJsonUtils.parseObject(FastJsonUtils.toJSONNoFeatures(data));
        jsonObject.put("sign", sign);
        String resultString = HttpUtil.doPost(url, jsonObject.toString(), headers, true);
        return resultString;
    }

}
