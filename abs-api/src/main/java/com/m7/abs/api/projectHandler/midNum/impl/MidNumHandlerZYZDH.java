package com.m7.abs.api.projectHandler.midNum.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.m7.abs.api.common.constant.RedisKeys;
import com.m7.abs.api.common.utils.RedisUtil;
import com.m7.abs.api.domain.dto.midNum.*;
import com.m7.abs.api.domain.vo.midNum.*;
import com.m7.abs.api.mapper.XNumGroupChannelConfMapper;
import com.m7.abs.api.projectHandler.midNum.IMidNumHandler;
import com.m7.abs.api.projectHandler.midNum.MIdNumType;
import com.m7.abs.api.projectHandler.midNum.model.zyhdh.*;
import com.m7.abs.common.constant.common.MidNumCallStatusEnum;
import com.m7.abs.common.constant.common.MidNumSmsStatusEnum;
import com.m7.abs.common.constant.common.MidNumType;
import com.m7.abs.common.constant.error.code.ErrorCodeConstant;
import com.m7.abs.common.constant.keys.CommonSessionKeys;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.XNumGroupChannelConfEntity;
import com.m7.abs.common.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.protocol.HTTP;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * 运营商：中移和多号
 * 对接人：彭科杰
 * 对接时间：202z年04月15日
 */
@Slf4j
@MIdNumType(channel = MidNumType.ZY_HDH)
public class MidNumHandlerZYZDH implements IMidNumHandler {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private XNumGroupChannelConfMapper xNumGroupChannelConfMapper;

    /**
     * 小号平台AXB绑定
     *
     * @param midMumConfigDto
     * @param requestVO
     * @return
     */
    @Override
    public BaseResponse<BindResponseVO> axbBind(MidMumConfigDto midMumConfigDto, BindAXBRequestVO requestVO) {
        try {
            String url = midMumConfigDto.getBasePath() + "/mid/axb/binding";

            if (requestVO.getAreaCode() == null) {
                return BaseResponse.error(ErrorCodeConstant.CommonErrorCode.PARAM_ERR, "Param areaCode is must.");
            }

            ZYHDHAXBReq axbReq = ZYHDHAXBReq.builder()
                    .appId(midMumConfigDto.getAppId())
                    .requestId(MDC.get(CommonSessionKeys.REQ_ID_KEY))
                    .telA("86" + requestVO.getTelA())
                    .telB("86" + requestVO.getTelB())
                    .areaCode(requestVO.getAreaCode())
                    .expiration(requestVO.getExpiration().intValue())
                    .record(requestVO.isNeedRecord() ? "1" : "0")
                    .build();

            String resultStr = sendRequest(midMumConfigDto, url, axbReq, null);
            if (StringUtils.isNotEmpty(resultStr)) {
                ZYHDHAXBResp result = JSON.parseObject(resultStr, ZYHDHAXBResp.class);
                String responseCode = result.getCode();
                if (StringUtils.isNotEmpty(responseCode) && "0000".equals(responseCode)) {
                    BindResponseVO bindResponseVO = new BindResponseVO();
                    bindResponseVO.setMappingId(result.getBindId());
                    String telX = result.getX_no();
                    if (StringUtils.isNotEmpty(telX) && telX.startsWith("86")) {
                        telX = telX.substring(2);
                    }
                    bindResponseVO.setTelX(telX);
                    return BaseResponse.success(bindResponseVO);
                } else {
                    return translateAXBResult(result);
                }
            }
            return BaseResponse.fail("[" + MidNumType.ZY_HDH + "] BindAXB FAIL");
        } catch (Exception e) {
            log.error("[" + MidNumType.ZY_HDH + "] BindAXB Error", e);
            return BaseResponse.error("[" + MidNumType.ZY_HDH + "] BindAXB Error");
        }
    }

    /**
     * 小号平台AX绑定
     *
     * @param midMumConfigDto
     * @param requestVO
     * @return
     */
    @Override
    public BaseResponse<BindResponseVO> axBind(MidMumConfigDto midMumConfigDto, BindAXRequestVO requestVO) {
        try {
            String url = midMumConfigDto.getBasePath() + "/mid/ax/binding";
            if (requestVO.getAreaCode() == null) {
                return BaseResponse.error(ErrorCodeConstant.CommonErrorCode.PARAM_ERR, "Param areaCode is must.");
            }

            ZYHDHAXReq req = ZYHDHAXReq.builder()
                    .requestId(MDC.get(CommonSessionKeys.REQ_ID_KEY))
                    .appId(midMumConfigDto.getAppId())
                    .telA("86" + requestVO.getTelA())
                    .expiration(String.valueOf(requestVO.getExpiration()))
                    .record(requestVO.isNeedRecord() ? "1" : "0")
                    .areaCode(requestVO.getAreaCode())
                    .build();

            String resultStr = sendRequest(midMumConfigDto, url, req, null);
            if (StringUtils.isNotEmpty(resultStr)) {
                ZYHDHAXResp result = JSON.parseObject(resultStr, ZYHDHAXResp.class);
                String responseCode = result.getCode();
                if (StringUtils.isNotEmpty(responseCode) && "0000".equals(responseCode)) {
                    BindResponseVO bindResponseVO = new BindResponseVO();
                    bindResponseVO.setMappingId(result.getBindId());
                    String telX = result.getTelX();
                    if (StringUtils.isNotEmpty(telX) && telX.startsWith("86")) {
                        telX = telX.substring(2);
                    }
                    bindResponseVO.setTelX(telX);
                    return BaseResponse.success(bindResponseVO);
                } else {
                    return translateAXResult(result);
                }
            }
            return BaseResponse.fail("[" + MidNumType.ZY_HDH + "] BindAX FAIL");
        } catch (Exception e) {
            log.error("[" + MidNumType.ZY_HDH + "] BindAX Error", e);
            return BaseResponse.error("[" + MidNumType.ZY_HDH + "] BindAX Error");
        }
    }

