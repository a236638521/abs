package com.m7.abs.api.core.auth.hmac;

import com.m7.abs.api.core.auth.hmac.bean.AuthInfo;
import com.m7.abs.api.core.auth.hmac.bean.CanonicalHeaders;
import com.m7.abs.api.core.wrapper.MyHttpServletRequestWrapper;
import com.m7.abs.common.utils.CryptoUtil;
import com.m7.abs.common.utils.DateUtil;
import com.m7.abs.common.utils.MapUtil;
import com.m7.abs.common.utils.MyStringUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import java.io.*;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

/**
 * @author KejiePeng
 * @Title:
 * @Description
 * @date 2021/7/12 10:00
 */
@Slf4j
public class HmacAuthUtil {
    private static String PROJECT_NAME = "abs";

    private static long TIME_OUT_LENGTH = 5 * 60; //5分钟
    private static String HEADER_AUTHORIZATION = "Authorization";
    private static String HEADER_HTTP_METHOD = "HTTP-method";
    private static String HEADER_CONTENT_TYPE = "Content-Type";
    private static String HEADER_CONTENT_MD5 = "Content-MD5";

    private static String HEADER_ABS_NONCE = "m7-abs-nonce";
    private static String HEADER_ABS_TIMESTAMP = "m7-abs-timestamp";

    private static String CHAR_SET_UTF8 = "UTF-8";

    /**
     * 从请求中获取验签数据
     *
     * @param request
     * @return
     */
    protected static AuthInfo getAuthInfoFromRequest(HttpServletRequest request) {
        AuthInfo authInfo = new AuthInfo();


        String authorization = request.getHeader(HEADER_AUTHORIZATION);

        if (org.apache.commons.lang3.StringUtils.isNotEmpty(authorization)) {
            String[] split = authorization.split(":");
            if (split != null && split.length == 2) {
                authInfo.setAccessKey(split[0].replaceAll(PROJECT_NAME + " ", ""));
                authInfo.setAuthorization(split[1]);
            }
        }

        authInfo.setHttpMethod(request.getHeader(HEADER_HTTP_METHOD));
        authInfo.setContentType(request.getHeader(HEADER_CONTENT_TYPE));

        CanonicalHeaders canonicalHeaders = new CanonicalHeaders();
        canonicalHeaders.setNonce(request.getHeader(HEADER_ABS_NONCE));
        canonicalHeaders.setTimestamp(request.getHeader(HEADER_ABS_TIMESTAMP));
        authInfo.setCanonicalHeaders(canonicalHeaders);

        String requestURI = request.getRequestURI();
        String queryString = sortQueryString(request.getQueryString());
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(queryString)) {
            requestURI += "?" + queryString;
        }
        authInfo.setCanonicalResource(requestURI);

        String body = getRequestBody(request);

        if (org.apache.commons.lang3.StringUtils.isNotEmpty(body)) {
            try {
                authInfo.setBodyMD5(Base64.getEncoder().encodeToString(CryptoUtil.encodeMD5(body).getBytes(CHAR_SET_UTF8)));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return authInfo;
    }

    /**
     * 判断非空
     * 此方法 有待改善
     *
     * @param authInfo
     * @return
     */
    protected static boolean checkAuthInfo(AuthInfo authInfo) {

        if (org.apache.commons.lang3.StringUtils.isEmpty(authInfo.getContentType())) {
            return false;
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(authInfo.getHttpMethod())) {
            return false;
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(authInfo.getAccessKey())) {
            return false;
        }

        CanonicalHeaders canonicalHeaders = authInfo.getCanonicalHeaders();
        if (org.apache.commons.lang3.StringUtils.isEmpty(canonicalHeaders.getNonce())) {
            return false;
        }
        @NotEmpty String timestamp = canonicalHeaders.getTimestamp();
        if (org.apache.commons.lang3.StringUtils.isEmpty(timestamp)) {
            return false;
        } else {
            Date date = DateUtil.parseStrToDate(timestamp, DateUtil.DATE_TIME_FORMAT_UTC);
            long distanceTime = DateUtil.getDistanceTimeBySec(new Date(), date);
            if (distanceTime > TIME_OUT_LENGTH) {
                log.info("auth timestamp timeout:" + distanceTime);
                return false;
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(authInfo.getAuthorization())) {
            return false;
        }
        return true;
    }

    /**
     * 组装需要签名的数据
     *
     * @param authInfo
     * @return
     */
    protected static String getStringToSign(AuthInfo authInfo) {
        StringBuilder stringToSign = new StringBuilder();

        stringToSign.append(authInfo.getBodyMD5()).append("\n");
        stringToSign.append(authInfo.getContentType()).append("\n");
        stringToSign.append(authInfo.getHttpMethod()).append("\n");


        CanonicalHeaders canonicalHeaders = authInfo.getCanonicalHeaders();
        stringToSign.append(HEADER_ABS_NONCE.toLowerCase() + ":").append(canonicalHeaders.getNonce()).append("\n");
        stringToSign.append(HEADER_ABS_TIMESTAMP.toLowerCase() + ":").append(canonicalHeaders.getTimestamp()).append("\n");


        stringToSign.append(authInfo.getCanonicalResource());

        return stringToSign.toString();
    }

    /**
     * 签名
     *
     * @param secretKey
     * @param stringToSign
     * @return
     */
    protected static String signature(String secretKey, String stringToSign) {
        byte[] bytes = CryptoUtil.sha256_HMAC(secretKey, MyStringUtils.toUTF8(stringToSign));
        return Base64.getEncoder().encodeToString(bytes);
    }


    private static String getRequestBody(HttpServletRequest request) {
        StringBuffer sb = new StringBuffer();
        try {
            MyHttpServletRequestWrapper myHttpServletRequestWrapper = new MyHttpServletRequestWrapper(request);
            InputStream is = myHttpServletRequestWrapper.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
        } catch (IOException e) {
            log.error("get request body error.", e);
        }
        return sb.toString();
    }

    private static String sortQueryString(String queryString) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(queryString)) {
            return null;
        }
        Map<String, Object> mapByUrlParams = MapUtil.getMapByUrlParams(queryString, CHAR_SET_UTF8);
        Map<String, Object> sortMap = MapUtil.sortMapByKey(mapByUrlParams);
        return MapUtil.getUrlParamsByMap(sortMap);
    }
}
