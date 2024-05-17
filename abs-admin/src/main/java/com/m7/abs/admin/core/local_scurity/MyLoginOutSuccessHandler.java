package com.m7.abs.admin.core.local_scurity;

import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.utils.HttpUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 退出登录hander
 *
 * @author Kejie Peng
 * @date 2023年 04月21日 10:15:38
 */
public class MyLoginOutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpStatus.OK.value());
        HttpUtil.sendJson(response, BaseResponse.success("退出登录"));
    }
}
