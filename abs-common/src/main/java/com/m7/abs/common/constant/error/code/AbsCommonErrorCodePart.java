package com.m7.abs.common.constant.error.code;

/**
 * 公共模块错误码
 *
 * @author zhuhf
 */
interface AbsCommonErrorCodePart {
    ErrorCodeConstant SUCCESS = new ErrorCodeConstant("0000200", "成功");
    ErrorCodeConstant PARAM_ERR = new ErrorCodeConstant("0000001", "参数错误");
    ErrorCodeConstant FAILED = new ErrorCodeConstant("0000002", "失败");
    ErrorCodeConstant SYSTEM_EXCEPTION = new ErrorCodeConstant("0000500", "系统异常");
    ErrorCodeConstant PERMISSION_EXCEPTION = new ErrorCodeConstant("0000403", "NO RIGHT");
    ErrorCodeConstant NOT_FOUND = new ErrorCodeConstant("0000404", "NOT FOUND");
    ErrorCodeConstant PARAM_NOT_SUPPORT = new ErrorCodeConstant("0000402", "PARAM_NOT_SUPPORT");
    ErrorCodeConstant PUBLIC_INVALID_REQUEST_HEADER_STATUS_CODE = new ErrorCodeConstant("0000405", "非法请求头");
    ErrorCodeConstant NOT_SUPPORT = new ErrorCodeConstant("0000003", "NOT_SUPPORT");
}