    /**
     * 小号平台AXB解绑
     *
     * @param accountDto
     * @param requestVO
     * @return
     */
    @Override
    public BaseResponse unBindAXB(MidMumConfigDto accountDto, UnBindRequestVO requestVO) {
        try {
            String url = accountDto.getBasePath() + "/mid/axb/unbinding";

            ZYHDHAXBUnbindReq unbindReq = ZYHDHAXBUnbindReq.builder()
                    .bindId(requestVO.getAssociationId())
                    .appId(accountDto.getAppId())
                    .build();

            String resultStr = sendRequest(accountDto, url, unbindReq, null);
            if (StringUtils.isNotEmpty(resultStr)) {
                ZYHDHAXBUnbindResp result = FastJsonUtils.toBean(resultStr, ZYHDHAXBUnbindResp.class);
                String code = result.getCode();
                /**
                 * 0000 解绑成功
                 * 1001 绑定关系不存在
                 * 1002 订单已过期
                 */
                if (StringUtils.isNotEmpty(code) && ("0000".equals(code) || "1001".equals(code) || "1002".equals(code))) {
                    return BaseResponse.success();
                } else {
                    return BaseResponse.fail(result.getMessage());
                }
            }
            return BaseResponse.fail("[" + MidNumType.ZY_HDH + "] unBindAXB FAIL");
        } catch (Exception e) {
            log.error("[" + MidNumType.ZY_HDH + "] unBindAXB Error", e);
            return BaseResponse.error("[" + MidNumType.ZY_HDH + "] unBindAXB Error");
        }
    }

    /**
     * 小号平台AX解绑
     *
     * @param accountDto
     * @param requestVO
     * @return
     */
    @Override
    public BaseResponse axUnBind(MidMumConfigDto accountDto, UnBindRequestVO requestVO) {
        try {
            String url = accountDto.getBasePath() + "/mid/ax/unbinding";

            ZYHDHAXUnbindReq unbindReq = ZYHDHAXUnbindReq.builder()
                    .bindId(requestVO.getAssociationId())
                    .appId(accountDto.getAppId())
                    .coolDown(0)
                    .build();

            String resultStr = sendRequest(accountDto, url, unbindReq, null);
            if (StringUtils.isNotEmpty(resultStr)) {
                ZYHDHAXBUnbindResp result = FastJsonUtils.toBean(resultStr, ZYHDHAXBUnbindResp.class);
                String code = result.getCode();
                /**
                 * 0000 解绑成功
                 * 1001 绑定关系不存在
                 * 1002 订单已过期
                 */
                if (StringUtils.isNotEmpty(code) && ("0000".equals(code) || "1001".equals(code) || "1002".equals(code))) {
                    return BaseResponse.success();
                } else {
                    return BaseResponse.fail(result.getMessage());
                }
            }
            return BaseResponse.fail("[" + MidNumType.ZY_HDH + "] unBindAX FAIL");
        } catch (Exception e) {
            log.error("[" + MidNumType.ZY_HDH + "] unBindAX Error", e);
            return BaseResponse.error("[" + MidNumType.ZY_HDH + "] unBindAX Error");
        }
    }

    @Override
    public BaseResponse<BindResponseVO> axDelay(MidMumConfigDto accountDto, DelayAxRequestVO requestVO) {
        try {
            String url = accountDto.getBasePath() + "/mid/ax/delay";

            ZYHDHDelayReq delayReq = ZYHDHDelayReq.builder()
                    .bindId(requestVO.getAssociationId())
                    .appId(accountDto.getAppId())
                    .delta(requestVO.getExtraTime())
                    .build();

            String resultStr = sendRequest(accountDto, url, delayReq, null);
            if (StringUtils.isNotEmpty(resultStr)) {
                ZYHDHDelayResp result = FastJsonUtils.toBean(resultStr, ZYHDHDelayResp.class);
                String code = result.getCode();
                /**
                 * 0000 操作成功
                 */
                if (StringUtils.isNotEmpty(code) && ("0000".equals(code))) {
                    return BaseResponse.success();
                } else {
                    return BaseResponse.fail(result.getMessage());
                }
            }
            return BaseResponse.fail("[" + MidNumType.ZY_HDH + "] axDelay FAIL");
        } catch (Exception e) {
            log.error("[" + MidNumType.ZY_HDH + "] axDelay Error", e);
            return BaseResponse.error("[" + MidNumType.ZY_HDH + "] axDelay Error");
        }
    }

