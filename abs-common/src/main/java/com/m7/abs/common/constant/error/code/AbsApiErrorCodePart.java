package com.m7.abs.common.constant.error.code;

/**
 * @author zhuhf
 */
interface AbsApiErrorCodePart {
    /**
     * 小号业务
     */
    ErrorCodeConstant MID_NOT_FOUND = new ErrorCodeConstant("001", "0001", "未知中间号");
    ErrorCodeConstant BIND_TYPE_NOT_SUPPORT = new ErrorCodeConstant("001", "0002", "账户与中间号模式不匹配");
    ErrorCodeConstant BIND_NOT_FOUND = new ErrorCodeConstant("001", "0003", "绑定关系不存在");
    ErrorCodeConstant CHANNEL_NOT_FOUND = new ErrorCodeConstant("001", "0004", "通道不存在");
    ErrorCodeConstant CHANNEL_CONFIG_NOT_FOUND = new ErrorCodeConstant("001", "0005", "请检查通道配置是否正确");
    ErrorCodeConstant ACCOUNT_CHANNEL_CONFIG_NOT_FOUND = new ErrorCodeConstant("001", "0006", "账户通道配置异常");
    ErrorCodeConstant ACCOUNT_NOT_FOUND = new ErrorCodeConstant("001", "0007", "未知账户");
    ErrorCodeConstant BIND_EXPIRED = new ErrorCodeConstant("001", "0008", "绑定关系已过期");
    ErrorCodeConstant NO_NUMBER_AVAILABLE = new ErrorCodeConstant("001", "0009", "无可用中间号");
    ErrorCodeConstant NUMBER_BINDING_UPPER_LIMIT = new ErrorCodeConstant("001", "0010", "中间号号码池已达绑定上限");
    ErrorCodeConstant BIND_ALREADY_EXISTS = new ErrorCodeConstant("001", "0011", "绑定关系已存在");
    ErrorCodeConstant NO_RECORD_FOUND = new ErrorCodeConstant("001", "0012", "未查到任何记录");
    ErrorCodeConstant REPEAT_GROUP = new ErrorCodeConstant("001", "0013", "存在相同的分组");
    ErrorCodeConstant UNKNOWN_GROUP = new ErrorCodeConstant("001", "0014", "未知号码组");
    ErrorCodeConstant BINDING_RELATIONSHIP_CONFLICT = new ErrorCodeConstant("001", "0015", "绑定关系冲突");
    ErrorCodeConstant GROUP_NUMBER_UPPER_LIMIT = new ErrorCodeConstant("001", "0016", "号码组中号码数量达到上限");


    /**
     * 闪信业务
     */
    ErrorCodeConstant UNKNOWN_TEMPLATE = new ErrorCodeConstant("002", "0001", "未知模板");
    ErrorCodeConstant BATCH_PUSH_NOT_SUPPORTED = new ErrorCodeConstant("002", "0001", "暂不支持批量推送");

}
