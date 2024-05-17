package com.m7.abs.admin.core.local_scurity;

import com.alibaba.fastjson.JSON;
import com.m7.abs.common.constant.error.code.ErrorCodeConstant;
import com.m7.abs.common.domain.base.BaseResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author KejiePeng
 * @Title:
 * @Description
 * @date 2021/4/27 17:53
 */
public class SecurityAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setHeader("Content-type", "application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(BaseResponse.error(ErrorCodeConstant.CommonErrorCode.PERMISSION_EXCEPTION)));
    }
}
