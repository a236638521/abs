package com.m7.abs.api.core.auth;

import com.m7.abs.api.core.auth.hmac.HmacAuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author KejiePeng
 * @Title:
 * @Description
 * @date 2021/7/6 15:53
 */
@Component
public class AuthHandler {
    @Autowired
    private HmacAuthProvider hmacAuthProvider;

    /**
     * 校验接口权限
     *
     * @param request
     * @param response
     * @return
     */
    public boolean checkPermission(HttpServletRequest request, HttpServletResponse response) {
        return hmacAuthProvider.doAuthFilter(request, response);
    }
}
