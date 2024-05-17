package com.m7.abs.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.m7.abs.api.common.constant.RedisKeys;
import com.m7.abs.common.utils.PhoneUtil;
import com.m7.abs.api.common.utils.RedisUtil;
import com.m7.abs.api.domain.dto.ChannelConfigDto;
import com.m7.abs.api.domain.dto.midNum.*;
import com.m7.abs.api.domain.vo.midNum.*;
import com.m7.abs.api.projectHandler.midNum.IMidNumHandler;
import com.m7.abs.api.projectHandler.midNum.MIdNumType;
import com.m7.abs.api.service.*;
import com.m7.abs.common.constant.common.BindStatusEnum;
import com.m7.abs.common.constant.common.BindTypeEnum;
import com.m7.abs.common.constant.common.DataStatusEnum;
import com.m7.abs.common.constant.error.code.ErrorCodeConstant;
import com.m7.abs.common.constant.keys.CommonSessionKeys;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.AccountEntity;
import com.m7.abs.common.domain.entity.AccountFlashSmConfEntity;
import com.m7.abs.common.domain.entity.MiddleNumberBindLogEntity;
import com.m7.abs.common.utils.DateUtil;
import com.m7.abs.common.utils.MyStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hulin
 */
@Slf4j
@Service
public class MidNumServiceImpl implements IMidNumService {
    /**
     * 小号支持类型的分隔符
     */
    private static final String SPLIT = ";";
    private Map<String, IMidNumHandler> midNumHandleMap;
    @Autowired
    private IMiddleNumberPoolService iMiddleNumberPoolService;
    @Autowired
    private IMiddleNumberBindLogService iMiddleNumberBindLogService;
    @Autowired
    private IChannelService channelService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IAsyncService asyncService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IAccountFlashSmConfService accountFlashSmConfService;


    @Autowired
    public void setMidNumHandleMap(List<IMidNumHandler> handlers) {
        // 注入各种类型的handler
        midNumHandleMap = handlers.stream().collect(
                Collectors.toMap(handler -> AnnotationUtils.findAnnotation(handler.getClass(), MIdNumType.class).channel(),
                        v -> v, (v1, v2) -> v1));
    }

    /**
     * 获取小号绑定请求的相关数据，以及业务是否满足判断
     *
     * @param type    中间号类型
     * @param account 账户ID
     * @param telX    中间号
     * @return
     */
    private BaseResponse<MidNumReqDto> getBindConfInfo(String type, String account, String telX, String channelCode) {
        if (StringUtils.isEmpty(telX) && StringUtils.isEmpty(channelCode)) {
            return BaseResponse.error(ErrorCodeConstant.CommonErrorCode.PARAM_ERR, "Param telX and channelCode must exist one.");
        }
        /**
         * 优先使用channelCode获取指定招呼配置信息,
         * 次之通过指定小号获取账号信息
         */
        if (StringUtils.isNotEmpty(channelCode)) {
            /**
             * 通过通道编码获取配置信息
             * 不会校验小号是否可用
             */
            return getBindConfInfoByChannelCode(type, account, channelCode);
        } else if (StringUtils.isNotEmpty(telX)) {
            /**
             * 通过小号获取配置信息
             */
            return getBindConfInfoByTelX(type, account, telX);
        } else {
            return BaseResponse.error(ErrorCodeConstant.CommonErrorCode.PARAM_ERR);
        }
    }

