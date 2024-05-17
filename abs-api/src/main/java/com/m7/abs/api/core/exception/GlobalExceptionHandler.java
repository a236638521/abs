package com.m7.abs.api.core.exception;


import com.m7.abs.common.constant.error.code.ErrorCodeConstant;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * Title
 * Description:
 *
 * @author Kejie Peng
 * @date 2018/5/13 21:45
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //记录日志
        log.error(ex.getMessage(), ex);

        if (ex instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
            String msg = "";
            if (bindingResult.hasErrors()) {
                List<FieldError> fieldErrors = bindingResult.getFieldErrors();
                if (fieldErrors != null && fieldErrors.size() > 0) {
                    for (int i = 0; i < fieldErrors.size(); i++) {
                        FieldError fieldError = fieldErrors.get(i);
                        String field = fieldError.getField();
                        String defaultMessage = fieldError.getDefaultMessage();
                        msg += "[" + field + "]" + defaultMessage + ";";
                    }
                }
            }
            log.warn("IP             : " + HttpUtil.getIpAddr(request) + ",URL            : " + request.getRequestURL().toString());
            log.warn("ERROR MSG      : " + ex.getMessage());

            HttpUtil.sendJson(response, BaseResponse.fail(ErrorCodeConstant.CommonErrorCode.PARAM_NOT_SUPPORT.getCode(), msg));
            return null;
        }

        //获取请求异常返回给前台json数据
        if (ex instanceof HttpRequestMethodNotSupportedException) {
            HttpUtil.sendJson(response, BaseResponse.fail(ErrorCodeConstant.CommonErrorCode.FAILED.getCode(), "请求类型错误"));
            return null;
        }

        if (ex instanceof HttpMessageNotReadableException) {
            HttpUtil.sendJson(response, BaseResponse.fail(ErrorCodeConstant.CommonErrorCode.NOT_FOUND.getCode(), "Required request body is missing"));
            return null;
        }

        if (ex instanceof HttpMediaTypeNotSupportedException) {
            HttpUtil.sendJson(response, BaseResponse.fail(ErrorCodeConstant.CommonErrorCode.NOT_FOUND.getCode(), ex.getMessage()));
            return null;
        }

        if (ex instanceof AccessDeniedException) {
            HttpUtil.sendJson(response, BaseResponse.fail(ErrorCodeConstant.CommonErrorCode.FAILED.getCode(), "权限不够,不允许访问"));
            return null;
        }

        HttpUtil.sendJson(response, BaseResponse.fail(ErrorCodeConstant.CommonErrorCode.SYSTEM_EXCEPTION));
        return null;
    }


}
