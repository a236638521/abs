package com.m7.abs.api.projectHandler.midNum.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.m7.abs.api.domain.dto.midNum.MidMumConfigDto;
import com.m7.abs.api.domain.dto.midNum.MidNumRecordRespDto;
import com.m7.abs.api.domain.dto.midNum.MidNumSmsRecordRespDto;
import com.m7.abs.api.domain.dto.midNum.MidNumTranslateDto;
import com.m7.abs.api.domain.vo.midNum.*;
import com.m7.abs.api.projectHandler.midNum.IMidNumHandler;
import com.m7.abs.api.projectHandler.midNum.MIdNumType;
import com.m7.abs.api.projectHandler.midNum.model.tyzn.*;
import com.m7.abs.common.constant.common.MidNumCallStatusEnum;
import com.m7.abs.common.constant.common.MidNumType;
import com.m7.abs.common.constant.error.code.ErrorCodeConstant;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.protocol.HTTP;

import java.util.*;

@Slf4j
@MIdNumType(channel = MidNumType.TYZN)
public class MidNumHandlerTYZN implements IMidNumHandler {

    /**
     * 天眼智能小号平台AXB绑定
     *
     * @param midMumConfigDto
     * @param requestVO
     * @return
     */
    @Override
    public BaseResponse<BindResponseVO> axbBind(MidMumConfigDto midMumConfigDto, BindAXBRequestVO requestVO) {
        try {
            String url = midMumConfigDto.getBasePath() + "/api/axb/bind";

            //icDisplayFlag "0"显真实号码 "1"显示中间号 默认 "1" String形式
            String icDisplayFlag = "";
            Integer icDisplayFlag_ = requestVO.getIcDisplayFlag();
            if (icDisplayFlag_ == null || icDisplayFlag_ == 1) {
                icDisplayFlag = "0,0";
            } else {
                icDisplayFlag = "0,1";
            }

            TYZNAXBReq tyznaxbReq = TYZNAXBReq.builder()
                    .telA(requestVO.getTelA())
                    .telB(requestVO.getTelB())
                    .telX(requestVO.getTelX())
                    .callDisplay(icDisplayFlag)//calldisplay的值，0或者不填显示中间号码 1全部显示为真实号码
                    .expiration(String.valueOf(requestVO.getExpiration()))//主被叫+虚拟保护号码允许合作方最大 cache 存储时 间(单位秒)，超过该时间绑定关系失效，拨叫无法接通
                    .callRecording(requestVO.isNeedRecord() ? "1" : "0")
//                    .anucode()
                    .build();

            String resultStr = sendRequest(midMumConfigDto, url, tyznaxbReq, null);
            if (StringUtils.isNotEmpty(resultStr)) {
//                TYZNResp<TYZNAXBRespData> result = FastJsonUtils.toBean(resultStr, TYZNResp.class);
                TYZNResp<TYZNAXBRespData> result = JSON.parseObject(resultStr, new TypeReference<TYZNResp<TYZNAXBRespData>>() {
                });
                Integer responseCode = result.getCode();
                if (responseCode != null && responseCode.equals(0)) {
                    BindResponseVO bindResponseVO = new BindResponseVO();
                    TYZNAXBRespData data = result.getData();
                    String subId = data.getSubId();
                    bindResponseVO.setMappingId(subId);
                    return BaseResponse.success(bindResponseVO);
                } else {
                    return BaseResponse.fail(result.getMessage());
                }
            }
            return BaseResponse.fail("[" + MidNumType.TYZN + "] BindAXB FAIL");
        } catch (Exception e) {
            log.error("[" + MidNumType.TYZN + "] BindAXB Error", e);
            return BaseResponse.error("[" + MidNumType.TYZN + "] BindAXB Error");
        }
    }

