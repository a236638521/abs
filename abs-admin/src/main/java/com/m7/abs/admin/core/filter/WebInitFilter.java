package com.m7.abs.admin.core.filter;


import com.m7.abs.common.constant.keys.CommonSessionKeys;
import com.m7.abs.common.utils.MyStringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @author pkj
 * @create 2018-05-28 下午 6:43
 **/
@Component
@WebFilter(urlPatterns = {"/*"}, filterName = "initFilter")
public class WebInitFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String requestId = MyStringUtils.randomUUID();
        MDC.put(CommonSessionKeys.REQ_ID_KEY, requestId);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        MDC.remove(CommonSessionKeys.REQ_ID_KEY);
    }
}
