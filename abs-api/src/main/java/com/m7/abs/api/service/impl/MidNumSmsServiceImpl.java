package com.m7.abs.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.m7.abs.api.domain.dto.midNum.MidNumSmsRecordRespDto;
import com.m7.abs.api.domain.dto.midNum.MidNumSmsTranslateDto;
import com.m7.abs.api.projectHandler.midNum.IMidNumHandler;
import com.m7.abs.api.projectHandler.midNum.MIdNumType;
import com.m7.abs.api.service.IAsyncService;
import com.m7.abs.api.service.IMidNumSmsService;
import com.m7.abs.api.service.IMiddleNumberBindLogService;
import com.m7.abs.common.constant.error.code.ErrorCodeConstant;
import com.m7.abs.common.constant.keys.CommonSessionKeys;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.MiddleNumberBindLogEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author kejie peng
 */
@Slf4j
@Service
public class MidNumSmsServiceImpl implements IMidNumSmsService {
    private Map<String, IMidNumHandler> midNumHandleMap;
    @Autowired
    private IMiddleNumberBindLogService iMiddleNumberBindLogService;
    @Autowired
    private IAsyncService asyncService;

    @Autowired
    public void setMidNumHandleMap(List<IMidNumHandler> handlers) {
        // 注入各种类型的handler
        midNumHandleMap = handlers.stream().collect(
                Collectors.toMap(handler -> AnnotationUtils.findAnnotation(handler.getClass(), MIdNumType.class).channel(),
                        v -> v, (v1, v2) -> v1));
    }


    @Override
    public Object record(String channelCode, Map<String, Object> requestVO) {
        //根据channelCode获取执行handler
        IMidNumHandler midNumHandler = midNumHandleMap.get(channelCode);
        if (midNumHandler == null) {
            return BaseResponse.fail("No such channelCode,please check your params!");
        }

        //TODO 翻译不同渠道返回的消息,转换成固定对象
        BaseResponse<MidNumSmsRecordRespDto> translateResult = midNumHandler.translateSmsRecord(requestVO);
        String code = translateResult.getCode();
        MidNumSmsRecordRespDto data = translateResult.getData();

        if (StringUtils.isNotEmpty(code) && ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode().equals(code)) {//翻译成功
            //TODO 处理话单
            MidNumSmsTranslateDto recordDto = data.getTranslateDto();//话单固定参数对象
            String channelBindId = recordDto.getChannelBindId();

            if (StringUtils.isEmpty(channelBindId)) {
                return BaseResponse.fail("[" + channelCode + "] bindId not find.");
            }

            /**
             * 1.通过绑定记录ID获取小号绑定记录
             */
            LambdaQueryWrapper<MiddleNumberBindLogEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(MiddleNumberBindLogEntity::getAssociationId, channelBindId);
            queryWrapper.last("LIMIT 1");
            MiddleNumberBindLogEntity bindLogEntity = iMiddleNumberBindLogService.getOne(queryWrapper);

            if (bindLogEntity == null) {
                return BaseResponse.fail("[" + channelCode + "] bind log not find.");
            }
            /**
             * 异步处理话单相关数据
             */
            asyncService.handleMidNumSmsRecord(MDC.get(CommonSessionKeys.REQ_ID_KEY), bindLogEntity, recordDto);

        }


        if (data != null) {
            return data.getRespData();
        }
        return BaseResponse.fail("[" + channelCode + "] Save fail.");
    }
}
