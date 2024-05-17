package com.m7.abs.api.core.auth.hmac.bean;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * @author KejiePeng
 * @Title:
 * @Description
 * @date 2021/7/7 14:01
 */
@Getter
@Setter
public class AuthInfo {
    /**
     * 签名
     */
    @NotEmpty
    private String authorization;
    /**
     * accessKey 用于获取secretKey
     */
    @NotEmpty
    private String accessKey;
    /**
     * 请求类型
     */
    @NotEmpty
    private String httpMethod;
    /**
     * contentType
     */
    @NotEmpty
    private String contentType;

    /**
     * 非标准HTTP头部信息，是请求中出现的以m7-abs-为前缀的参数。请求中必须包含以下参数：
     * m7-abs-access-key：AccessKey。
     * m7-abs-nonce：随机ID。
     * m7-abs-timestamp：时间戳。
     * 完成以下操作，构造规范头：
     * <p>
     * 将所有以m7-abs-为前缀的HTTP请求头的名字转换成小写字母。例如将m7-abs-Meta-Name: 7Moor 转换成m7-abs-meta-name: 7Moor。
     * 将上一步得到的所有HTTP头按照字典序进行升序排列。
     * 将所有的头和内容用\n分隔符分隔拼成最后的CanonicalHeaders。
     */
    private CanonicalHeaders canonicalHeaders;
    /**
     * CanonicalResource 表示想要访问资源的规范描述，需要将子资源和query参数一同按照字典序，从小到大排列并以&为分隔符生成子资源字符串，即?后的所有参数。
     */
    private String canonicalResource;

    /**
     * 将请求的body用MD5算法加密，再进行Base64编码，将结果添加到Content-MD5中。
     */
    private String bodyMD5;

    public String getCanonicalResource() {
        return canonicalResource == null ? "" : canonicalResource;
    }

    public String getBodyMD5() {
        return bodyMD5 == null ? "" : bodyMD5;
    }
}
