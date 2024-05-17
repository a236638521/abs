package com.m7.abs.common.utils;

import java.util.UUID;

/**
 * 密码工具类
 *
 * @author Louis
 * @date Sep 1, 2018
 */
public class PasswordUtils {

    /**
     * 匹配密码
     *
     * @param salt    盐
     * @param rawPass 明文
     * @param encPass 密文
     * @return
     */
    public static boolean matches(String salt, String rawPass, String encPass) {
        return new PasswordEncoder(salt).matches(encPass, rawPass);
    }

    /**
     * 明文密码加密
     *
     * @param rawPass 明文
     * @param salt
     * @return
     */
    public static String encode(String rawPass, String salt) {
        return new PasswordEncoder(salt).encode(rawPass);
    }

    /**
     * 获取加密盐
     *
     * @return
     */
    public static String getSalt() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
    }

    /**
     * 产生随机数(Salt)
     *
     * @return
     */
    public static String generateRandomString() {
        return UUID.randomUUID().toString();
    }

    /**
     * 产生密码
     *
     * @param password
     * @param salt
     * @return
     */
    public static String generatePassword(String password, String salt) {
        return MD5Util.hash(password + salt);
    }

    /**
     * 验证密码
     *
     * @param password
     * @param hash
     * @param salt
     * @return
     */
    public static boolean match(String password, String hash, String salt) {
        return generatePassword(password, salt).equals(hash);
    }

    /**
     * 检测密码安全等级
     *
     * @param password
     * @return
     */
    public static int checkLevel(String password) {
        String str = "/^[0-9]{1,20}$/"; // 不超过20位的数字组合
        String str1 = "/^[0-9|a-z|A-Z]{1,20}$/"; // 由字母、数字组成，不超过20位
        String str2 = "/^[a-zA-Z]{1,20}$/"; // 由字母不超过20位
        if (password.matches(str)) {
            return 1;
        }
        if (password.matches(str2)) {
            return 2;
        }
        if (password.matches(str1)) {
            return 3;
        }
        return 1;
    }
}
