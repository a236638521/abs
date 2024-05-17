package com.m7.abs.common.utils;

import java.security.SecureRandom;

/**
 * @author Kejie Peng
 * @date 2023年 10月10日 16:43:00
 */
public class RandomStringGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateRandomString(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        String randomString = generateRandomString(10);
        System.out.println(randomString);
    }
}