    @Override
    public BaseResponse<BindResponseVO> axbDelay(MidMumConfigDto accountDto, DelayAXBRequestVO requestVO) {
        try {
            String url = accountDto.getBasePath() + "/mid/axb/delay";

            ZYHDHDelayReq delayReq = ZYHDHDelayReq.builder()
                    .bindId(requestVO.getAssociationId())
                    .appId(accountDto.getAppId())
                    .delta(requestVO.getExtraTime())
                    .build();

            String resultStr = sendRequest(accountDto, url, delayReq, null);
            if (StringUtils.isNotEmpty(resultStr)) {
                ZYHDHDelayResp result = FastJsonUtils.toBean(resultStr, ZYHDHDelayResp.class);
                String code = result.getCode();
                /**
                 * 0000 操作成功
                 */
                if (StringUtils.isNotEmpty(code) && ("0000".equals(code))) {
                    return BaseResponse.success();
                } else {
                    return BaseResponse.fail(result.getMessage());
                }
            }
            return BaseResponse.fail("[" + MidNumType.ZY_HDH + "] axbDelay FAIL");
        } catch (Exception e) {
            log.error("[" + MidNumType.ZY_HDH + "] axbDelay Error", e);
            return BaseResponse.error("[" + MidNumType.ZY_HDH + "] axbDelay Error");
        }
    }

    @Override
    public BaseResponse<BindResponseVO> gxbBind(MidMumConfigDto midMumConfigDto, BindGXBRequestVO requestVO) {
        try {
            String url = midMumConfigDto.getBasePath() + "/gxb/gxb/binding";

            if (requestVO.getAreaCode() == null) {
                return BaseResponse.error(ErrorCodeConstant.CommonErrorCode.PARAM_ERR, "Param areaCode is must.");
            }

            ZYHDHGXBReq zyhdhgxbReq = ZYHDHGXBReq.builder()
                    .appId(midMumConfigDto.getAppId())
                    .orderId(MDC.get(CommonSessionKeys.REQ_ID_KEY))
                    .groupId(requestVO.getAssociationId())
                    .telB("86" + requestVO.getTelB())
                    .areaCode(requestVO.getAreaCode())
                    .expiration(requestVO.getExpiration().intValue())
                    .record(requestVO.isNeedRecord() ? "1" : "0")
                    .build();

            String resultStr = sendRequest(midMumConfigDto, url, zyhdhgxbReq, null);
            if (StringUtils.isNotEmpty(resultStr)) {
                ZYHDHGXBResp result = JSON.parseObject(resultStr, ZYHDHGXBResp.class);
                String responseCode = result.getCode();
                if (StringUtils.isNotEmpty(responseCode) && "0000".equals(responseCode)) {
                    BindResponseVO bindResponseVO = new BindResponseVO();
                    bindResponseVO.setMappingId(result.getBindId());
                    String telX = result.getX_no();
                    if (StringUtils.isNotEmpty(telX) && telX.startsWith("86")) {
                        telX = telX.substring(2);
                    }
                    bindResponseVO.setTelX(telX);
                    return BaseResponse.success(bindResponseVO);
                } else {
                    return translateGXBResult(result);
                }
            }
            return BaseResponse.fail("[" + MidNumType.ZY_HDH + "] gxbBind FAIL");
        } catch (Exception e) {
            log.error("[" + MidNumType.ZY_HDH + "] gxbBind Error", e);
            return BaseResponse.error("[" + MidNumType.ZY_HDH + "] gxbBind Error");
        }
    }