    /**
     * 天眼智能小号平台XB绑定
     *
     * @param midMumConfigDto
     * @param requestVO
     * @return
     */
    @Override
    public BaseResponse<BindResponseVO> axBind(MidMumConfigDto midMumConfigDto, BindAXRequestVO requestVO) {
        try {
            String url = midMumConfigDto.getBasePath() + "/api/axb/outTransfer";

            String bindId = MyStringUtils.randomUUID();

            //icDisplayFlag "0"显真实号码 "1"显示中间号 默认 "1" String形式
            String icDisplayFlag = "";
            Integer icDisplayFlag_ = requestVO.getIcDisplayFlag();
            if (icDisplayFlag_ == null || icDisplayFlag_ == 1) {
                icDisplayFlag = "0";
            } else {
                icDisplayFlag = "1";
            }

            TYZNXBReq tyznaxbReq = TYZNXBReq.builder()
                    .telA(requestVO.getTelA())
                    .telX(requestVO.getTelX())
                    .callDisplay(icDisplayFlag)//calldisplay的值，0或者不填显示中间号码 1全部显示为真实号码
                    .expiration(String.valueOf(requestVO.getExpiration()))//主被叫+虚拟保护号码允许合作方最大 cache 存储时 间(单位秒)，超过该时间绑定关系失效，拨叫无法接通
                    .callRecording(requestVO.isNeedRecord() ? "1" : "0")
                    .remark(bindId)
                    .build();

            String resultStr = sendRequest(midMumConfigDto, url, tyznaxbReq, null);
            if (StringUtils.isNotEmpty(resultStr)) {
                TYZNResp<TYZNAXBRespData> result = FastJsonUtils.toBean(resultStr, TYZNResp.class);
                Integer responseCode = result.getCode();
                if (responseCode != null && responseCode.equals(0)) {
                    BindResponseVO bindResponseVO = new BindResponseVO();
                    bindResponseVO.setMappingId(bindId);
                    return BaseResponse.success(bindResponseVO);
                } else {
                    return BaseResponse.fail(result.getMessage());
                }
            }
            return BaseResponse.fail("[" + MidNumType.TYZN + "] BindXB FAIL");
        } catch (Exception e) {
            log.error("[" + MidNumType.TYZN + "] BindXB Error", e);
            return BaseResponse.error("[" + MidNumType.TYZN + "] BindXB Error");
        }
    }

    /**
     * 天眼智能小号平台AXB解绑
     *
     * @param accountDto
     * @param requestVO
     * @return
     */
    @Override
    public BaseResponse unBindAXB(MidMumConfigDto accountDto, UnBindRequestVO requestVO) {
        try {


            String url = accountDto.getBasePath() + "/api/axb/unBind/" + requestVO.getAssociationId();

            TYZNAXBAndXBUnbindReq unbindReq = TYZNAXBAndXBUnbindReq.builder()
                    .telX(requestVO.getTelX())
                    .build();

            String resultStr = sendRequest(accountDto, url, unbindReq, null);
            if (StringUtils.isNotEmpty(resultStr)) {
                TYZNResp<TYZNAXBRespData> result = FastJsonUtils.toBean(resultStr, TYZNResp.class);
                Integer responseCode = result.getCode();
                if (responseCode != null && responseCode.equals(0)) {
                    return BaseResponse.success();
                } else {
                    return BaseResponse.fail(result.getMessage());
                }
            }
            return BaseResponse.fail("[" + MidNumType.TYZN + "] unBindAXB FAIL");
        } catch (Exception e) {
            log.error("[" + MidNumType.TYZN + "] unBindAXB Error", e);
            return BaseResponse.error("[" + MidNumType.TYZN + "] unBindAXB Error");
        }
    }