    private BaseResponse<MidNumReqDto> getBindConfInfoByTelX(String type, String account, String telX) {
        AccountEntity accountEntity = accountService.getByBillAccountId(account);
        if (accountEntity == null) {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.ACCOUNT_NOT_FOUND);
        }
        //在小号池中查询小号
        MiddleNumberPoolDto middleNumberPool = iMiddleNumberPoolService.getByAccountAndTelX(account, telX);
        if (middleNumberPool == null) {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.MID_NOT_FOUND);
        }

        //判断类型是否支持
        if (!Arrays.asList(middleNumberPool.getType().split(SPLIT)).contains(type)) {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.BIND_TYPE_NOT_SUPPORT);
        }

        //获取channel配置
        ChannelConfigDto channelConfigDto = channelService.getChannelConfigInfo(middleNumberPool.getChannelId(), middleNumberPool.getAccountId());

        if (channelConfigDto == null) {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.CHANNEL_CONFIG_NOT_FOUND);
        }

        //根据channelCode获取执行handler
        IMidNumHandler midNumHandler = midNumHandleMap.get(channelConfigDto.getChannelCode());
        if (midNumHandler == null) {
            return BaseResponse.fail("No such channelCode,please check your params!");
        }

       /* if (StringUtils.isEmpty(channelConfigDto.getAppId())) {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.ACCOUNT_CHANNEL_CONFIG_NOT_FOUND);
        }*/

        MidMumConfigDto accountDto = MidMumConfigDto.builder()
                .appId(channelConfigDto.getAppId())
                .accessKey(channelConfigDto.getAccessKey())
                .secretKey(channelConfigDto.getSecretKey())
                .accountId(accountEntity.getId())
                .channelId(channelConfigDto.getChannelId())
                .channelCode(channelConfigDto.getChannelCode())
                .build();
        accountDto.setBasePath(channelConfigDto.getHost(), channelConfigDto.getPort(), channelConfigDto.getContextPath());

        MidNumReqDto midNumReqDto = MidNumReqDto.builder()
                .midNumHandler(midNumHandler)
                .midMumConfigDto(accountDto)
                .areaCode(middleNumberPool.getAreaCode())
                .build();

        return BaseResponse.success(midNumReqDto);
    }

    private BaseResponse<MidNumReqDto> getBindConfInfoByChannelCode(String type, String account, String channelCode) {
        AccountEntity accountEntity = accountService.getByBillAccountId(account);
        if (accountEntity == null) {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.ACCOUNT_NOT_FOUND);
        }

        //获取channel配置
        ChannelConfigDto channelConfigDto = channelService.getChannelConfigInfoByAccountAndChannelCode(account, channelCode);

        if (channelConfigDto == null) {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.CHANNEL_CONFIG_NOT_FOUND);
        }

        //根据channelCode获取执行handler
        IMidNumHandler midNumHandler = midNumHandleMap.get(channelConfigDto.getChannelCode());
        if (midNumHandler == null) {
            return BaseResponse.fail("No such channelCode,please check your params!");
        }

