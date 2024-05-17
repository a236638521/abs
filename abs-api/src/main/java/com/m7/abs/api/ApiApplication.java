package com.m7.abs.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author KejiePeng
 * @Title:
 * @Description
 * @date 2021/12/14 10:16
 */
@SpringBootApplication
@ServletComponentScan
@EnableCaching  //开启缓存
@EnableFeignClients
@EnableDiscoveryClient
public class ApiApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