    @Override
    public BaseResponse<BindResponseVO> gxbUnBind(MidMumConfigDto midMumConfigDto, UnBindRequestVO requestVO) {
        try {
            String url = midMumConfigDto.getBasePath() + "/gxb/gxb/unbinding";

            ZYHDHGXBUnbindReq zyhdhgxbReq = ZYHDHGXBUnbindReq.builder()
                    .appId(midMumConfigDto.getAppId())
                    .bindId(requestVO.getAssociationId())
                    .groupId(requestVO.getAssociationGroupId())
                    .build();

            String resultStr = sendRequest(midMumConfigDto, url, zyhdhgxbReq, null);
            if (StringUtils.isNotEmpty(resultStr)) {
                ZYHDHGXBUnbindResp result = JSON.parseObject(resultStr, ZYHDHGXBUnbindResp.class);
                String code = result.getCode();
                if (StringUtils.isNotEmpty(code) &&  ("0000".equals(code) || "1001".equals(code) || "1002".equals(code))) {
                    BindResponseVO bindResponseVO = new BindResponseVO();
                    bindResponseVO.setMappingId(result.getBindId());
                    String telX = result.getX_no();
                    if (StringUtils.isNotEmpty(telX) && telX.startsWith("86")) {
                        telX = telX.substring(2);
                    }
                    bindResponseVO.setTelX(telX);
                    return BaseResponse.success(bindResponseVO);
                } else {
                    return BaseResponse.fail(result.getMessage());
                }
            }
            return BaseResponse.fail("[" + MidNumType.ZY_HDH + "] gxbBind FAIL");
        } catch (Exception e) {
            log.error("[" + MidNumType.ZY_HDH + "] gxbBind Error", e);
            return BaseResponse.error("[" + MidNumType.ZY_HDH + "] gxbBind Error");
        }
    }


    @Override
    public BaseResponse<String> groupInsert(MidMumConfigDto midMumConfigDto) {
        BaseResponse<String> success = BaseResponse.success();
        success.setData(getAssociationId());
        return success;
    }

    private String getAssociationId() {
        String associationId = MyStringUtils.randomUUID();
        associationId = associationId.substring(0, 20);
        LambdaQueryWrapper<XNumGroupChannelConfEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(XNumGroupChannelConfEntity::getAssociationId, associationId);
        queryWrapper.last("LIMIT 1");
        XNumGroupChannelConfEntity xNumGroupChannelConfEntity = xNumGroupChannelConfMapper.selectOne(queryWrapper);
        if (xNumGroupChannelConfEntity == null) {
            return associationId;
        } else {
            return getAssociationId();
        }
    }


    @Override
    public BaseResponse groupAddNumbers(MidMumConfigDto midMumConfigDto, GroupAddNumbersRequestVO requestVO, List<String> oldNumber) {
        try {
            String url = midMumConfigDto.getBasePath() + "/gxb/gxb/phonelist";
            List<String> numberList = new ArrayList<>();
            numberList.addAll(requestVO.getNumbers());
            numberList.addAll(oldNumber);
            ZYHDHGGroupReq zyhdhgGroupReq = ZYHDHGGroupReq.builder()
                    .appId(midMumConfigDto.getAppId())
                    .groupId(requestVO.getAssociationId())
                    .numberList(numberList)
                    .Method("6")
                    .srvType("0")
                    .build();

            String resultStr = sendRequest(midMumConfigDto, url, zyhdhgGroupReq, null);
            if (StringUtils.isNotEmpty(resultStr)) {
                ZYHDHGGroupResp result = JSON.parseObject(resultStr, ZYHDHGGroupResp.class);
                String responseCode = result.getCode();
                if (StringUtils.isNotEmpty(responseCode) && "0000".equals(responseCode)) {
                    return BaseResponse.success();
                } else {
                    return BaseResponse.fail(result.getMessage());
                }
            }
            return BaseResponse.fail("[" + MidNumType.ZY_HDH + "] groupAddNumbers FAIL");
        } catch (Exception e) {
            log.error("[" + MidNumType.ZY_HDH + "] groupAddNumbers Error", e);
            return BaseResponse.error("[" + MidNumType.ZY_HDH + "] groupAddNumbers Error");
        }
    }

    @Override
    public BaseResponse groupDelNumbers(MidMumConfigDto midMumConfigDto, GroupDelNumbersRequestVO requestVO, List<String> oldNumber) {
        try {
            String url = midMumConfigDto.getBasePath() + "/gxb/gxb/phonelist";
            List<String> numbers = requestVO.getNumbers();
            if (oldNumber != null && numbers != null) {
                oldNumber.removeAll(numbers);
            }

            ZYHDHGGroupReq zyhdhgGroupReq = ZYHDHGGroupReq.builder()
                    .appId(midMumConfigDto.getAppId())
                    .groupId(requestVO.getAssociationId())
                    .numberList(oldNumber)
                    .Method("6")
                    .srvType("0")
                    .build();

            String resultStr = sendRequest(midMumConfigDto, url, zyhdhgGroupReq, null);
            if (StringUtils.isNotEmpty(resultStr)) {
                ZYHDHGGroupResp result = JSON.parseObject(resultStr, ZYHDHGGroupResp.class);
                String responseCode = result.getCode();
                if (StringUtils.isNotEmpty(responseCode) && "0000".equals(responseCode)) {
                    return BaseResponse.success();
                } else {
                    return BaseResponse.fail(result.getMessage());
                }
            }
            return BaseResponse.fail("[" + MidNumType.ZY_HDH + "] groupDelNumbers FAIL");
        } catch (Exception e) {
            log.error("[" + MidNumType.ZY_HDH + "] groupDelNumbers Error", e);
            return BaseResponse.error("[" + MidNumType.ZY_HDH + "] groupDelNumbers Error");
        }
    }

