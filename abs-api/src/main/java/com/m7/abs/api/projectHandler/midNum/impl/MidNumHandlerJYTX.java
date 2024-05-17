package com.m7.abs.api.projectHandler.midNum.impl;

import com.alibaba.fastjson.JSONObject;
import com.m7.abs.api.common.properties.AbsApiProperties;
import com.m7.abs.api.domain.dto.midNum.MidMumConfigDto;
import com.m7.abs.api.domain.dto.midNum.MidNumRecordRespDto;
import com.m7.abs.api.domain.dto.midNum.MidNumSmsRecordRespDto;
import com.m7.abs.api.domain.dto.midNum.MidNumTranslateDto;
import com.m7.abs.api.domain.vo.midNum.*;
import com.m7.abs.api.projectHandler.midNum.IMidNumHandler;
import com.m7.abs.api.projectHandler.midNum.MIdNumType;
import com.m7.abs.api.projectHandler.midNum.model.jytx.*;
import com.m7.abs.common.constant.common.MidNumCallStatusEnum;
import com.m7.abs.common.constant.common.MidNumType;
import com.m7.abs.common.constant.error.code.ErrorCodeConstant;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.utils.FastJsonUtils;
import com.m7.abs.common.utils.HttpUtil;
import com.m7.abs.common.utils.MyStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.protocol.HTTP;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@MIdNumType(channel = MidNumType.JYTX)
public class MidNumHandlerJYTX implements IMidNumHandler {

    @Resource
    private AbsApiProperties absApiProperties;

    /**
     * 君悦天下小号平台AXB绑定
     *
     * @param midMumConfigDto
     * @param requestVO
     * @return
     */
    @Override
    public BaseResponse<BindResponseVO> axbBind(MidMumConfigDto midMumConfigDto, BindAXBRequestVO requestVO) {
        try {
            String url = midMumConfigDto.getBasePath() + "/vnum/numberBinding/binding";

            String bindId = MyStringUtils.randomUUID();


            //icDisplayFlag "0"显真实号码 "1"显示中间号 默认 "1"
            Integer icDisplayFlag = requestVO.getIcDisplayFlag();
            if (icDisplayFlag == null || icDisplayFlag == 1) {
                icDisplayFlag = 0;
            } else {
                icDisplayFlag = 1;
            }

            JYTXAXBReq jytxaxbReq = JYTXAXBReq.builder()
                    .appId(midMumConfigDto.getAppId())
                    .bindId(bindId)//绑定唯一标识，该标识全局唯一
                    .caller("0086" + requestVO.getTelA())//主叫号码(必须为11位手机号，号码前加0086，如008613631686024)如果caller默认为空，则为小号模式。
                    .callee("0086" + requestVO.getTelB())//被叫号码,规则同caller
                    .dstVirtualNum("0086" + requestVO.getTelX())//分配的直呼虚拟中间保护号码，规则同caller
//                    .calleeDisplayNum()//被叫显号，可传入真实主叫，被叫显示真实主叫，如果该字段为空，被叫默认显虚拟中间号，规则同caller(透传真实主叫会影响接通率)
                    .calldisplay(String.valueOf(icDisplayFlag))//calldisplay的值，0或者不填显示中间号码1全部显示为真实号码
//                    .smscode()
                    .maxAge(String.valueOf(requestVO.getExpiration()))//主被叫+虚拟保护号码允许合作方最大cache存储时间(单位秒)，超过该时间绑定关系失效，拨叫无法接通
                    .requestId(bindId)//请求ID，该requestId在后面话单和录音URL推送中原样带回,对应以前对外接口的userData字段
//                    .statusUrl()//暂没有
                    .hangupUrl(absApiProperties.getApi().getServerUrl() + "/midNum/record/" + MidNumType.JYTX)//接收话单地址
                    .record(requestVO.isNeedRecord() ? "1" : "0")
                    .recordUrl(absApiProperties.getApi().getServerUrl() + "/midNum/record/" + MidNumType.JYTX)//接收话单地址
//                    .anucode()
                    .build();

            String resultStr = sendRequest(url, jytxaxbReq, null);
            if (StringUtils.isNotEmpty(resultStr)) {
                JYTXResp result = FastJsonUtils.toBean(resultStr, JYTXResp.class);
                String responseCode = result.getStatus();
                if (responseCode != null && responseCode.equals("200")) {
                    BindResponseVO bindResponseVO = new BindResponseVO();
                    bindResponseVO.setMappingId(bindId);
                    return BaseResponse.success(bindResponseVO);
                } else {
                    return BaseResponse.fail(result.getMeg());
                }
            }
            return BaseResponse.fail("[" + MidNumType.JYTX + "] BindAXB FAIL");
        } catch (Exception e) {
            log.error("[" + MidNumType.JYTX + "] BindAXB Error", e);
            return BaseResponse.error("[" + MidNumType.JYTX + "] BindAXB Error");
        }
    }