/*        if (StringUtils.isEmpty(channelConfigDto.getAppId())) {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.ACCOUNT_CHANNEL_CONFIG_NOT_FOUND);
        }*/

        MidMumConfigDto accountDto = MidMumConfigDto.builder()
                .appId(channelConfigDto.getAppId())
                .accessKey(channelConfigDto.getAccessKey())
                .secretKey(channelConfigDto.getSecretKey())
                .accountId(accountEntity.getId())
                .channelId(channelConfigDto.getChannelId())
                .channelCode(channelConfigDto.getChannelCode())
                .build();
        accountDto.setBasePath(channelConfigDto.getHost(), channelConfigDto.getPort(), channelConfigDto.getContextPath());

        MidNumReqDto midNumReqDto = MidNumReqDto.builder()
                .midNumHandler(midNumHandler)
                .midMumConfigDto(accountDto)
                .build();

        return BaseResponse.success(midNumReqDto);
    }

    @Override
    public BaseResponse axbBind(BindAXBRequestVO requestVO) {
        boolean reusable = requestVO.isReusable();

        /**
         *  校验主被叫是否存在绑定关系
         */
        if (reusable) {
            BaseResponse checkReusable = checkReusable(requestVO.getAccount(), requestVO.getTelA(), requestVO.getTelB());
            if (ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode().equals(checkReusable.getCode())) {
                return checkReusable;
            }
        }


        //获取小号对应的配置信息
        BaseResponse<MidNumReqDto> bindConfInfo = this.getBindConfInfo(BindTypeEnum.AXB.getType(), requestVO.getAccount(), requestVO.getTelX(), requestVO.getChannelCode());

        if (!ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode().equals(bindConfInfo.getCode())) {
            return bindConfInfo;
        }
        MidNumReqDto midNumReqDto = bindConfInfo.getData();
        MidMumConfigDto midMumConfigDto = midNumReqDto.getMidMumConfigDto();


        Integer areaCode = requestVO.getAreaCode();
        if (areaCode == null && StringUtils.isNotEmpty(midNumReqDto.getAreaCode())) {
            requestVO.setAreaCode(Integer.valueOf(midNumReqDto.getAreaCode()));
        }

        //查询小号的绑定关系
        if (StringUtils.isNotEmpty(requestVO.getTelX())) {
            LambdaQueryWrapper<MiddleNumberBindLogEntity> bindLogWrapper = new LambdaQueryWrapper<>();
            bindLogWrapper.eq(MiddleNumberBindLogEntity::getAccountId, midMumConfigDto.getAccountId());
            bindLogWrapper.eq(MiddleNumberBindLogEntity::getTelA, requestVO.getTelA());
            bindLogWrapper.eq(MiddleNumberBindLogEntity::getTelB, requestVO.getTelB());
            bindLogWrapper.eq(MiddleNumberBindLogEntity::getTelX, requestVO.getTelX());
            bindLogWrapper.eq(MiddleNumberBindLogEntity::getBindType, BindTypeEnum.AXB.getType());
            bindLogWrapper.eq(MiddleNumberBindLogEntity::getStatus, BindStatusEnum.BINDING.getType());

            List<MiddleNumberBindLogEntity> bindLogList = iMiddleNumberBindLogService.list(bindLogWrapper);
            if (bindLogList != null && bindLogList.size() > 0) {
                //TODO 解绑操作
                bindLogList.stream().forEach(item -> {
                    UnBindRequestVO unBindRequestVO = new UnBindRequestVO();
                    unBindRequestVO.setMappingId(item.getId());
                    unBindRequestVO.setTelX(item.getTelX());
                    this.unBindAXB(unBindRequestVO);
                });
            }
        }

        //TODO 请求绑定

        BaseResponse<BindResponseVO> binResponse = midNumReqDto.getMidNumHandler().axbBind(midMumConfigDto, requestVO);

        //绑定成功后
        if (binResponse != null && ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode().equals(binResponse.getCode())) {
            BindResponseVO data = binResponse.getData();

            String telX = requestVO.getTelX();
            if (StringUtils.isEmpty(telX)) {
                telX = data.getTelX();
            }
            Date createTime = new Date();
            Long expiration = requestVO.getExpiration();

            MiddleNumberBindLogEntity saveBindLog = MiddleNumberBindLogEntity.builder()
                    .recordReceiveUrl(requestVO.getRecordReceiveUrl())
                    .accountId(midMumConfigDto.getAccountId())
                    .billAccountId(requestVO.getAccount())
                    .channelId(midMumConfigDto.getChannelId())
                    .associationId(data.getMappingId())
                    .callRecording(requestVO.isNeedRecord())
                    .expiration(expiration)
                    .status(BindStatusEnum.BINDING.getType())
                    .telA(requestVO.getTelA())
                    .telX(telX)
                    .telB(requestVO.getTelB())
                    .bindType(BindTypeEnum.AXB.getType())
                    .userData(requestVO.getUserData())
                    .createTime(createTime)
                    .channelCode(midMumConfigDto.getChannelCode())
                    .build();

            Date expireTime = null;
            if (expiration != null) {
                expireTime = DateUtil.getNextTime(createTime, expiration.intValue());
                saveBindLog.setExpireTime(expireTime);
            }

            saveBindLog.setId(MyStringUtils.randomUUID());
            log.info("bind info:{}", saveBindLog);
            //保存绑定记录
            iMiddleNumberBindLogService.save(saveBindLog);

            BindResponseVO bindResponseVO = BindResponseVO.builder()
                    .mappingId(saveBindLog.getId())
                    .expireTime(expireTime)
                    .build();
            if (StringUtils.isNotEmpty(data.getTelX())) {
                bindResponseVO.setTelX(data.getTelX());
            } else {
                bindResponseVO.setTelX(telX);
            }
            return BaseResponse.success(bindResponseVO);
        } else {
            return binResponse;
        }
    }

    @Override
    public BaseResponse axBind(BindAXRequestVO requestVO) {

        //获取小号对应的配置信息
        BaseResponse<MidNumReqDto> bindConfInfo = this.getBindConfInfo(BindTypeEnum.AX.getType(), requestVO.getAccount(), requestVO.getTelX(), requestVO.getChannelCode());

        if (!ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode().equals(bindConfInfo.getCode())) {
            return bindConfInfo;
        }
        MidNumReqDto midNumReqDto = bindConfInfo.getData();
        MidMumConfigDto midMumConfigDto = midNumReqDto.getMidMumConfigDto();
        Integer areaCode = requestVO.getAreaCode();
        if (areaCode == null && StringUtils.isNotEmpty(midNumReqDto.getAreaCode())) {
            requestVO.setAreaCode(Integer.valueOf(midNumReqDto.getAreaCode()));
        }

        //查询小号的绑定关系
        if (StringUtils.isNotEmpty(requestVO.getTelX())) {
            LambdaQueryWrapper<MiddleNumberBindLogEntity> bindLogWrapper = new LambdaQueryWrapper<>();
            bindLogWrapper.eq(MiddleNumberBindLogEntity::getAccountId, midMumConfigDto.getAccountId());
            bindLogWrapper.eq(MiddleNumberBindLogEntity::getTelX, requestVO.getTelX());
            bindLogWrapper.eq(MiddleNumberBindLogEntity::getBindType, BindTypeEnum.AX.getType());
            bindLogWrapper.eq(MiddleNumberBindLogEntity::getStatus, BindStatusEnum.BINDING.getType());
            List<MiddleNumberBindLogEntity> bindList = iMiddleNumberBindLogService.list(bindLogWrapper);
            if (bindList != null && bindList.size() > 0) {
                //TODO 解绑操作
                bindList.stream().forEach(item -> {
                    UnBindRequestVO unBindRequestVO = new UnBindRequestVO();
                    unBindRequestVO.setMappingId(item.getId());
                    unBindRequestVO.setTelX(item.getTelX());
                    this.axUnBind(unBindRequestVO);
                });
            }
        }


        //TODO 请求绑定
        BaseResponse<BindResponseVO> binResponse = midNumReqDto.getMidNumHandler().axBind(midMumConfigDto, requestVO);

        if (binResponse != null && ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode().equals(binResponse.getCode())) {
            BindResponseVO data = binResponse.getData();

            String telX = requestVO.getTelX();
            if (StringUtils.isEmpty(telX)) {
                telX = data.getTelX();
            }

            Date createTime = new Date();
            Long expiration = requestVO.getExpiration();

            MiddleNumberBindLogEntity saveBindLog = MiddleNumberBindLogEntity.builder()
                    .accountId(midMumConfigDto.getAccountId())
                    .billAccountId(requestVO.getAccount())
                    .recordReceiveUrl(requestVO.getRecordReceiveUrl())
                    .channelId(midMumConfigDto.getChannelId())
                    .associationId(data.getMappingId())
                    .callRecording(requestVO.isNeedRecord())
                    .expiration(requestVO.getExpiration())
                    .status(BindStatusEnum.BINDING.getType())
                    .telX(telX)
                    .telA(requestVO.getTelA())
                    .bindType(BindTypeEnum.AX.getType())
                    .userData(requestVO.getUserData())
                    .channelCode(midMumConfigDto.getChannelCode())
                    .createTime(createTime)
                    .build();
            saveBindLog.setId(MyStringUtils.randomUUID());
            Date expireTime = null;
            if (expiration != null) {
                expireTime = DateUtil.getNextTime(createTime, expiration.intValue());
                saveBindLog.setExpireTime(expireTime);
            }
            log.info("bind info:{}", saveBindLog);
            ////保存绑定记录
            iMiddleNumberBindLogService.save(saveBindLog);

            BindResponseVO bindResponseVO = BindResponseVO.builder()
                    .mappingId(saveBindLog.getId())
                    .expireTime(expireTime)
                    .build();
            if (StringUtils.isNotEmpty(data.getTelX())) {
                bindResponseVO.setTelX(data.getTelX());
            } else {
                bindResponseVO.setTelX(requestVO.getTelX());
            }
            return BaseResponse.success(bindResponseVO);
        } else {
            return binResponse;
        }


    }

    @Override
    public BaseResponse unBindAXB(UnBindRequestVO requestVO) {

        //查询绑定信息
        BaseResponse<MiddleNumberBindLogEntity> bindLogResponse = iMiddleNumberBindLogService.getUnbindLog(requestVO.getMappingId());
        if (!ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode().equals(bindLogResponse.getCode())) {
            return bindLogResponse;
        }
        MiddleNumberBindLogEntity bindInfo = bindLogResponse.getData();
        requestVO.setAssociationId(bindInfo.getAssociationId());

        //获取小号对应的配置信息
        BaseResponse<MidNumReqDto> bindConfInfo = this.getBindConfInfo(BindTypeEnum.AXB.getType(), bindInfo.getBillAccountId(), bindInfo.getTelX(), bindInfo.getChannelCode());
        if (!ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode().equals(bindConfInfo.getCode())) {
            return bindConfInfo;
        }
        MidNumReqDto midNumReqDto = bindConfInfo.getData();

        //TODO 解绑操作
        BaseResponse<BindResponseVO> binResponse = midNumReqDto.getMidNumHandler().unBindAXB(midNumReqDto.getMidMumConfigDto(), requestVO);
        if (binResponse != null && ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode().equals(binResponse.getCode())) {
            LambdaUpdateWrapper<MiddleNumberBindLogEntity> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(MiddleNumberBindLogEntity::getId, requestVO.getMappingId());
            updateWrapper.set(MiddleNumberBindLogEntity::getLastUpdateTime, new Date());
            updateWrapper.set(MiddleNumberBindLogEntity::getStatus, BindStatusEnum.UNBIND.getType());
            iMiddleNumberBindLogService.update(updateWrapper);
            return BaseResponse.success();
        } else {
            return binResponse;
        }
    }

    @Override
    public BaseResponse axUnBind(UnBindRequestVO requestVO) {
        //查询绑定信息
        BaseResponse<MiddleNumberBindLogEntity> bindLogResponse = iMiddleNumberBindLogService.getUnbindLog(requestVO.getMappingId());
        if (!ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode().equals(bindLogResponse.getCode())) {
            return bindLogResponse;
        }
        MiddleNumberBindLogEntity bindInfo = bindLogResponse.getData();
        requestVO.setAssociationId(bindInfo.getAssociationId());

        //获取小号对应的配置信息
        BaseResponse<MidNumReqDto> bindConfInfo = this.getBindConfInfo(BindTypeEnum.AX.getType(), bindInfo.getBillAccountId(), bindInfo.getTelX(), bindInfo.getChannelCode());
        if (!ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode().equals(bindConfInfo.getCode())) {
            return bindConfInfo;
        }
        MidNumReqDto midNumReqDto = bindConfInfo.getData();

        //TODO 解绑操作
        BaseResponse<BindResponseVO> binResponse = midNumReqDto.getMidNumHandler().axUnBind(midNumReqDto.getMidMumConfigDto(), requestVO);
        if (binResponse != null && ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode().equals(binResponse.getCode())) {
            LambdaUpdateWrapper<MiddleNumberBindLogEntity> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(MiddleNumberBindLogEntity::getId, requestVO.getMappingId());
            updateWrapper.set(MiddleNumberBindLogEntity::getLastUpdateTime, new Date());
            updateWrapper.set(MiddleNumberBindLogEntity::getStatus, BindStatusEnum.UNBIND.getType());
            iMiddleNumberBindLogService.update(updateWrapper);
            return BaseResponse.success();
        } else {
            return binResponse;
        }
    }

    @Override
    public BaseResponse axDelay(DelayAxRequestVO requestVO) {
        //查询绑定信息
        BaseResponse<MiddleNumberBindLogEntity> bindLogResponse = iMiddleNumberBindLogService.getUnbindLog(requestVO.getMappingId());
        if (!ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode().equals(bindLogResponse.getCode())) {
            return bindLogResponse;
        }
        MiddleNumberBindLogEntity bindInfo = bindLogResponse.getData();
        requestVO.setAssociationId(bindInfo.getAssociationId());

        //获取小号对应的配置信息
        BaseResponse<MidNumReqDto> bindConfInfo = this.getBindConfInfo(BindTypeEnum.AX.getType(), bindInfo.getBillAccountId(), bindInfo.getTelX(), bindInfo.getChannelCode());
        if (!ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode().equals(bindConfInfo.getCode())) {
            return bindConfInfo;
        }
        MidNumReqDto midNumReqDto = bindConfInfo.getData();

        //TODO 延期操作
        BaseResponse<BindResponseVO> binResponse = midNumReqDto.getMidNumHandler().axDelay(midNumReqDto.getMidMumConfigDto(), requestVO);
        if (binResponse != null && ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode().equals(binResponse.getCode())) {
            Integer extraTime = requestVO.getExtraTime();
            if (extraTime != null) {
                /**
                 * XB延期单位为天,需要转换为秒
                 */
                extraTime = extraTime * 86400;
                iMiddleNumberBindLogService.delayExpiration(requestVO.getMappingId(), extraTime);
            }
            return BaseResponse.success();
        } else {
            return binResponse;
        }
    }

    @Override
    public BaseResponse axbDelay(DelayAXBRequestVO requestVO) {
        //查询绑定信息
        BaseResponse<MiddleNumberBindLogEntity> bindLogResponse = iMiddleNumberBindLogService.getUnbindLog(requestVO.getMappingId());
        if (!ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode().equals(bindLogResponse.getCode())) {
            return bindLogResponse;
        }
        MiddleNumberBindLogEntity bindInfo = bindLogResponse.getData();
        requestVO.setAssociationId(bindInfo.getAssociationId());
        //获取小号对应的配置信息
        BaseResponse<MidNumReqDto> bindConfInfo = this.getBindConfInfo(BindTypeEnum.AXB.getType(), bindInfo.getBillAccountId(), bindInfo.getTelX(), bindInfo.getChannelCode());
        if (!ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode().equals(bindConfInfo.getCode())) {
            return bindConfInfo;
        }
        MidNumReqDto midNumReqDto = bindConfInfo.getData();

        //TODO 延期操作
        BaseResponse<BindResponseVO> response = midNumReqDto.getMidNumHandler().axbDelay(midNumReqDto.getMidMumConfigDto(), requestVO);
        if (response != null && ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode().equals(response.getCode())) {
            iMiddleNumberBindLogService.delayExpiration(requestVO.getMappingId(), requestVO.getExtraTime());
            return BaseResponse.success();
        } else {
            return response;
        }
    }

    @Override
    public Object record(String channelCode, Map<String, Object> requestVO) {
        //根据channelCode获取执行handler
        IMidNumHandler midNumHandler = midNumHandleMap.get(channelCode);
        if (midNumHandler == null) {
            return BaseResponse.fail("No such channelCode,please check your params!");
        }

        //TODO 翻译不同渠道返回的消息,转换成固定对象
        BaseResponse<MidNumRecordRespDto> translateResult = midNumHandler.translateRecord(requestVO);
        String code = translateResult.getCode();
        MidNumRecordRespDto data = translateResult.getData();

        if (StringUtils.isNotEmpty(code) && ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode().equals(code)) {//翻译成功
            //TODO 处理话单
            MidNumTranslateDto recordDto = data.getTranslateDto();//话单固定参数对象
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
            asyncService.handleMidNumCdr(MDC.get(CommonSessionKeys.REQ_ID_KEY), bindLogEntity, recordDto);

        }


        if (data != null) {
            return data.getRespData();
        }
        return BaseResponse.fail("[" + channelCode + "] Save fail.");
    }

    @Override
    public Object recordUrl(String channelCode, Map<String, Object> requestVO) {
        //根据channelCode获取执行handler
        IMidNumHandler midNumHandler = midNumHandleMap.get(channelCode);
        if (midNumHandler == null) {
            return BaseResponse.fail("No such channelCode,please check your params!");
        }

        //TODO 翻译不同渠道返回的消息,转换成固定对象
        BaseResponse<MidNumRecordRespDto> translateResult = midNumHandler.translateRecordUrl(requestVO);

        String code = translateResult.getCode();
        MidNumRecordRespDto data = translateResult.getData();

        if (StringUtils.isNotEmpty(code) && ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode().equals(code)) {//翻译成功
            //TODO 处理录音
            MidNumRecordUrlTranslateDto recordDto = data.getRecordUrlDto();//录音固定参数对象

            String channelBindId = recordDto.getChannelBindId();

            if (StringUtils.isEmpty(channelBindId)) {
                return BaseResponse.fail("[" + channelCode + "] MappingId not find.");
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
             * 异步处理录音相关数据
             */
            asyncService.handleMidNumRecordUrl(MDC.get(CommonSessionKeys.REQ_ID_KEY), bindLogEntity, recordDto);

        }


        if (data != null) {
            return data.getRespData();
        }
        return BaseResponse.fail("[" + channelCode + "] Save fail.");
    }

    @Override
    public Object getRingTime(String channelCode, Map<String, Object> requestVO) {
        //根据channelCode获取执行handler
        IMidNumHandler midNumHandler = midNumHandleMap.get(channelCode);
        if (midNumHandler == null) {
            return BaseResponse.fail("No such channelCode,please check your params!");
        }

        //TODO 翻译不同渠道返回的消息,转换成固定对象
        BaseResponse<MidNumRecordRespDto> translateResult = midNumHandler.translateRingTime(requestVO);
        String code = translateResult.getCode();
        MidNumRecordRespDto data = translateResult.getData();

        if (StringUtils.isNotEmpty(code) && ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode().equals(code)) {//翻译成功
            //TODO 处理振铃时间
            MidNumRingTimeTranslateDto ringTimeDto = data.getRingTimeDto();
            String recordId = ringTimeDto.getChannelRecordId();
            Date ringTime = ringTimeDto.getRingTime();
            /**
             * 振铃时间临时数据
             * 存储一天
             */
            String s = DateUtil.parseDateToStr(ringTime, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
            redisUtil.set(RedisKeys.REDIS_MID_NUM_RINGTIME + "-" + recordId, s, 86400);
            log.info("cache ring time record:recordId " + recordId + ",ring time:" + s);

            /**
             * 1.通过绑定记录ID获取小号绑定记录
             */
            LambdaQueryWrapper<MiddleNumberBindLogEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(MiddleNumberBindLogEntity::getAssociationId, ringTimeDto.getChannelBindId());
            queryWrapper.last("LIMIT 1");
            MiddleNumberBindLogEntity bindLogEntity = iMiddleNumberBindLogService.getOne(queryWrapper);

            if (bindLogEntity == null) {
                return BaseResponse.fail("[" + channelCode + "] bind log not find.");
            }

            String accountId = bindLogEntity.getAccountId();
            LambdaQueryWrapper<AccountEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.and(orWrapper -> {
                orWrapper.eq(AccountEntity::getBillAccountId, accountId);
                orWrapper.or();
                orWrapper.eq(AccountEntity::getId, accountId);
            });
            wrapper.eq(AccountEntity::getStatus, DataStatusEnum.ENABLE.getValue());
            wrapper.last("LIMIT 1");
            AccountEntity accountEntity = accountService.getOne(wrapper);

            /**
             * 判断是否需要发送闪信
             */
            if (accountEntity != null && accountEntity.isFlashSmEnable()) {
                /**
                 * 发送闪信
                 * 触发条件:
                 * 1.A给B打电话时
                 * 2.B的号码为手机号
                 */
                String telB = bindLogEntity.getTelB();
                String telX = bindLogEntity.getTelX();
                String callee = ringTimeDto.getCallee();

                if (StringUtils.isNotEmpty(telB) && telB.equals(callee) && PhoneUtil.checkPhoneNumber(Long.parseLong(telB), 86)) {
                    log.info("MidNum send flash sm,account: {} ,recordId: {} ,mappingId: {},sender: {} ,target: {}", accountId, recordId, bindLogEntity.getId(), telX, telB);
                    LambdaQueryWrapper<AccountFlashSmConfEntity> confQueryWrapper = new LambdaQueryWrapper<>();
                    confQueryWrapper.eq(AccountFlashSmConfEntity::getAccountId, accountEntity.getId());
                    confQueryWrapper.eq(AccountFlashSmConfEntity::getStatus, DataStatusEnum.ENABLE.getValue());
                    confQueryWrapper.last("LIMIT 1");
                    AccountFlashSmConfEntity flashSmConfEntity = accountFlashSmConfService.getOne(confQueryWrapper);
                    if (flashSmConfEntity != null) {
                        String flashSmTemplateId = flashSmConfEntity.getFlashSmTemplateId();
                        String flashSmAccountId = flashSmConfEntity.getFlashSmAccountId();
                        asyncService.sendFlashSmAsync(MDC.get(CommonSessionKeys.REQ_ID_KEY), flashSmAccountId, flashSmTemplateId, telX, telB);
                    } else {
                        log.warn("account:{},未找到闪信配置", accountId);
                    }
                } else {
                    log.info("{} 被叫号码并非B号码,或者被叫号码并非手机号.", telB);
                }
            }

        }


        if (data != null) {
            return data.getRespData();
        }
        return BaseResponse.fail("[" + channelCode + "] Save fail.");
    }

    private BaseResponse checkReusable(String account, String telA, String telB) {
        MiddleNumberBindLogEntity bindLogEntity = iMiddleNumberBindLogService.checkReusable(account, telA, telB);
        if (bindLogEntity != null) {
            Date expireTime = bindLogEntity.getExpireTime();

            BindResponseVO bindResponseVO = BindResponseVO.builder()
                    .mappingId(bindLogEntity.getId())
                    .telX(bindLogEntity.getTelX())
                    .build();

            if (expireTime != null) {
                bindResponseVO.setExpireTime(expireTime);
            } else {
                bindResponseVO.setExpireTime(DateUtil.getNextTime(bindLogEntity.getCreateTime(), bindLogEntity.getExpiration().intValue()));

            }
            return BaseResponse.success(bindResponseVO);
        }
        return BaseResponse.fail("未找到对应的绑定关系");
    }

}
