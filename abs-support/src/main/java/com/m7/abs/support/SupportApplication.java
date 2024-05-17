package com.m7.abs.support;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author KejiePeng
 * @Title:
 * @Description
 * @date 2021/12/14 10:16
 */
@SpringBootApplication
@ServletComponentScan
@EnableScheduling
public class SupportApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(SupportApplication.class, args);
    }
}