    @Override
    public BaseResponse groupDel(MidMumConfigDto midMumConfigDto, GroupDelRequestVO requestVO) {
        try {
            String url = midMumConfigDto.getBasePath() + "/gxb/gxb/phonelist";

            ZYHDHGGroupReq zyhdhgGroupReq = ZYHDHGGroupReq.builder()
                    .appId(midMumConfigDto.getAppId())
                    .groupId(requestVO.getAssociationId())
                    .Method("1")
                    .srvType("0")
                    .build();

            String resultStr = sendRequest(midMumConfigDto, url, zyhdhgGroupReq, null);
            if (StringUtils.isNotEmpty(resultStr)) {
                ZYHDHGGroupResp result = JSON.parseObject(resultStr, ZYHDHGGroupResp.class);
                String responseCode = result.getCode();
                if (StringUtils.isNotEmpty(responseCode) && "0000".equals(responseCode)) {
                    return BaseResponse.success();
                } else {
                    return BaseResponse.fail(result.getMessage());
                }
            }
            return BaseResponse.fail("[" + MidNumType.ZY_HDH + "] groupDel FAIL");
        } catch (Exception e) {
            log.error("[" + MidNumType.ZY_HDH + "] groupDel Error", e);
            return BaseResponse.error("[" + MidNumType.ZY_HDH + "] groupDel Error");
        }
    }

    private BaseResponse<BindResponseVO> translateGXBResult(ZYHDHGXBResp result) {
        String responseCode = result.getCode();

        ErrorCodeConstant constant = ErrorCodeConstant.CommonErrorCode.FAILED;
        if (StringUtils.isNotEmpty(responseCode)) {
            switch (responseCode) {
                case "1001":
                    constant = ErrorCodeConstant.ApiErrorCode.BIND_ALREADY_EXISTS;
                    break;
                case "1002":
                    constant = ErrorCodeConstant.ApiErrorCode.NO_NUMBER_AVAILABLE;
                    break;
                case "1003":
                case "1004":
                case "1005":
                case "1006":
                    constant = ErrorCodeConstant.CommonErrorCode.PARAM_ERR;
                    break;
                case "1007":
                    constant = ErrorCodeConstant.ApiErrorCode.BINDING_RELATIONSHIP_CONFLICT;
                    break;
                default:
                    constant = ErrorCodeConstant.CommonErrorCode.FAILED;
                    break;
            }
        }

        if (constant.getCode().equals(ErrorCodeConstant.CommonErrorCode.FAILED.getCode())) {
            return BaseResponse.error(constant.getCode(), result.getMessage());
        } else {
            return BaseResponse.error(constant);
        }
    }

    private BaseResponse<BindResponseVO> translateGXBUnbindResult(ZYHDHGXBUnbindResp result) {
        return null;
    }

    @Override
    public BaseResponse<MidNumSmsRecordRespDto> translateSmsRecord(Map<String, Object> requestVO) {
        if (requestVO != null) {
            String reqJsonStr = FastJsonUtils.toJSONString(requestVO);
            ZYHDHSmsRecordReq recordReq = FastJsonUtils.toBean(reqJsonStr, ZYHDHSmsRecordReq.class);

            String smsResult = recordReq.getSmsResult();
            String bindId = recordReq.getBindId();
            String callId = recordReq.getCallId();
            String caller = recordReq.getCallNo();
            String called = recordReq.getPeerNo();
            String x = recordReq.getX();
            String smsTime = recordReq.getSmsTime();

            if (StringUtils.isNotEmpty(x) && x.startsWith("86")) {
                x = x.substring(2);
            }
            if (StringUtils.isNotEmpty(caller) && caller.startsWith("86")) {
                caller = caller.substring(2);
            }
            if (StringUtils.isNotEmpty(called) && called.startsWith("86")) {
                called = called.substring(2);
            }

            MidNumSmsTranslateDto recordDto = MidNumSmsTranslateDto.builder()
                    .channelBindId(bindId)
                    .channelRecordId(callId)
                    .caller(caller)
                    .callee(called)
                    .telX(x)
                    .smsResult(translateSmsStatus(smsResult))
                    .smsNumber(recordReq.getSmsNumber())
                    .smsTime(DateUtil.parseStrToDate(smsTime, DateUtil.DATE_TIME_FORMAT_YYYYMMDDHHMISS))
                    .build();

            Map<String, Object> respMap = new HashMap<>();
            respMap.put("code", "0000");
            respMap.put("message", "success");
            MidNumSmsRecordRespDto respDto = MidNumSmsRecordRespDto.builder()
                    .translateDto(recordDto)
                    .respData(respMap)
                    .build();
            return BaseResponse.success(respDto);
        }

        return BaseResponse.fail("translate record fail.");
    }


