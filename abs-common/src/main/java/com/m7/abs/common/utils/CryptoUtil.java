package com.m7.abs.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URL;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

/**
 * @author KejiePeng
 * @Title:
 * @Description
 * @date 2021/7/7 13:44
 */
@Slf4j
public class CryptoUtil {
    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * MD5加密
     *
     * @param bytes an array of byte.
     * @return a {@link java.lang.String} object.
     */
    public static String encodeMD5(final byte[] bytes) {
        return DigestUtils.md5Hex(bytes);
    }

    /**
     * MD5加密，默认UTF-8
     *
     * @param str a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String encodeMD5(final String str) {
        return encodeMD5(str, DEFAULT_CHARSET);
    }

    /**
     * MD5加密
     *
     * @param str     a {@link java.lang.String} object.
     * @param charset a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String encodeMD5(final String str, final String charset) {
        if (str == null) {
            return null;
        }
        try {
            byte[] bytes = str.getBytes(charset);
            return encodeMD5(bytes);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * SHA加密
     *
     * @param bytes an array of byte.
     * @return a {@link java.lang.String} object.
     */
    public static String encodeSHA(final byte[] bytes) {
        return DigestUtils.sha512Hex(bytes);
    }

    /**
     * SHA加密
     *
     * @param str     a {@link java.lang.String} object.
     * @param charset a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String encodeSHA(final String str, final String charset) {
        if (str == null) {
            return null;
        }
        try {
            byte[] bytes = str.getBytes(charset);
            return encodeSHA(bytes);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * SHA加密,默认utf-8
     *
     * @param str a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String encodeSHA(final String str) {
        return encodeSHA(str, DEFAULT_CHARSET);
    }

    /**
     * BASE64加密
     *
     * @param bytes an array of byte.
     * @return a {@link java.lang.String} object.
     */
    public static String encodeBASE64(final byte[] bytes) {
        return new String(Base64.encodeBase64String(bytes));
    }

    /**
     * BASE64加密
     *
     * @param str     a {@link java.lang.String} object.
     * @param charset a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String encodeBASE64(final String str, String charset) {
        if (str == null) {
            return null;
        }
        try {
            byte[] bytes = str.getBytes(charset);
            return encodeBASE64(bytes);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * BASE64加密,默认UTF-8
     *
     * @param str a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String encodeBASE64(final String str) {
        return encodeBASE64(str, DEFAULT_CHARSET);
    }

    /**
     * BASE64解密,默认UTF-8
     *
     * @param str a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String decodeBASE64(String str) {
        return decodeBASE64(str, DEFAULT_CHARSET);
    }

    /**
     * BASE64解密
     *
     * @param str     a {@link java.lang.String} object.
     * @param charset 字符编码
     * @return a {@link java.lang.String} object.
     */
    public static String decodeBASE64(String str, String charset) {
        try {
            byte[] bytes = str.getBytes(charset);
            return new String(Base64.decodeBase64(bytes));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * CRC32字节校验
     *
     * @param bytes an array of byte.
     * @return a {@link java.lang.String} object.
     */
    public static String crc32(byte[] bytes) {
        CRC32 crc32 = new CRC32();
        crc32.update(bytes);
        return Long.toHexString(crc32.getValue());
    }

    /**
     * CRC32字符串校验
     *
     * @param str     a {@link java.lang.String} object.
     * @param charset a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String crc32(final String str, String charset) {
        try {
            byte[] bytes = str.getBytes(charset);
            return crc32(bytes);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * CRC32字符串校验,默认UTF-8编码读取
     *
     * @param str a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String crc32(final String str) {
        return crc32(str, DEFAULT_CHARSET);
    }

    /**
     * CRC32流校验
     *
     * @param input a {@link java.io.InputStream} object.
     * @return a {@link java.lang.String} object.
     */
    public static String crc32(InputStream input) {
        CRC32 crc32 = new CRC32();
        CheckedInputStream checkInputStream = null;
        int test = 0;
        try {
            checkInputStream = new CheckedInputStream(input, crc32);
            do {
                test = checkInputStream.read();
            } while (test != -1);
            return Long.toHexString(crc32.getValue());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * CRC32文件唯一校验
     *
     * @param file a {@link java.io.File} object.
     * @return a {@link java.lang.String} object.
     */
    public static String crc32(File file) {
        InputStream input = null;
        try {
            input = new BufferedInputStream(new FileInputStream(file));
            return crc32(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    /**
     * CRC32文件唯一校验
     *
     * @param url a {@link java.net.URL} object.
     * @return a {@link java.lang.String} object.
     */
    public static String crc32(URL url) {
        InputStream input = null;
        try {
            input = url.openStream();
            return crc32(input);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    public static byte[] sha256_HMAC(String secret, String message) {
        byte[] bytes = new byte[0];

        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            bytes = sha256_HMAC.doFinal(message.getBytes());
        } catch (Exception e) {
            log.error("Error HmacSHA256", e);
        }
        return bytes;
    }

    public static String sha256_HMAC_HEX(String secret, String message) {
        String hash = "";
        try {
            byte[] bytes = sha256_HMAC(secret, message);
            hash = byteArrayToHexString(bytes);
        } catch (Exception e) {
            log.error("Error sha256_HMAC_HEX", e);
        }
        return hash;
    }

    /**
     * 将加密后的字节数组转换成字符串
     *
     * @param b 字节数组
     * @return 字符串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF | 0x100);
            if (stmp.length() == 1) {
                hs.append('0');
            }
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

}
