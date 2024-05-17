package com.m7.abs.api.core.filter;


import com.m7.abs.api.core.wrapper.MyHttpServletRequestWrapper;
import com.m7.abs.common.constant.keys.CommonSessionKeys;
import com.m7.abs.common.constant.keys.HeaderKeys;
import com.m7.abs.common.utils.MyStringUtils;
import com.m7.abs.common.utils.RandomStringGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
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
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestId = httpServletRequest.getHeader(HeaderKeys.REQ_ID_KEY);
        if (StringUtils.isEmpty(requestId)) {
            requestId = RandomStringGenerator.generateRandomString(20);
        }
        MDC.put(CommonSessionKeys.REQ_ID_KEY, requestId);
        servletRequest = new MyHttpServletRequestWrapper(httpServletRequest);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        MDC.remove(CommonSessionKeys.REQ_ID_KEY);
    }
}
