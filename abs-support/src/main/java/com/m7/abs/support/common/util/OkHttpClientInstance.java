package com.m7.abs.support.common.util;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhuhf
 */
@Slf4j
public class OkHttpClientInstance {

    public static OkHttpClient getInstance() {
        return OkHttpClientInstanceHolder.OK_HTTP_CLIENT;
    }

    private static class OkHttpClientInstanceHolder {
        private static final OkHttpClient OK_HTTP_CLIENT = initHttpClient();

        private static OkHttpClient initHttpClient() {
            final TrustManager[] trustAllCerts = trustAllCerts();
            final SSLSocketFactory sslSocketFactory = sslSocketFactory(trustAllCerts);
            return new OkHttpClient.Builder()
                    // 忽略证书校验
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true)
                    .connectionPool(new ConnectionPool())
                    .retryOnConnectionFailure(true)
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .writeTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .build();
        }

        private static TrustManager[] trustAllCerts() {
            return new TrustManager[] {
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {}

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {}

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[] {};
                    }
                }
            };
        }

        private static SSLSocketFactory sslSocketFactory(TrustManager[] trustAllCerts) {
            final SSLContext sslContext;
            final SSLSocketFactory sslSocketFactory;
            try {
                sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                sslSocketFactory = sslContext.getSocketFactory();
                return sslSocketFactory;
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                log.error("ssl socket factory has error.", e);
                throw new RuntimeException(e);
            }
        }
    }
}
