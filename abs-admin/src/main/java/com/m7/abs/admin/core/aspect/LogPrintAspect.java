package com.m7.abs.admin.core.aspect;


import com.alibaba.fastjson.JSONObject;
import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.constant.keys.CommonSessionKeys;
import com.m7.abs.common.domain.base.BaseRequest;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.utils.FastJsonUtils;
import com.m7.abs.common.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author Kejie Peng
 * @apiNote
 */

@Aspect
@Component
@Slf4j(topic = "request_log")
public class LogPrintAspect {
    /**
     * 换行符
     */
    private static final String LINE_SEPARATOR = System.lineSeparator();

    /**
     * 以自定义 @LogPrint 注解为切点
     */
    @Pointcut("@annotation(com.m7.abs.common.annotation.WebAspect)")
    public void webAspect() {
    }

    /**
     * 在切点之前织入
     *
     * @param joinPoint
     * @throws Throwable
     */
    @Before("webAspect()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {

        // 获取 @webAspect 注解
        WebAspect annotation = getWebAspectAnnotation(joinPoint);

        if (annotation != null) {
            String logDesc = annotation.logDesc();
            boolean logDetail = annotation.logDetail();
            boolean injectReqId = annotation.injectReqId();
            Object reqObject = getReqObject(joinPoint);
            //请求参数自动注入RequestId;
            if (injectReqId) {
                if (reqObject != null && reqObject instanceof BaseRequest) {
                    BaseRequest baseRequest = (BaseRequest) reqObject;
                    String requestId = baseRequest.getRequestId();
                    if (StringUtils.isEmpty(requestId)) {
                        requestId = MDC.get(CommonSessionKeys.REQ_ID_KEY);
                        baseRequest.setRequestId(requestId);
                    }

                }
            }

            if (StringUtils.isNotEmpty(logDesc)) {
                // 开始打印请求日志
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                HttpServletRequest request = attributes.getRequest();
                LogPrintAspect.log.info("URL            : " + request.getMethod() + " [{}] [{}] => {}", HttpUtil.getIpAddr(request), annotation.logDesc(), request.getRequestURL().toString());
                // 打印请求入参
                if (logDetail) {
                    LogPrintAspect.log.info("Request Args   : " + FastJsonUtils.toJSONString(reqObject));
                }
            }


        }
    }


    /**
     * 在切点之后织入
     *
     * @throws Throwable
     */
    @After("webAspect()")
    public void doAfter() throws Throwable {

    }

    /**
     * 环绕
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("webAspect()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        // 获取 @webAspect 注解
        WebAspect annotation = getWebAspectAnnotation(proceedingJoinPoint);
        if (annotation != null) {
            String logDesc = annotation.logDesc();
            boolean logDetail = annotation.logDetail();
            boolean injectReqId = annotation.injectReqId();

            if (injectReqId) {
                Object reqObject = getReqObject(proceedingJoinPoint);
                if (reqObject != null && result != null) {
                    BaseRequest baseRequest = null;
                    if (reqObject instanceof BaseRequest) {
                        baseRequest = (BaseRequest) reqObject;
                    }
                    if (result instanceof BaseResponse) {
                        BaseResponse respMsg = (BaseResponse) result;
                        if (baseRequest != null) {
                            respMsg.setRequestId(baseRequest.getRequestId());
                        } else {
                            respMsg.setRequestId(MDC.get(CommonSessionKeys.REQ_ID_KEY));
                        }
                    }
                }
            }

            if (StringUtils.isNotEmpty(logDesc)) {
                // 打印出参
                long l = System.currentTimeMillis() - startTime;
                if (logDetail) {
                    LogPrintAspect.log.info("Time-Consuming : " + l + " ms" + " ,Response Args  : " + JSONObject.toJSONString(result));
                } else {
                    LogPrintAspect.log.info("Time-Consuming : " + l + " ms");
                }
            }

        }
        return result;
    }


    /**
     * 获取切面注解的描述
     *
     * @param joinPoint 切点
     * @return 描述信息
     * @throws Exception
     */
    public WebAspect getWebAspectAnnotation(JoinPoint joinPoint)
            throws Exception {
        WebAspect annotation = null;
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    annotation = method.getAnnotation(WebAspect.class);
                    break;
                }
            }
        }
        return annotation;
    }

    private Object getReqObject(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if ((arg instanceof HttpServletResponse) || (arg instanceof HttpServletRequest)
                        || (arg instanceof MultipartFile) || (arg instanceof MultipartFile[])) {
                    continue;
                }
                return arg;
            }
        }
        return null;
    }

}

