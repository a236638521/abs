package com.m7.abs.api.core.auth.hmac;

import com.m7.abs.api.core.auth.hmac.bean.AuthInfo;
import com.m7.abs.api.mapper.AppInterfaceMapper;
import com.m7.abs.common.constant.error.code.ErrorCodeConstant;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.utils.FastJsonUtils;
import com.m7.abs.common.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author KejiePeng
 * @Title:
 * @Description
 * @date 2021/7/7 11:02
 */
@Slf4j
@Component
public class HmacAuthProvider {
    @Autowired
    private AppInterfaceMapper appInterfaceMapper;


    public boolean doAuthFilter(HttpServletRequest request, HttpServletResponse response) {


        AuthInfo authInfo = HmacAuthUtil.getAuthInfoFromRequest(request);
        //判断验签参数非空以及内容格式
        boolean authInfoPass = HmacAuthUtil.checkAuthInfo(authInfo);
        if (!authInfoPass) {
            log.info("auth info has been intercepted: " + FastJsonUtils.toJSONString(authInfo));
            HttpUtil.sendJson(response, BaseResponse.error(ErrorCodeConstant.CommonErrorCode.PERMISSION_EXCEPTION, "No right,please check your params."));
            return false;
        }

        String accessKey = authInfo.getAccessKey();
        String authorization = authInfo.getAuthorization();

        //获取DB中的secretKey
        String secretKey = appInterfaceMapper.getAppInterFaceByAccessKey(accessKey);
        if (StringUtils.isEmpty(secretKey)) {
            //未知AK SK
            log.info("unknown accessKey:" + accessKey);
            HttpUtil.sendJson(response, BaseResponse.error(ErrorCodeConstant.CommonErrorCode.PERMISSION_EXCEPTION, "No right,unknown auth key."));
            return false;
        }

        //获取待签名拼接字符串
        String msg = HmacAuthUtil.getStringToSign(authInfo);


        //签名
        String signStr = HmacAuthUtil.signature(secretKey, msg);
        if (authorization.equals(signStr)) {
            log.debug("StringToSign:\n" + msg);
            return true;
        } else {
            //验签不通过
            log.warn("StringToSign:\n" + msg);
            log.warn("authorization no pass:" + authorization + " VS " + signStr);
        }

        HttpUtil.sendJson(response, BaseResponse.error(ErrorCodeConstant.CommonErrorCode.PERMISSION_EXCEPTION));
        return false;
    }


}
