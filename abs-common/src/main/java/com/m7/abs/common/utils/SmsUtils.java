package com.m7.abs.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 全局工具类
 */
public class SmsUtils {

    /**
     * 随机UUID
     * @return 32位随机字符串
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 正则匹配
     * @param patternString 正则字符串
     * @param value 待匹配数据
     * @return true/false
     */
    public static boolean match(String patternString, String value) {
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }



    /**
     * 获取当前时间 yyyy-MM-dd HH:mm:ss格式
     * @return 当前时间
     */
    public static String getCurrentTime() {
        return getCurrentTime("yyyy-MM-dd HH:mm:ss");
    }

    public static String getCurrentTime(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(new Date());
    }

    public static String convertTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return simpleDateFormat.format(date);
    }

}
