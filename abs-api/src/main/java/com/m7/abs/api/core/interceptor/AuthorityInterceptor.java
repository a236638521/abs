package com.m7.abs.api.core.interceptor;


import com.m7.abs.api.common.annotation.Authenticate;
import com.m7.abs.api.core.auth.AuthHandler;
import com.m7.abs.api.service.IWhiteIpService;
import com.m7.abs.common.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Slf4j
@Component
public class AuthorityInterceptor implements HandlerInterceptor {
    @Autowired
    private AuthHandler absAuthHandler;
    @Autowired
    private IWhiteIpService whiteIpService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        Authenticate annotation = method.getAnnotation(Authenticate.class);
        if (annotation != null) {
            String ip = HttpUtil.getIpAddr(request);
            //ip检查
            if (annotation.checkIp()) {
                boolean exist = whiteIpService.checkIpAddress(ip);
                if (exist) {
                    log.info("trust ip:" + ip + " : " + exist);
                    return true;
                }
            }

            //权限检查
            if (annotation.permission()) {
                boolean b = absAuthHandler.checkPermission(request, response);
                if (!b) {
                    log.warn("[" + ip + "] client has no right.");
                }
                return b;
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
    }
}