    @Override
    public BaseResponse<MidNumRecordRespDto> translateRecord(Map<String, Object> requestVO) {
        if (requestVO != null) {
            String reqJsonStr = FastJsonUtils.toJSONString(requestVO);
            ZYHDHRecordReq recordReq = FastJsonUtils.toBean(reqJsonStr, ZYHDHRecordReq.class);

            String finishState = recordReq.getFinishState();
            String mappingId = recordReq.getBindId();
            String callId = recordReq.getCallId();

            if (mappingId.equals("0")) {
                mappingId = recordReq.getBindId();
            }

            int callDuration = recordReq.getCallDuration();
            String startTime = recordReq.getStartTime();
            String releaseTime = recordReq.getFinishTime();

            String caller = recordReq.getCallNo();
            String called = recordReq.getPeerNo();
            String x = recordReq.getX();

            if (StringUtils.isNotEmpty(x) && x.startsWith("86")) {
                x = x.substring(2);
            }
            if (StringUtils.isNotEmpty(caller) && caller.startsWith("86")) {
                caller = caller.substring(2);
            }
            if (StringUtils.isNotEmpty(called) && called.startsWith("86")) {
                called = called.substring(2);
            }

            String releaseDir=recordReq.getFinishType();

            MidNumTranslateDto recordDto = MidNumTranslateDto.builder()
                    .channelBindId(mappingId)
                    .channelRecordId(callId)
                    .channelGroupId(recordReq.getGroupId())
                    .caller(caller)
                    .callee(called)
                    .telX(x)
                    .result(translateCallStatus(callDuration, finishState))
                    .callDisplay("1")//0显真实号 1 不显真实号 2强制不显真实号
                    .billDuration(callDuration)
                    .beginTime(DateUtil.parseStrToDate(recordReq.getCallTime(), DateUtil.DATE_TIME_FORMAT_YYYYMMDDHHMISS))
                    .releaseTime(DateUtil.parseStrToDate(releaseTime, DateUtil.DATE_TIME_FORMAT_YYYYMMDDHHMISS))
                    .connectTime(DateUtil.parseStrToDate(startTime, DateUtil.DATE_TIME_FORMAT_YYYYMMDDHHMISS))
                    .calledShow(x)
                    .callerShow(x)
                    .releaseDir(releaseDir)
                    .build();

            try {
                String ringTime = (String) redisUtil.get(RedisKeys.REDIS_MID_NUM_RINGTIME + "-" + callId);
                if (StringUtils.isNotEmpty(releaseTime)) {
                    recordDto.setAlertingTime(DateUtil.parseStrToDate(ringTime, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));
                    redisUtil.del(RedisKeys.REDIS_MID_NUM_RINGTIME + "-" + callId);
                }
            } catch (Exception e) {
                log.error("get ring time from redis error.", e);
            }

            Map<String, Object> respMap = new HashMap<>();
            respMap.put("code", "0000");
            respMap.put("message", "success");
            MidNumRecordRespDto respDto = MidNumRecordRespDto.builder()
                    .translateDto(recordDto)
                    .respData(respMap)
                    .build();
            return BaseResponse.success(respDto);
        }

        return BaseResponse.fail("translate record fail.");
    }


    /**
     * 绑定请求响应码：
     * 0000: 成功；
     * 1001：绑定关系已存在；
     * 1002： 请求消息指定地区无可用中间号；
     * 1003：中间号号码池已达绑定上限；
     * 1004：请求消息过期时间参数无效（小于或等于0）；
     * 1005： 请求消息格式错误；
     * 1006： 请求消息参数有误；
     * 1008：企业不存在；
     * 1011：账户与中间号模式不匹配；
     * 1018：真实号码不正确；
     * 1111：该企业暂停服务；
     * 1113：入参必须指示要不录音；
     * 1114：入参必须指示要录音；
     * 2000：找不到appId对应的应用；
     * 2001：多账户appId不能为空；
     * 1050：系统异常；
     *
     * @param result
     * @return
     */
    private BaseResponse translateAXBResult(ZYHDHAXBResp result) {
        String responseCode = result.getCode();

        ErrorCodeConstant constant = ErrorCodeConstant.CommonErrorCode.FAILED;
        if (StringUtils.isNotEmpty(responseCode)) {
            switch (responseCode) {
                case "1001":
                    constant = ErrorCodeConstant.ApiErrorCode.BIND_ALREADY_EXISTS;
                    break;
                case "1002":
                    constant = ErrorCodeConstant.ApiErrorCode.NO_NUMBER_AVAILABLE;
                    break;
                case "1003":
                    constant = ErrorCodeConstant.ApiErrorCode.NUMBER_BINDING_UPPER_LIMIT;
                    break;
                case "1004":
                case "1005":
                case "1006":
                    constant = ErrorCodeConstant.CommonErrorCode.PARAM_ERR;
                    break;
                case "1008":
                    constant = ErrorCodeConstant.ApiErrorCode.ACCOUNT_NOT_FOUND;
                    break;
                case "1011":
                    constant = ErrorCodeConstant.ApiErrorCode.BIND_TYPE_NOT_SUPPORT;
                    break;
                case "1050":
                    constant = ErrorCodeConstant.CommonErrorCode.SYSTEM_EXCEPTION;
                    break;
                default:
                    constant = ErrorCodeConstant.CommonErrorCode.FAILED;
                    break;
            }
        }

        if (constant.getCode().equals(ErrorCodeConstant.CommonErrorCode.FAILED.getCode())) {
            return BaseResponse.error(constant.getCode(), result.getMessage());
        } else {
            return BaseResponse.error(constant);
        }
    }