    /**
     * 天眼智能小号平台XB解绑
     *
     * @param accountDto
     * @param requestVO
     * @return
     */
    @Override
    public BaseResponse axUnBind(MidMumConfigDto accountDto, UnBindRequestVO requestVO) {
        try {


            String url = accountDto.getBasePath() + "/api/axb/outTransfer/" + requestVO.getTelX();

            TYZNAXBAndXBUnbindReq tyznaxbAndXBUnbindReq = TYZNAXBAndXBUnbindReq.builder()
                    .telX(requestVO.getTelX())
                    .build();

            String resultStr = sendRequest(accountDto, url, tyznaxbAndXBUnbindReq, null);
            if (StringUtils.isNotEmpty(resultStr)) {
                TYZNResp<TYZNAXBRespData> result = FastJsonUtils.toBean(resultStr, TYZNResp.class);
                Integer responseCode = result.getCode();
                if (responseCode != null && responseCode.equals(0)) {
                    return BaseResponse.success();
                } else {
                    return BaseResponse.fail(result.getMessage());
                }
            }
            return BaseResponse.fail("[" + MidNumType.TYZN + "] unBindAx FAIL");
        } catch (Exception e) {
            log.error("[" + MidNumType.TYZN + "] unBindAx Error", e);
            return BaseResponse.error("[" + MidNumType.TYZN + "] unBindAx Error");
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
            TYZNRecordReq recordReq = FastJsonUtils.toBean(reqJsonStr, TYZNRecordReq.class);

            String recordUrl = recordReq.getRecordUrl();
            if (StringUtils.isNotBlank(recordUrl) && !recordUrl.startsWith("http://")) {
                recordUrl = "http://" + recordUrl;
            }

            String releaseCause = recordReq.getReleaseCause();
            String appId = recordReq.getAppId();
            String mappingId = recordReq.getSubId();

            if (mappingId.equals("0")) {
                mappingId = recordReq.getRemark();
            }

            long billDuration = recordReq.getBillDuration();
            String startTime = recordReq.getStartTime();
            String releaseTime = recordReq.getReleaseTime();

            String caller = recordReq.getTelA();
            String called = recordReq.getTelB();
            String callType = recordReq.getCallType();
            if (StringUtils.isNotBlank(callType) && callType.equals("21")) {//回呼类型话单
                caller = recordReq.getTelB();
                called = recordReq.getTelA();
            }

            MidNumTranslateDto recordDto = MidNumTranslateDto.builder()
                    .channelBindId(mappingId)
                    .channelRecordId(recordReq.getCallId())
                    .caller(caller)
                    .callee(called)
                    .telX(recordReq.getTelX())
                    .result(translateCallStatus(billDuration, releaseCause))
                    .callDisplay("1")//0显真实号 1 不显真实号 2强制不显真实号
                    .callRecording(StringUtils.isNotBlank(recordUrl) ? true : false)
                    .billDuration(billDuration)
                    .beginTime(DateUtil.parseStrToDate(recordReq.getCallTime(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS))
                    .releaseTime(DateUtil.parseStrToDate(releaseTime, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS))
                    .connectTime(DateUtil.parseStrToDate(startTime, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS))
                    .alertingTime(DateUtil.parseStrToDate(recordReq.getRingingTime(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS))
                    .calledShow(recordReq.getTelX())
                    .callerArea("")
                    .calledArea("")
                    .recordFileUrl(recordUrl)
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


    /**
     * 释放原因
     * 000 0001（1） 未分配的号码
     * 000 0010（2） 无路由到指定的转接网
     * 000 0011（3） 无路由到目的地
     * 000 0100（4） 发送专用信息音
     * 001 0000（16） 正常的呼叫拆线
     * 001 0001（17） 用户忙
     * 001 0010（18） 用户未响应
     * 001 0011（19） 用户未应答
     * 001 0100（20） 用户缺席
     * 001 0101（21） 呼叫拒收
     * 001 0110（22） 号码改变
     * 001 1011（27） 目的地不可达
     * 001 1100（28） 无效的号码格式（地址不全）
     * 001 1101（29） 性能拒绝
     * 001 1111（31） 正常—未指定类别 010，资源不可用
     * 010 0010（34） 无电路/通路可用
     * 010 1010（42） 交换设备拥塞
     * 011 0010（50） 所请求的性能未预定
     * 011 0101（53） CUG 中限制去呼叫
     * 011 0111（55） CUG 中限制来呼叫
     * 011 1001（57） 承载能力无权
     * 011 1010（58） 承载能力目前不可用
     * 类别 100，业务或任选未实现类：
     * 100 0001（65） 承载能力未实现
     * 100 0101（69） 所请求的性能未实现
     * 类别 101，无效的消息（例如参数超出范围）类：
     * 101 0111（87） 被叫用户不是CUG 的成员
     * 101 1000（88） 不兼容的目的地
     * 101 1010（90） 不存在的 CUG
     * 101 1011（91） 无效的转接网选择
     * 101 1111（95） 无效的消息，未指定
     * 类别 110，协议错误（例如未知的消息）类：
     * 110 0001（97） 消息类型不存在或未实现
     * 110 0011（99） 参数不存在或未实现
     * 110 0110（102）定时器终了时恢复
     * 110 0101（103）参数不存在或未实现—传递
     * 110 1110（110）消息带有未被识别的参数—舍弃
     * 110 1111（111）协议错误，未指定
     * 类别 111，互通类：
     * 111 1111（127）互通，未指定
     * 类别 1100 、 1101 ， 平台拒绝类：
     * 1100 1010（202）用户忙，MSRN
     * 获取失败，平台挂机
     * 1100 1011（203）用户去活，平台挂机
     * 1100 1100（204）用户在平台侧关机，平台挂机
     * 1100 1101（205）用户未开户，平台挂机
     * 1100 1110（206）小号不允许呼叫，平台挂机
     * 1100 1111（207）主号拨打小号，平台挂机
     * 1101 0001（209）主叫打小号带原始被叫，平台挂机
     *
     * @param callDuration
     * @param status
     * @return
     */
    private String translateCallStatus(long callDuration, String status) {

        if (StringUtils.isNotEmpty(status)) {
            if (callDuration > 0 || "16".equals(status) || "31".equals(status)) {
                return MidNumCallStatusEnum.NORMAL_HANGUP.getValue();
            }

            switch (status) {
                case "18":
                    return MidNumCallStatusEnum.CALLED_NO_ANSWER.getValue();
                case "19":
                    return MidNumCallStatusEnum.CALLED_NO_ANSWER.getValue();
                case "17":
                    return MidNumCallStatusEnum.CALLED_BUSY.getValue();
                case "27":
                    return MidNumCallStatusEnum.CALLED_OUT_OF_REACH.getValue();
                case "2":
                    return MidNumCallStatusEnum.ROUTING_FAILED.getValue();
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
     * 获取指定长度的随机字符串
     *
     * @param length 字符串长度
     * @return String
     */
    public static String getRandomString(Integer length) {
        int lengthTemp = 32;
        if (!org.springframework.util.StringUtils.isEmpty(length)) {
            lengthTemp = length;
        }
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int index = 0; index < lengthTemp; index++) {
            if (random.nextInt(2) % 2 == 0) {
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;// 取大写字母还是小写字母
                builder.append((char) (choice + random.nextInt(26)));
            } else {
                builder.append(random.nextInt(10));
            }
        }
        return builder.toString();
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

        String appId = midMumConfigDto.getAppId();
        String ts = String.valueOf(new Date().getTime());
        String RequestId = getRandomString(16);

        headers.put(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8");
        headers.put("Accept", "application/json;charset=UTF-8");
        headers.put("AppId", appId);
        headers.put("Ts", ts);
        headers.put("RequestId", RequestId);
        headers.put("Authorization", MD5Util.md5Encrypt32Lower(appId + ts + RequestId + midMumConfigDto.getSecretKey()).toLowerCase());

        JSONObject jsonObject = FastJsonUtils.parseObject(FastJsonUtils.toJSONNoFeatures(data));
        String resultString = HttpUtil.doPost(url, jsonObject.toString(), headers, true);
        return resultString;
    }

}