    /**
     * 君悦天下小号平台AX绑定
     *
     * @param midMumConfigDto
     * @param requestVO
     * @return
     */
    @Override
    public BaseResponse<BindResponseVO> axBind(MidMumConfigDto midMumConfigDto, BindAXRequestVO requestVO) {
        try {
            String url = midMumConfigDto.getBasePath() + "/vnum/numberBinding/binding";

            String bindId = MyStringUtils.randomUUID();


            //icDisplayFlag "0"显真实号码 "1"显示中间号 默认 "1"
            Integer icDisplayFlag = requestVO.getIcDisplayFlag();
            if (icDisplayFlag == null || icDisplayFlag == 1) {
                icDisplayFlag = 0;
            } else {
                icDisplayFlag = 1;
            }

            JYTXAxReq jytxAxReq = JYTXAxReq.builder()
                    .appId(midMumConfigDto.getAppId())
                    .bindId(bindId)//绑定唯一标识，该标识全局唯一
//                    .caller()//主叫号码(必须为11位手机号，号码前加0086，如008613631686024)如果caller默认为空，则为小号模式。
                    .callee("0086" + requestVO.getTelA())//被叫号码,规则同caller
                    .dstVirtualNum("0086" + requestVO.getTelX())//分配的直呼虚拟中间保护号码，规则同caller
//                    .calleeDisplayNum()//被叫显号，可传入真实主叫，被叫显示真实主叫，如果该字段为空，被叫默认显虚拟中间号，规则同caller(透传真实主叫会影响接通率)
                    .calldisplay(String.valueOf(icDisplayFlag))//calldisplay的值，0或者不填显示中间号码1全部显示为真实号码
//                    .smscode()
                    .maxAge(String.valueOf(requestVO.getExpiration()))//主被叫+虚拟保护号码允许合作方最大cache存储时间(单位秒)，超过该时间绑定关系失效，拨叫无法接通
                    .requestId(bindId)//请求ID，该requestId在后面话单和录音URL推送中原样带回,对应以前对外接口的userData字段
//                    .statusUrl()//暂没有
                    .hangupUrl(absApiProperties.getApi().getServerUrl() + "/midNum/record/" + MidNumType.JYTX)//接收话单地址
                    .record(requestVO.isNeedRecord() ? "1" : "0")
                    .recordUrl(absApiProperties.getApi().getServerUrl() + "/midNum/record/" + MidNumType.JYTX)//接收话单地址
//                    .anucode()
                    .build();

            String resultStr = sendRequest(url, jytxAxReq, null);
            if (StringUtils.isNotEmpty(resultStr)) {
                JYTXResp result = FastJsonUtils.toBean(resultStr, JYTXResp.class);
                String responseCode = result.getStatus();
                if (responseCode != null && responseCode.equals("200")) {
                    BindResponseVO bindResponseVO = new BindResponseVO();
                    bindResponseVO.setMappingId(bindId);
                    return BaseResponse.success(bindResponseVO);
                } else {
                    return BaseResponse.fail(result.getMeg());
                }
            }
            return BaseResponse.fail("[" + MidNumType.JYTX + "] BindXB FAIL");
        } catch (Exception e) {
            log.error("[" + MidNumType.JYTX + "] BindXB Error", e);
            return BaseResponse.error("[" + MidNumType.JYTX + "] BindXB Error");
        }
    }

    /**
     * 君悦天下小号平台AXB解绑
     *
     * @param accountDto
     * @param requestVO
     * @return
     */
    @Override
    public BaseResponse unBindAXB(MidMumConfigDto accountDto, UnBindRequestVO requestVO) {
        try {
            String url = accountDto.getBasePath() + "/vnum/numberBinding/unbind";

            JYTXAXBAndXBUnbindReq jytxaxbAndXBUnbindReq = JYTXAXBAndXBUnbindReq.builder()
                    .appId(accountDto.getAppId())
                    .bindId(requestVO.getAssociationId())//绑定唯一标识，该标识全局唯一
                    .build();

            String resultStr = sendRequest(url, jytxaxbAndXBUnbindReq, null);
            if (StringUtils.isNotEmpty(resultStr)) {
                JYTXResp result = FastJsonUtils.toBean(resultStr, JYTXResp.class);
                String responseCode = result.getStatus();
                if (responseCode != null && (responseCode.equals("200") || responseCode.equals("201"))) {
                    return BaseResponse.success();
                } else {
                    return BaseResponse.fail(result.getMeg());
                }
            }
            return BaseResponse.fail("[" + MidNumType.JYTX + "] unBindAXB FAIL");
        } catch (Exception e) {
            log.error("[" + MidNumType.JYTX + "] unBindAXB Error", e);
            return BaseResponse.error("[" + MidNumType.JYTX + "] unBindAXB Error");
        }
    }


