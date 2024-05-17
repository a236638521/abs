package com.m7.abs.common.domain.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.m7.abs.common.constant.error.code.ErrorCodeConstant;
import com.m7.abs.common.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author KejiePeng
 * @Title:
 * @Description
 * @date 2021/12/14 10:25
 */
@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> implements Serializable {
    private String requestId;
    /**
     * 状态码
     */
    private String code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据对象
     */
    private T data;

    private String responseTime;

    public static <T> BaseResponse<T> error() {
        return error(ErrorCodeConstant.CommonErrorCode.SYSTEM_EXCEPTION.getCode(), "未知异常，请联系管理员");
    }

    public static <T> BaseResponse<T> error(String message) {
        return error(ErrorCodeConstant.CommonErrorCode.SYSTEM_EXCEPTION.getCode(), message);
    }

    public static <T> BaseResponse<T> error(String code, String message) {
        BaseResponse<T> r = new BaseResponse<>();
        r.setCode(code);
        r.setMessage(message);
        return r;
    }

    public static <T> BaseResponse<T> error(ErrorCodeConstant code, String message) {
        return error(code.getCode(), message);
    }

    public static <T> BaseResponse<T> error(ErrorCodeConstant code) {
        return error(code.getCode(), code.getErrMsg());
    }

    public static <T> BaseResponse<T> success(String message) {
        BaseResponse<T> r = new BaseResponse<>();
        r.setMessage(message);
        return r;
    }

    public static <T> BaseResponse<T> success(T data) {
        BaseResponse<T> r = new BaseResponse<>();
        r.setData(data);
        return r;
    }

    public static <T> BaseResponse<T> success() {
        return new BaseResponse<>();
    }

    public static <T> BaseResponse<T> fail(String message) {
        return new BaseResponse<>(ErrorCodeConstant.CommonErrorCode.FAILED.getCode(), message);
    }

    public static <T> BaseResponse<T> fail(T data) {
        return new BaseResponse<>(ErrorCodeConstant.CommonErrorCode.FAILED.getCode(), ErrorCodeConstant.CommonErrorCode.FAILED.getErrMsg(), data);
    }


    public static <T> BaseResponse<T> fail(String code, String message) {
        return new BaseResponse<>(code, message);
    }

    public BaseResponse() {
        this.code = ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode();
        this.message = ErrorCodeConstant.CommonErrorCode.SUCCESS.getErrMsg();
    }

    public BaseResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        String code = this.code;
        if (StringUtils.isNotEmpty(code) && code.equals(ErrorCodeConstant.CommonErrorCode.SUCCESS.getCode())) {
            return true;
        }
        return false;
    }

    public String getResponseTime() {
        return DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_UTC);
    }
}
