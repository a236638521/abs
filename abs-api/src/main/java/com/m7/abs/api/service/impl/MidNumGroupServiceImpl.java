package com.m7.abs.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.m7.abs.api.domain.dto.ChannelConfigDto;
import com.m7.abs.api.domain.dto.midNum.MidMumConfigDto;
import com.m7.abs.api.domain.dto.midNum.MidNumReqDto;
import com.m7.abs.api.domain.dto.midNum.MiddleNumberPoolDto;
import com.m7.abs.api.domain.vo.midNum.*;
import com.m7.abs.api.mapper.XNumGroupChannelConfMapper;
import com.m7.abs.api.mapper.XNumGroupMapper;
import com.m7.abs.api.mapper.XNumGroupNumberListMapper;
import com.m7.abs.api.projectHandler.midNum.IMidNumHandler;
import com.m7.abs.api.projectHandler.midNum.MIdNumType;
import com.m7.abs.api.service.*;
import com.m7.abs.common.constant.common.BindStatusEnum;
import com.m7.abs.common.constant.common.BindTypeEnum;
import com.m7.abs.common.constant.error.code.ErrorCodeConstant;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.*;
import com.m7.abs.common.utils.DateUtil;
import com.m7.abs.common.utils.FastJsonUtils;
import com.m7.abs.common.utils.MyStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hulin
 */
