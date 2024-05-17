package com.m7.abs.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author KejiePeng
 * @Title:
 * @Description
 * @date 2021/5/15 15:07
 */
@Slf4j
public class MyStringUtils {
    private final static Pattern urlPattern = Pattern.compile("\\S*[?]\\S*");

    /**
     * 下划线转驼峰法
     *
     * @param line       源字符串
     * @param smallCamel 大小驼峰,是否为小驼峰
     * @return 转换后的字符串
     */
    public static String underlineToCamel(String line, boolean smallCamel) {
        if (line == null || "".equals(line)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("([A-Za-z\\d]+)(_)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(smallCamel && matcher.start() == 0 ? Character.toLowerCase(word.charAt(0)) : Character.toUpperCase(word.charAt(0)));
            int index = word.lastIndexOf('_');
            if (index > 0) {
                sb.append(word.substring(1, index).toLowerCase());
            } else {
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰法转下划线
     *
     * @param line 源字符串
     * @return 转换后的字符串
     */
    public static String camelToUnderline(String line) {
        if (line == null || "".equals(line)) {
            return "";
        }
        line = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(word.toLowerCase());
            sb.append(matcher.end() == line.length() ? "" : "_");
        }
        return sb.toString();
    }


    private static String changeCharSet(
            String str, String newCharset) throws UnsupportedEncodingException {
        if (str != null) {
            // 用默认字符编码解码字符串。
            byte[] bs = str.getBytes();
            // 用新的字符编码生成字符串
            return new String(bs, newCharset);
        }
        return str;
    }

    /**
     * 字符串转化为UTF-8
     *
     * @param str
     * @return
     */
    public static String toUTF8(String str) {
        String result = str;
        try {
            result = changeCharSet(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("String toUTF8 error", e);
        }
        return result;
    }

    /**
     * 字节数组转化为UTF-8
     *
     * @param bty
     * @return
     */
    public static String toUTF8(byte[] bty) {
        try {
            if (bty.length > 0) {
                return new String(bty, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            log.error("byte toUTF8 error", e);
        }
        return new String(bty);
    }


    /**
     * 将value转换为对应的class类型 fieldType
     *
     * @param fieldType class类型
     * @param value     需要转换的值
     * @return Object
     * @throws Exception 反射获取类 Class.forName 可能会导致异常
     */
    public static Object transformValue(Class fieldType, String value) throws Exception {
        if (org.apache.commons.lang3.StringUtils.isEmpty(value)) {
            return null;
        }

        if (fieldType == String.class) {
            return String.valueOf(value);
        } else if (fieldType == Boolean.class || fieldType == boolean.class) {
            return Boolean.valueOf(value);
        } else if (fieldType == Byte.class || fieldType == byte.class) {
            return Byte.valueOf(value);
        } else if (fieldType == Double.class || fieldType == double.class) {
            return Double.valueOf(value);
        } else if (fieldType == Float.class || fieldType == float.class) {
            return Float.valueOf(value);
        } else if (fieldType == Integer.class || fieldType == int.class) {
            return Integer.valueOf(value);
        } else if (fieldType == Long.class || fieldType == long.class) {
            return Long.valueOf(value);
        } else if (fieldType == Short.class || fieldType == short.class) {
            return Short.valueOf(value);
        } else if (fieldType == Character.class || fieldType == char.class) {
            return value.charAt(0);
        } else if (fieldType == BigDecimal.class) {
            return new BigDecimal(value);
        } else if (fieldType == BigInteger.class) {
            return new BigInteger(value);
        } else if (fieldType == Date.class) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(value);
        } else if (fieldType == List.class) {
            return Arrays.asList(value.split(","));
        } else if (fieldType == Set.class) {
            return new HashSet<>(Arrays.asList(value.split(",")));
        } else if (fieldType.isEnum()) { // 枚举类型
            Class<?> cl = Class.forName(fieldType.getName());
            Field field = cl.getDeclaredField(value);
            return field.get(cl);
        } else if (fieldType == Pattern.class) {
            return Pattern.compile(value);
        } else {
            return value;
        }

    }

    /**
     * 获取链接的后缀名
     *
     * @return
     */
    public static String parseSuffix(String url) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(url)) {
            return "";
        }
        Matcher matcher = urlPattern.matcher(url);
        String[] split = null;
        if (matcher.find()) {
            String[] spUrl = url.split("\\?");
            if (spUrl != null && spUrl.length > 0) {
                String s = spUrl[0];
                String[] spSpUrl = s.split("/");
                String endUrl = spSpUrl[spSpUrl.length - 1];
                split = endUrl.split("\\.");
            } else {
                return "";
            }
        } else {
            String[] spUrl = url.split("/");
            int len = spUrl.length;
            String endUrl = spUrl[len - 1];
            split = endUrl.split("\\.");
        }

        if (split != null && split.length > 0) {
            return split[split.length - 1];
        } else {
            return "";
        }
    }

    /**
     * 返回去除_的UUID
     *
     * @return
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String encryptPhoneNumber(String number) {
        if (StringUtils.isEmpty(number)) {
            return number;
        }

        if (ValidationUtil.isPhone(number)) {
            return hideNumber(number, 3, 4);
        }else if (number.length()>=8){
            return hideNumber(number, 2, 4);
        }

        return number;
    }

    public static String hideNumber(String str, int front, int behind) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        String regex = "(?<=[\\w]{" + front + "})\\w(?=[\\w]{" + behind + "})";
        return str.replaceAll(regex, "*");
    }
}