    private BaseResponse translateAXResult(ZYHDHAXResp result) {
        String responseCode = result.getCode();

        ErrorCodeConstant constant = ErrorCodeConstant.CommonErrorCode.FAILED;
        if (StringUtils.isNotEmpty(responseCode)) {
            switch (responseCode) {
                case "1001":
                    constant = ErrorCodeConstant.ApiErrorCode.BIND_ALREADY_EXISTS;
                    break;
                case "1002":
                    constant = ErrorCodeConstant.ApiErrorCode.NO_NUMBER_AVAILABLE;
                    break;
                case "1003":
                    constant = ErrorCodeConstant.ApiErrorCode.NUMBER_BINDING_UPPER_LIMIT;
                    break;
                case "1004":
                case "1005":
                case "1006":
                    constant = ErrorCodeConstant.CommonErrorCode.PARAM_ERR;
                    break;
                case "1008":
                    constant = ErrorCodeConstant.ApiErrorCode.ACCOUNT_NOT_FOUND;
                    break;
                case "1011":
                    constant = ErrorCodeConstant.ApiErrorCode.BIND_TYPE_NOT_SUPPORT;
                    break;
                case "1050":
                    constant = ErrorCodeConstant.CommonErrorCode.SYSTEM_EXCEPTION;
                    break;
                default:
                    constant = ErrorCodeConstant.CommonErrorCode.FAILED;
                    break;
            }
        }

        if (constant.getCode().equals(ErrorCodeConstant.CommonErrorCode.FAILED.getCode())) {
            return BaseResponse.error(constant.getCode(), result.getMessage());
        } else {
            return BaseResponse.error(constant);
        }
    }

    /**
     * 结束状态（即挂断原因）：
     * 0： 无绑定关系
     * 1:	主叫挂机
     * 2:	被叫挂机
     * 3:	主叫放弃
     * 4:	被叫无应答
     * 5:	被叫忙
     * 6:	被叫不可及
     * 7:	路由失败
     * 8:	中间号状态异常
     * 9:	订单超过有效期（自1.2.13版本开始弃用）
     * 10:	平台系统异常
     * 11: 关机
     * 12: 停机
     * 13: 拒接
     * 14: 空号
     * 注：11-14状态值只出现在AS呼叫
     *
     * @param callDuration
     * @param status
     * @return
     */
    private String translateCallStatus(int callDuration, String status) {

        if (StringUtils.isNotEmpty(status)) {
            if (callDuration > 0 || ("2".equals(status) || "1".equals(status))) {
                return MidNumCallStatusEnum.NORMAL_HANGUP.getValue();
            }

            switch (status) {
                case "3":
                    return MidNumCallStatusEnum.CALLER_ABANDONMENT.getValue();
                case "4":
                    return MidNumCallStatusEnum.CALLED_NO_ANSWER.getValue();
                case "5":
                    return MidNumCallStatusEnum.CALLED_BUSY.getValue();
                case "6":
                    return MidNumCallStatusEnum.CALLED_OUT_OF_REACH.getValue();
                case "7":
                    return MidNumCallStatusEnum.ROUTING_FAILED.getValue();
                case "8":
                    return MidNumCallStatusEnum.TEL_X_ERROR_STATE.getValue();
                case "10":
                    return MidNumCallStatusEnum.PLATFORM_ERROR.getValue();
                case "11":
                    return MidNumCallStatusEnum.POWER_OFF.getValue();
                case "12":
                    return MidNumCallStatusEnum.OUT_OF_SERVICE.getValue();
                case "13":
                    return MidNumCallStatusEnum.REFUSE.getValue();
                case "14":
                    return MidNumCallStatusEnum.EMPTY_NUMBER.getValue();

            }
        }
        return MidNumCallStatusEnum.CALL_FAIL.getValue();
    }