@Slf4j
@Service
public class MidNumGroupServiceImpl implements IMidNumGroupService {
    private static final Logger logger = LoggerFactory.getLogger("server");
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
    private XNumGroupMapper xNumGroupMapper;
    @Autowired
    private XNumGroupNumberListMapper xNumGroupNumberListMapper;
    @Autowired
    private XNumGroupChannelConfMapper xNumGroupChannelConfMapper;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IXNumGroupService xNumGroupService;

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
        //获取channel配置
        ChannelConfigDto channelConfigDto = channelService.getChannelConfigInfoByAccountAndChannelCode(account, channelCode);
        if (channelConfigDto == null) {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.CHANNEL_NOT_FOUND);
        }

        MidNumReqDto midNumReqDto = this.getAccountChannelConfigInfo(channelConfigDto);
        IMidNumHandler midNumHandler = midNumReqDto.getMidNumHandler();
        if (midNumHandler == null) {
            return BaseResponse.fail("No such channelCode,please check your params!");
        }

        if (channelConfigDto == null) {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.CHANNEL_CONFIG_NOT_FOUND);
        }

        return BaseResponse.success(midNumReqDto);
    }


    @Override
    public BaseResponse gxbBind(BindGXBRequestVO requestVO) {
        //获取小号对应的配置信息
        BaseResponse<MidNumReqDto> bindConfInfo = this.getBindConfInfo(BindTypeEnum.GXB.getType(), requestVO.getAccount(), requestVO.getTelX(), requestVO.getChannelCode());

        if (!ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode().equals(bindConfInfo.getCode())) {
            return bindConfInfo;
        }
        MidNumReqDto midNumReqDto = bindConfInfo.getData();

        Integer areaCode = requestVO.getAreaCode();
        if (areaCode == null && StringUtils.isNotEmpty(midNumReqDto.getAreaCode())) {
            requestVO.setAreaCode(Integer.valueOf(midNumReqDto.getAreaCode()));
        }

        //TODO 请求绑定
        MidMumConfigDto midMumConfigDto = midNumReqDto.getMidMumConfigDto();
        /**
         * 获取第三方groupID
         */
        String groupId = requestVO.getGroupId();
        XNumGroupChannelConfEntity groupConfInfo = xNumGroupService.getGroupConfInfo(groupId, midMumConfigDto.getChannelId());
        if (groupConfInfo == null) {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.UNKNOWN_GROUP);
        }
        requestVO.setAssociationId(groupConfInfo.getAssociationId());
        BaseResponse<BindResponseVO> binResponse = midNumReqDto.getMidNumHandler().gxbBind(midMumConfigDto, requestVO);

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
                    .groupId(requestVO.getGroupId())
                    .telX(telX)
                    .telB(requestVO.getTelB())
                    .bindType(BindTypeEnum.GXB.getType())
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
            logger.info("bind info:{}", saveBindLog);
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
    public BaseResponse gxbUnBind(UnBindRequestVO requestVO) {
        //查询绑定信息
        BaseResponse<MiddleNumberBindLogEntity> bindLogResponse = iMiddleNumberBindLogService.getUnbindLog(requestVO.getMappingId());
        if (!ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode().equals(bindLogResponse.getCode())) {
            return bindLogResponse;
        }
        MiddleNumberBindLogEntity bindInfo = bindLogResponse.getData();
        requestVO.setAssociationId(bindInfo.getAssociationId());

        //获取小号对应的配置信息
        BaseResponse<MidNumReqDto> bindConfInfo = this.getBindConfInfo(BindTypeEnum.GXB.getType(), bindInfo.getAccountId(), bindInfo.getTelX(), bindInfo.getChannelCode());
        if (!ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode().equals(bindConfInfo.getCode())) {
            return bindConfInfo;
        }
        MidNumReqDto midNumReqDto = bindConfInfo.getData();

        //TODO 解绑操作
        BaseResponse<BindResponseVO> binResponse = midNumReqDto.getMidNumHandler().gxbUnBind(midNumReqDto.getMidMumConfigDto(), requestVO);
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
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<GroupCreateRespVO> createGroup(GroupCreateRequestVO requestVO) {
        AccountEntity accountEntity = accountService.getByBillAccountId(requestVO.getAccount());
        if (accountEntity == null) {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.ACCOUNT_NOT_FOUND);
        }
        LambdaQueryWrapper<XNumGroupEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(XNumGroupEntity::getAccountId, accountEntity.getId());
        queryWrapper.eq(XNumGroupEntity::getName, requestVO.getName());
        queryWrapper.last("LIMIT 1");
        XNumGroupEntity xNumGroupEntity = xNumGroupMapper.selectOne(queryWrapper);
        if (xNumGroupEntity != null) {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.REPEAT_GROUP);
        }

        /**
         * 创建local group
         */
        xNumGroupEntity = XNumGroupEntity.builder()
                .accountId(accountEntity.getId())
                .status(1)
                .createTime(new Date())
                .name(requestVO.getName())
                .build();
        xNumGroupMapper.insert(xNumGroupEntity);

        /**
         * 创建渠道group
         */
        BaseResponse<List<MidNumReqDto>> channelConfResp = this.getChannels(requestVO.getAccount());
        if (!channelConfResp.isSuccess()) {
            return BaseResponse.fail(channelConfResp.getCode(), channelConfResp.getMessage());
        }

        List<MidNumReqDto> confList = channelConfResp.getData();
        if (confList == null || confList.size() == 0) {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.ACCOUNT_CHANNEL_CONFIG_NOT_FOUND);
        }

        for (int i = 0; i < confList.size(); i++) {
            MidNumReqDto midNumReqDto = confList.get(i);
            IMidNumHandler midNumHandler = midNumReqDto.getMidNumHandler();
            if (midNumHandler != null) {
                String channelCode = midNumReqDto.getMidMumConfigDto().getChannelCode();
                String channelId = midNumReqDto.getMidMumConfigDto().getChannelId();

                LambdaQueryWrapper<XNumGroupChannelConfEntity> confWrapper = new LambdaQueryWrapper();
                confWrapper.eq(XNumGroupChannelConfEntity::getGroupId, xNumGroupEntity.getId());
                confWrapper.eq(XNumGroupChannelConfEntity::getChannelId, channelId);
                XNumGroupChannelConfEntity xNumGroupChannelConfEntity = xNumGroupChannelConfMapper.selectOne(confWrapper);
                if (xNumGroupChannelConfEntity == null) {
                    log.info("channelId:{}", channelId);
                    log.info("channelCode:{}", channelCode);
                    BaseResponse baseResponse = midNumHandler.groupInsert(midNumReqDto.getMidMumConfigDto());
                    if (baseResponse.isSuccess()) {
                        String associationId = (String) baseResponse.getData();
                        XNumGroupChannelConfEntity confEntity = XNumGroupChannelConfEntity.builder()
                                .groupId(xNumGroupEntity.getId())
                                .associationId(associationId)
                                .channelId(channelId)
                                .createTime(new Date())
                                .build();
                        xNumGroupChannelConfMapper.insert(confEntity);
                    }
                }

            }
        }
        GroupCreateRespVO respVO = GroupCreateRespVO.builder()
                .groupId(xNumGroupEntity.getId())
                .name(requestVO.getName())
                .build();
        return BaseResponse.success(respVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse deleteGroup(GroupDelRequestVO requestVO) {
        String groupId = requestVO.getGroupId();
        String account = requestVO.getAccount();
        AccountEntity accountEntity = accountService.getByBillAccountId(account);
        if (accountEntity == null) {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.ACCOUNT_NOT_FOUND);
        }
        LambdaQueryWrapper<XNumGroupEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(XNumGroupEntity::getAccountId, accountEntity.getId());
        queryWrapper.eq(XNumGroupEntity::getId, groupId);
        queryWrapper.last("LIMIT 1");
        XNumGroupEntity xNumGroupEntity = xNumGroupMapper.selectOne(queryWrapper);
        if (xNumGroupEntity == null) {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.UNKNOWN_GROUP);
        }
        BaseResponse<List<MidNumReqDto>> channelConfResp = this.getChannels(account);
        if (!channelConfResp.isSuccess()) {
            return channelConfResp;
        }

        List<MidNumReqDto> confList = channelConfResp.getData();
        if (confList == null || confList.size() == 0) {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.ACCOUNT_CHANNEL_CONFIG_NOT_FOUND);
        }

        boolean flag = false;
        StringBuilder msgStrB = new StringBuilder();
        for (int i = 0; i < confList.size(); i++) {
            MidNumReqDto midNumReqDto = confList.get(i);
            IMidNumHandler midNumHandler = midNumReqDto.getMidNumHandler();
            if (midNumHandler != null) {
                String channelCode = midNumReqDto.getMidMumConfigDto().getChannelCode();
                String channelId = midNumReqDto.getMidMumConfigDto().getChannelId();
                log.info("channelCode:{}", channelCode);
                log.info("channelId:{}", channelId);

                LambdaQueryWrapper<XNumGroupChannelConfEntity> confWrapper = new LambdaQueryWrapper();
                confWrapper.eq(XNumGroupChannelConfEntity::getGroupId, xNumGroupEntity.getId());
                confWrapper.eq(XNumGroupChannelConfEntity::getChannelId, channelId);
                XNumGroupChannelConfEntity xNumGroupChannelConfEntity = xNumGroupChannelConfMapper.selectOne(confWrapper);
                if (xNumGroupChannelConfEntity != null) {
                    requestVO.setAssociationId(xNumGroupChannelConfEntity.getAssociationId());
                    BaseResponse baseResponse = midNumHandler.groupDel(midNumReqDto.getMidMumConfigDto(), requestVO);
                    if (baseResponse.isSuccess()) {
                        flag = true;
                    } else {
                        flag = false;
                        msgStrB.append(baseResponse.getMessage() + ";");
                    }
                }

            }
        }
        if (flag) {
            /**
             * 清空所有号码，删除group，删除groupConf
             */
            LambdaQueryWrapper<XNumGroupNumberListEntity> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.eq(XNumGroupNumberListEntity::getGroupId, groupId);
            xNumGroupNumberListMapper.delete(deleteWrapper);
            LambdaQueryWrapper<XNumGroupChannelConfEntity> deleteWrapper2 = new LambdaQueryWrapper<>();
            deleteWrapper2.eq(XNumGroupChannelConfEntity::getGroupId, groupId);
            xNumGroupChannelConfMapper.delete(deleteWrapper2);
            xNumGroupMapper.deleteById(groupId);
            return BaseResponse.success();
        } else {
            return BaseResponse.fail(msgStrB.toString());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<GroupAddNumbersRespVO> addNumbers(GroupAddNumbersRequestVO requestVO) {
        GroupAddNumbersRespVO respVO = GroupAddNumbersRespVO.builder().build();
        List<String> failNumbers = new ArrayList<>();
        String groupId = requestVO.getGroupId();
        String account = requestVO.getAccount();
        AccountEntity accountEntity = accountService.getByBillAccountId(account);
        if (accountEntity == null) {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.ACCOUNT_NOT_FOUND);
        }

        List<String> numbers = requestVO.getNumbers();

        BaseResponse<GroupAddNumbersRespVO> fail = BaseResponse.fail("失败");

        //号码去重
        List<String> uniqNumbers = numbers.stream().distinct().collect(Collectors.toList());

        LambdaQueryWrapper<XNumGroupEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(XNumGroupEntity::getAccountId, accountEntity.getId());
        queryWrapper.eq(XNumGroupEntity::getId, groupId);
        queryWrapper.last("LIMIT 1");
        XNumGroupEntity xNumGroupEntity = xNumGroupMapper.selectOne(queryWrapper);
        if (xNumGroupEntity == null) {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.UNKNOWN_GROUP);
        }


        BaseResponse<List<MidNumReqDto>> channelConfResp = this.getChannels(account);
        if (!channelConfResp.isSuccess()) {
            return BaseResponse.fail(channelConfResp.getCode(), channelConfResp.getMessage());
        }

        List<MidNumReqDto> confList = channelConfResp.getData();
        if (confList == null || confList.size() == 0) {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.ACCOUNT_CHANNEL_CONFIG_NOT_FOUND);
        }

        LambdaQueryWrapper<XNumGroupNumberListEntity> numberListWrapper = new LambdaQueryWrapper<>();
        numberListWrapper.eq(XNumGroupNumberListEntity::getGroupId, xNumGroupEntity.getId());
        List<XNumGroupNumberListEntity> existNumberList = xNumGroupNumberListMapper.selectList(numberListWrapper);


        List<String> existNumberTemp = new ArrayList<>();
        if (existNumberList != null) {
            if (existNumberList.size() > 100) {
                return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.GROUP_NUMBER_UPPER_LIMIT);
            }

            existNumberList.forEach(item -> {
                String number = item.getNumber();
                existNumberTemp.add(number);
                if (uniqNumbers.contains(number)) {
                    uniqNumbers.remove(number);
                    failNumbers.add(number);
                }
            });
        }
        respVO.setFail(failNumbers);

        List<String> existNumber = existNumberTemp.stream().distinct().collect(Collectors.toList());
        uniqNumbers.forEach(item -> {
            if (existNumber.contains(item)) {
                existNumber.remove(item);
            }
        });


        log.info("exist numbers:{}", FastJsonUtils.toJSONString(existNumber));
        log.info("needs insert numbers:{}", FastJsonUtils.toJSONString(uniqNumbers));

        requestVO.setNumbers(uniqNumbers);
        boolean flag = false;
        StringBuilder msgStrB = new StringBuilder();
        if (uniqNumbers != null && uniqNumbers.size() > 0) {
            for (int i = 0; i < confList.size(); i++) {
                MidNumReqDto midNumReqDto = confList.get(i);
                IMidNumHandler midNumHandler = midNumReqDto.getMidNumHandler();
                if (midNumHandler != null) {
                    String channelCode = midNumReqDto.getMidMumConfigDto().getChannelCode();
                    String channelId = midNumReqDto.getMidMumConfigDto().getChannelId();
                    log.info("channelCode:{}", channelCode);
                    log.info("channelId:{}", channelId);

                    LambdaQueryWrapper<XNumGroupChannelConfEntity> confWrapper = new LambdaQueryWrapper();
                    confWrapper.eq(XNumGroupChannelConfEntity::getGroupId, xNumGroupEntity.getId());
                    confWrapper.eq(XNumGroupChannelConfEntity::getChannelId, channelId);
                    XNumGroupChannelConfEntity xNumGroupChannelConfEntity = xNumGroupChannelConfMapper.selectOne(confWrapper);
                    if (xNumGroupChannelConfEntity != null) {
                        requestVO.setAssociationId(xNumGroupChannelConfEntity.getAssociationId());
                        BaseResponse baseResponse = midNumHandler.groupAddNumbers(midNumReqDto.getMidMumConfigDto(), requestVO, existNumber);
                        if (baseResponse.isSuccess()) {
                            flag = true;
                        } else {
                            flag = false;
                            msgStrB.append(baseResponse.getMessage() + ";");
                        }
                    }

                }
            }
        } else {
            fail.setMessage("没有匹配到需要添加的号码");
            fail.setData(respVO);
            return fail;
        }

        if (flag) {
            for (int i = 0; i < uniqNumbers.size(); i++) {
                String number = uniqNumbers.get(i);
                XNumGroupNumberListEntity xNumGroupNumberListEntity = XNumGroupNumberListEntity.builder()
                        .groupId(groupId)
                        .number(number)
                        .createTime(new Date())
                        .build();
                xNumGroupNumberListMapper.insert(xNumGroupNumberListEntity);
            }

            respVO.setSuccess(uniqNumbers);
            return BaseResponse.success(respVO);
        } else {
            return BaseResponse.fail(msgStrB.toString());
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<GroupDelNumbersRespVO> delNumbers(GroupDelNumbersRequestVO requestVO) {
        GroupDelNumbersRespVO respVO = GroupDelNumbersRespVO.builder().build();
        List<String> failNumbers = new ArrayList<>();
        BaseResponse<GroupDelNumbersRespVO> fail = BaseResponse.fail("失败");

        String groupId = requestVO.getGroupId();
        String account = requestVO.getAccount();
        AccountEntity accountEntity = accountService.getByBillAccountId(account);
        if (accountEntity == null) {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.ACCOUNT_NOT_FOUND);
        }
        List<String> numbers = requestVO.getNumbers();

        //号码去重
        List<String> uniqNumbers = numbers.stream().distinct().collect(Collectors.toList());

        LambdaQueryWrapper<XNumGroupEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(XNumGroupEntity::getAccountId, accountEntity.getId());
        queryWrapper.eq(XNumGroupEntity::getId, groupId);
        queryWrapper.last("LIMIT 1");
        XNumGroupEntity xNumGroupEntity = xNumGroupMapper.selectOne(queryWrapper);
        if (xNumGroupEntity == null) {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.UNKNOWN_GROUP);
        }


        BaseResponse<List<MidNumReqDto>> channelConfResp = this.getChannels(account);
        if (!channelConfResp.isSuccess()) {
            return BaseResponse.fail(channelConfResp.getCode(), channelConfResp.getMessage());
        }

        List<MidNumReqDto> confList = channelConfResp.getData();
        if (confList == null || confList.size() == 0) {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.ACCOUNT_CHANNEL_CONFIG_NOT_FOUND);
        }

        LambdaQueryWrapper<XNumGroupNumberListEntity> numberListWrapper = new LambdaQueryWrapper<>();
        numberListWrapper.eq(XNumGroupNumberListEntity::getGroupId, xNumGroupEntity.getId());
        List<XNumGroupNumberListEntity> existNumberList = xNumGroupNumberListMapper.selectList(numberListWrapper);


        List<String> existNumberTemp = new ArrayList<>();
        List<String> needRemoveNumberTemp = new ArrayList<>();
        List<XNumGroupNumberListEntity> needRemoveNumberList = new ArrayList<>();
        if (existNumberList != null) {
            existNumberList.forEach(item -> {
                String number = item.getNumber();
                existNumberTemp.add(number);
                if (uniqNumbers.contains(number)) {
                    needRemoveNumberTemp.add(number);
                    needRemoveNumberList.add(item);
                    uniqNumbers.remove(number);
                }
            });
        }
        List<String> existNumber = existNumberTemp.stream().distinct().collect(Collectors.toList());
        List<String> needRemoveNumber = needRemoveNumberTemp.stream().distinct().collect(Collectors.toList());
        log.info("already exist numbers:{}", FastJsonUtils.toJSONString(existNumber));
        log.info("needs remove numbers:{}", FastJsonUtils.toJSONString(needRemoveNumber));
        respVO.setFail(uniqNumbers);
        respVO.setSuccess(needRemoveNumber);

        requestVO.setNumbers(needRemoveNumber);
        boolean flag = false;
        StringBuilder msgStrB = new StringBuilder();
        if (needRemoveNumber != null && needRemoveNumber.size() > 0) {
            for (int i = 0; i < confList.size(); i++) {
                MidNumReqDto midNumReqDto = confList.get(i);
                IMidNumHandler midNumHandler = midNumReqDto.getMidNumHandler();
                if (midNumHandler != null) {
                    String channelCode = midNumReqDto.getMidMumConfigDto().getChannelCode();
                    String channelId = midNumReqDto.getMidMumConfigDto().getChannelId();
                    log.info("channelCode:{}", channelCode);
                    log.info("channelId:{}", channelId);
                    LambdaQueryWrapper<XNumGroupChannelConfEntity> confWrapper = new LambdaQueryWrapper();
                    confWrapper.eq(XNumGroupChannelConfEntity::getGroupId, xNumGroupEntity.getId());
                    confWrapper.eq(XNumGroupChannelConfEntity::getChannelId, channelId);
                    XNumGroupChannelConfEntity xNumGroupChannelConfEntity = xNumGroupChannelConfMapper.selectOne(confWrapper);
                    if (xNumGroupChannelConfEntity != null) {
                        requestVO.setAssociationId(xNumGroupChannelConfEntity.getAssociationId());
                        BaseResponse baseResponse = midNumHandler.groupDelNumbers(midNumReqDto.getMidMumConfigDto(), requestVO, existNumber);
                        if (baseResponse.isSuccess()) {
                            flag = true;
                        } else {
                            flag = false;
                            msgStrB.append(baseResponse.getMessage() + ";");
                        }
                    }

                }
            }
        } else {
            fail.setMessage("没有匹配到需要添加的号码");
            fail.setData(respVO);
            return fail;
        }

        if (flag) {
            for (int i = 0; i < needRemoveNumberList.size(); i++) {
                XNumGroupNumberListEntity xNumGroupNumberListEntity = needRemoveNumberList.get(i);
                xNumGroupNumberListMapper.deleteById(xNumGroupNumberListEntity.getId());
            }
            return BaseResponse.success(respVO);
        } else {
            fail.setMessage(msgStrB.toString());
            fail.setData(respVO);
            return fail;
        }
    }

    @Override
    public BaseResponse numberList(GroupQueryNumberListRequestVO requestVO) {
        String groupId = requestVO.getGroupId();
        String account = requestVO.getAccount();
        AccountEntity accountEntity = accountService.getByBillAccountId(account);
        if (accountEntity == null) {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.ACCOUNT_NOT_FOUND);
        }
        LambdaQueryWrapper<XNumGroupEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(XNumGroupEntity::getAccountId, accountEntity.getId());
        queryWrapper.eq(XNumGroupEntity::getId, groupId);
        queryWrapper.last("LIMIT 1");
        XNumGroupEntity xNumGroupEntity = xNumGroupMapper.selectOne(queryWrapper);
        if (xNumGroupEntity == null) {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.UNKNOWN_GROUP);
        }

        LambdaQueryWrapper<XNumGroupNumberListEntity> listQueryWrapper = new LambdaQueryWrapper<>();
        listQueryWrapper.eq(XNumGroupNumberListEntity::getGroupId, xNumGroupEntity.getId());
        List<XNumGroupNumberListEntity> xNumGroupNumberListEntities = xNumGroupNumberListMapper.selectList(listQueryWrapper);
        List<String> numberList = new ArrayList<>();
        if (xNumGroupNumberListEntities != null) {
            xNumGroupNumberListEntities.stream().forEach(item -> {
                numberList.add(item.getNumber());
            });
        }
        return BaseResponse.success(numberList);
    }


    private BaseResponse<List<MidNumReqDto>> getChannels(String account) {
        //获取channel配置
        List<ChannelConfigDto> channelConfigDtos = channelService.getChannelConfigInfoByAccount(account);

        if (channelConfigDtos == null || channelConfigDtos.size() == 0) {
            return BaseResponse.error(ErrorCodeConstant.ApiErrorCode.CHANNEL_CONFIG_NOT_FOUND);
        }

        List<MidNumReqDto> accountChannelConf = new ArrayList<>();
        for (int i = 0; i < channelConfigDtos.size(); i++) {
            ChannelConfigDto channelConfigDto = channelConfigDtos.get(i);
            MidNumReqDto midNumReqDto = getAccountChannelConfigInfo(channelConfigDto);
            IMidNumHandler midNumHandler = midNumReqDto.getMidNumHandler();
            if (midNumHandler != null) {
                accountChannelConf.add(midNumReqDto);
            }

        }
        return BaseResponse.success(accountChannelConf);
    }


    private MidNumReqDto getAccountChannelConfigInfo(ChannelConfigDto channelConfigDto) {

        //根据channelCode获取执行handler
        IMidNumHandler midNumHandler = midNumHandleMap.get(channelConfigDto.getChannelCode());

        MidMumConfigDto accountDto = MidMumConfigDto.builder()
                .appId(channelConfigDto.getAppId())
                .accessKey(channelConfigDto.getAccessKey())
                .secretKey(channelConfigDto.getSecretKey())
                .channelId(channelConfigDto.getChannelId())
                .channelCode(channelConfigDto.getChannelCode())
                .build();
        accountDto.setBasePath(channelConfigDto.getHost(), channelConfigDto.getPort(), channelConfigDto.getContextPath());

        MidNumReqDto midNumReqDto = MidNumReqDto.builder()
                .midNumHandler(midNumHandler)
                .midMumConfigDto(accountDto)
                .build();
        return midNumReqDto;
    }

}
