package com.m7.abs.api.core.auth.hmac;


import com.m7.abs.common.utils.CryptoUtil;
import com.m7.abs.common.utils.DateUtil;
import com.m7.abs.common.utils.HttpUtil;
import com.m7.abs.common.utils.MyStringUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author KejiePeng
 * @Title:
 * @Description
 * @date 2021/7/8 16:50
 */
public class HttpDemo {
    private static String CHAR_SET_UTF8 = "UTF-8";
    private static String accessKey = "5db41f25c3ff49edb84a889042a167a2";
    private static String secretKey = "bef254ac76c74e2ab87b75630272b846";

    public static void main(String[] args) throws UnsupportedEncodingException {
        HttpDemo.sendHttp();
    }

    public static void sendHttp() throws UnsupportedEncodingException {
        Date date = new Date();
        String timestamp = DateUtil.parseDateToStr(date, DateUtil.DATE_TIME_FORMAT_UTC);
        String nonce = MyStringUtils.randomUUID();

        String url = "http://10.43.171.253:35601/api/midNum/axb/bind";
        String content = "{\"account\": \"2000148\",\"telA\": \"13911112222\",\"telB\": \"13911113333\",\"telX\": \"13911114444\",\"needRecord\": true,\"expiration\": 60}";
        String contentMd5 = Base64.getEncoder().encodeToString(CryptoUtil.encodeMD5(content).getBytes(CHAR_SET_UTF8));

        Map<String, String> headers = new HashMap<>();
        headers.put("HTTP-method", "POST");
        headers.put("Content-Type", "application/json;charset=UTF-8");
        headers.put("Content-MD5", contentMd5);
        headers.put("m7-abs-nonce", nonce);
        headers.put("m7-abs-timestamp", timestamp);

        StringBuilder stringToSign = new StringBuilder();

        stringToSign.append(contentMd5).append("\n");
        stringToSign.append("application/json;charset=UTF-8").append("\n");
        stringToSign.append("POST").append("\n");
        stringToSign.append("m7-abs-nonce:").append(nonce).append("\n");
        stringToSign.append("m7-abs-timestamp:").append(timestamp).append("\n");
        stringToSign.append("/api/midNum/axb/bind");

        String signature = Base64.getEncoder().encodeToString(CryptoUtil.sha256_HMAC(secretKey, MyStringUtils.toUTF8(stringToSign.toString())));
        headers.put("Authorization", "abs " + accessKey + ":" + signature);

        String s = HttpUtil.doPost(url, content, headers, true);
    }


}