    private String translateSmsStatus(String status) {

        if (StringUtils.isNotEmpty(status)) {

            switch (status) {
                case "0":
                    return MidNumSmsStatusEnum.FAIL.getValue();
                case "1":
                    return MidNumSmsStatusEnum.SUCCESS.getValue();
                case "2":
                    return MidNumSmsStatusEnum.TIMEOUT.getValue();
                case "3":
                    return MidNumSmsStatusEnum.MID_NUM_STATUS_ERROR.getValue();
                case "4":
                    return MidNumSmsStatusEnum.ENTERPRISE_STATUS_ERROR.getValue();
                case "5":
                    return MidNumSmsStatusEnum.ENTERPRISE_NO_FUNCTION.getValue();
                case "6":
                    return MidNumSmsStatusEnum.NO_RELATION.getValue();
                case "7":
                    return MidNumSmsStatusEnum.ORDER_TIMEOUT.getValue();
                case "8":
                    return MidNumSmsStatusEnum.ORDER_STATUS_ERROR.getValue();
                case "9":
                    return MidNumSmsStatusEnum.SYSTEM_ERROR.getValue();
                case "19":
                    return MidNumSmsStatusEnum.OVERCLOCK.getValue();
                case "20":
                    return MidNumSmsStatusEnum.CONTAINS_SENSITIVE_KEYWORDS.getValue();
                case "21":
                    return MidNumSmsStatusEnum.CONTAINS_ILLEGAL_KEYWORDS.getValue();

            }
        }
        return MidNumCallStatusEnum.CALL_FAIL.getValue();
    }

    @Override
    public BaseResponse<MidNumRecordRespDto> translateRecordUrl(Map<String, Object> requestVO) {
        if (requestVO != null) {
            String reqJsonStr = FastJsonUtils.toJSONString(requestVO);
            ZYHDHRecordUrlReq recordUrlReq = FastJsonUtils.toBean(reqJsonStr, ZYHDHRecordUrlReq.class);


            MidNumRecordUrlTranslateDto recordUrlDto = MidNumRecordUrlTranslateDto.builder()
                    .channelRecordId(recordUrlReq.getCallId())
                    .channelBindId(recordUrlReq.getBindId())
                    .recordFileUrl(recordUrlReq.getRecordUrl())
                    .build();

            Map<String, Object> respMap = new HashMap<>();
            respMap.put("code", "0000");
            respMap.put("message", "success");
            MidNumRecordRespDto respDto = MidNumRecordRespDto.builder()
                    .recordUrlDto(recordUrlDto)
                    .respData(respMap)
                    .build();
            return BaseResponse.success(respDto);
        }

        return BaseResponse.fail("translate record fail.");


    }

    @Override
    public BaseResponse<MidNumRecordRespDto> translateRingTime(Map<String, Object> requestVO) {
        if (requestVO != null) {
            String reqJsonStr = FastJsonUtils.toJSONString(requestVO);
            ZYHDHGetRingingTimeReq recordReq = FastJsonUtils.toBean(reqJsonStr, ZYHDHGetRingingTimeReq.class);

            String caller = recordReq.getCallNo();
            String called = recordReq.getPeerNo();
            String telX = recordReq.getTelX();

            if (StringUtils.isNotEmpty(telX) && telX.startsWith("86")) {
                telX = telX.substring(2);
            }
            if (StringUtils.isNotEmpty(caller) && caller.startsWith("86")) {
                caller = caller.substring(2);
            }
            if (StringUtils.isNotEmpty(called) && called.startsWith("86")) {
                called = called.substring(2);
            }

            MidNumRingTimeTranslateDto ringTimeDto = MidNumRingTimeTranslateDto.builder()
                    .channelRecordId(recordReq.getCallId())
                    .channelBindId(recordReq.getBindId())
                    .ringTime(DateUtil.parseStrToDate(recordReq.getRingTime(), DateUtil.DATE_TIME_FORMAT_YYYYMMDDHHMISS))
                    .caller(caller)
                    .callee(called)
                    .telX(telX)
                    .build();

            Map<String, Object> respMap = new HashMap<>();
            respMap.put("code", "0000");
            respMap.put("message", "success");
            MidNumRecordRespDto respDto = MidNumRecordRespDto.builder()
                    .ringTimeDto(ringTimeDto)
                    .respData(respMap)
                    .build();
            return BaseResponse.success(respDto);
        }

        return BaseResponse.fail("translate record fail.");
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

        String platformId = midMumConfigDto.getAccessKey();
        String timestamp = String.valueOf(new Date().getTime() / 1000);
        String signature = MD5Util.md5Encrypt32Lower(timestamp + midMumConfigDto.getSecretKey()).toUpperCase();

        headers.put(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8");
        headers.put("Authorization", "EOPAUTH platformid=\"" + platformId + "\",timestamp=\"" + timestamp + "\",signature=\"" + signature + "\"");
        JSONObject jsonObject = FastJsonUtils.parseObject(FastJsonUtils.toJSONNoFeatures(data));

        String resultString = HttpUtil.doPost(url, jsonObject.toString(), headers, true);
        return resultString;
    }

}
