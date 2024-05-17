package com.m7.abs.admin.core.local_scurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录认证过滤器
 *
 * @author Louis
 * @date Nov 20, 2018
 */
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {


    @Autowired
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String servletPath = request.getServletPath();
//        Log.logger.info("JwtAuthenticationFilter=>servletPath:" + servletPath);
//        String header = request.getHeader(JwtTokenUtils.TOKEN_HEADER);

        // 如果请求头中没有Authorization信息则直接放行了
        if (servletPath.equals("/login")) {
            chain.doFilter(request, response);
            return;
        }

        // 获取token, 并检查登录状态
        SecurityUtils.checkAuthentication(request);
        chain.doFilter(request, response);
    }

}