    /**
     * 君悦天下小号平台AX解绑
     *
     * @param accountDto
     * @param requestVO
     * @return
     */
    @Override
    public BaseResponse axUnBind(MidMumConfigDto accountDto, UnBindRequestVO requestVO) {
        try {
            String url = accountDto.getBasePath() + "/vnum/numberBinding/unbind";

            JYTXAXBAndXBUnbindReq jytxaxbAndXBUnbindReq = JYTXAXBAndXBUnbindReq.builder()
                    .appId(accountDto.getAppId())
                    .bindId(requestVO.getAssociationId())//绑定唯一标识，该标识全局唯一
                    .build();

            String resultStr = sendRequest(url, jytxaxbAndXBUnbindReq, null);
            if (StringUtils.isNotEmpty(resultStr)) {
                JYTXResp result = FastJsonUtils.toBean(resultStr, JYTXResp.class);
                String responseCode = result.getStatus();
                if (responseCode != null && (responseCode.equals("200") || responseCode.equals("201"))) {
                    return BaseResponse.success();
                } else {
                    return BaseResponse.fail(result.getMeg());
                }
            }
            return BaseResponse.fail("[" + MidNumType.JYTX + "] unBindAx FAIL");
        } catch (Exception e) {
            log.error("[" + MidNumType.JYTX + "] unBindAx Error", e);
            return BaseResponse.error("[" + MidNumType.JYTX + "] unBindAx Error");
        }
    }

    @Override
    public BaseResponse<MidNumRecordRespDto> translateRecord(Map<String, Object> requestVO) {
        if (requestVO != null) {
            String reqJsonStr = FastJsonUtils.toJSONString(requestVO);
            JYTXRecordReq recordReq = FastJsonUtils.toBean(reqJsonStr, JYTXRecordReq.class);

            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String dstAcceptTime = "";//被叫接听时间
            long billDuration = 0;//接听时长
            String endCallTime = "";//用户挂机通话结束时间

            if (StringUtils.isNotBlank(dstAcceptTime)) {
                dstAcceptTime = recordReq.getDstAcceptTime();
                endCallTime = recordReq.getEndCallTime();
                billDuration = Long.parseLong(endCallTime) - Long.parseLong(dstAcceptTime);
            }

            //当存在callRecording为true时，说明通话有录音，这里为了按有录音计费，加上临时的录音地址，目前天音直连没有这个字段，先加上
            Boolean callRecording = recordReq.getCallRecording();

            MidNumTranslateDto recordDto = MidNumTranslateDto.builder()
                    .channelBindId(recordReq.getBindId())
                    .channelRecordId(recordReq.getCallId())
                    .caller(recordReq.getCaller().substring(4))//返回数据前面有0086
                    .callee(recordReq.getCallee().substring(4))//返回数据前面有0086
                    .callerShow(recordReq.getCallee().substring(4))
                    .calledShow(recordReq.getDstVirtualNum().substring(4))
                    .calledArea(recordReq.getDstVirtualNum().substring(4))

                    .billDuration(billDuration)
                    .result(translateCallStatus(billDuration, recordReq.getCallEndStatus()))

                    .beginTime(new Date(Long.parseLong(recordReq.getCallCenterAcceptTime())))
                    .alertingTime(new Date(Long.parseLong(recordReq.getStartDstRingTime())))
                    .connectTime(StringUtils.isNotBlank(dstAcceptTime) ?
                            new Date(Long.parseLong(dstAcceptTime)) : null)
                    .releaseTime(StringUtils.isNotBlank(endCallTime) ?
                            new Date(Long.parseLong(endCallTime)) : null)

                    .callRecording(callRecording)
                    .recordFileUrl(recordReq.getRecordUrl())
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
            if (callDuration > 0 || "1".equals(status)) {
                return MidNumCallStatusEnum.NORMAL_HANGUP.getValue();
            }

            switch (status) {
                case "4":
                    return MidNumCallStatusEnum.CALLED_NO_ANSWER.getValue();
                case "3":
                    return MidNumCallStatusEnum.CALLED_OUT_OF_REACH.getValue();
                case "2":
                    return MidNumCallStatusEnum.CALLED_OUT_OF_REACH.getValue();
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

    /**
     * 发送Http请求
     *
     * @param url
     * @param data
     * @param headers
     * @return
     */
    private String sendRequest(String url, Object data, Map<String, String> headers) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8");
        headers.put("Accept", "application/json;charset=UTF-8");

        JSONObject jsonObject = FastJsonUtils.parseObject(FastJsonUtils.toJSONNoFeatures(data));
        String resultString = HttpUtil.doPost(url, jsonObject.toString(), headers, true);
        return resultString;
    }

}
