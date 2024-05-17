package com.m7.abs.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.m7.abs.common.constant.keys.CommonSessionKeys;
import com.m7.abs.common.domain.base.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.MDC;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

@Slf4j
public class HttpUtil {

    private final static int SOCKET_TIME_OUT = 15 * 1000;
    private final static int CONNECT_TIME_OUT = 15 * 1000;


    /**
     * 发送json信息
     *
     * @param response
     * @param body
     */
    public static void sendJson(HttpServletResponse response, BaseResponse body) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            String requestId = body.getRequestId();
            if (StringUtils.isEmpty(requestId)) {
                body.setRequestId(MDC.get(CommonSessionKeys.REQ_ID_KEY));
            }

            out = response.getWriter();
            out.append(FastJsonUtils.toJSONString(body));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }


    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

    /**
     * 下载HTML代码
     *
     * @param strUrl 地址
     * @return
     */
    public static String downloadHtml(String strUrl) {

        String data = null;

        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream inputStream = conn.getInputStream();
            byte[] getData = StreamUtil.getByteByStream(inputStream);
            data = new String(getData, "UTF-8");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    /**
     * post请求
     *
     * @param url
     * @param json
     * @return
     */
    public static JSONObject doPostJson(String url, JSONObject json, boolean isLog) {
        if (isLog) {
            log.info("SEND POST JSON ->url: {}", url);
            log.info("SEND POST JSON ->param: {}", json.toJSONString());
        }
        JSONObject response = null;
        Integer statusCode = null;
        try {
            HttpResponse res = doPostJsonSimple(url, json, false);
            if (res != null) {
                statusCode = res.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    String result = EntityUtils.toString(res.getEntity());// 返回json格式：
                    response = JSONObject.parseObject(result);
                }
            }
        } catch (Exception e) {
            log.error("Bad Request:" + url, e);
            return response;
        }
        if (isLog) {
            log.info("POST JSON RESPONSE DATA : " + statusCode + ",response: " + (response == null ? "" : response.toJSONString()));
        }
        return response;
    }

    /**
     * 返回HttpResponse  的post方法
     * ContentType:application/json;charset=UTF-8
     *
     * @param url
     * @param json
     * @param isLog
     * @return
     */
    public static HttpResponse doPostJsonSimple(String url, JSONObject json, boolean isLog) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        if (isLog) {
            log.info("SEND POST JSON ->url: {} ,param:{}", url, json.toJSONString());
        }
        HttpResponse res = null;
        try {
            CloseableHttpClient httpclient = HttpClientBuilder.create().build();

            HttpPost post = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIME_OUT).setConnectTimeout(CONNECT_TIME_OUT).build();
            //设置请求和传输超时时间
            post.setConfig(requestConfig);
            StringEntity s = new StringEntity(json.toString(), "UTF-8");
            s.setContentType("application/json;charset=UTF-8");//发送json数据需要设置contentType
            post.setEntity(s);
            res = httpclient.execute(post);
        } catch (Exception e) {
            log.error("Bad Request:{}", url, e);
            return res;
        }
        if (isLog) {
            log.info("POST JSON RESPONSE DATA : {}", res.getStatusLine().getStatusCode());
        }
        return res;
    }

    public static String doGet(String url, boolean isLog) {
        if (isLog) {
            log.info("SEND GET            : " + url);
        }

        CloseableHttpClient httpclient = HttpClients.createDefault();
        String result = null;
        try {

            HttpGet httpget = new HttpGet(url);

            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIME_OUT).setConnectTimeout(CONNECT_TIME_OUT).build();
            //设置请求和传输超时时间
            httpget.setConfig(requestConfig);

            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                @Override
                public String handleResponse(final HttpResponse response) throws IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            result = httpclient.execute(httpget, responseHandler);
        } catch (Exception e) {
            log.error("Bad Request:" + url, e);
            return null;
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                return result;
            }
        }
        if (isLog) {
            log.info("GET RESPONSE DATA  : " + result);
        }
        return result;
    }

    public static String doPost(String url, String content, Map<String, String> headers, boolean isLog) {
        long startTime = System.currentTimeMillis();
        if (headers == null) {
            headers = new HashedMap();
        }
        if (isLog) {
            log.info("SEND POST          :{} CONTENT:{}", url, content);
        }
        CloseableHttpClient httpclient = null;
        try {
            SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(), NoopHostnameVerifier.INSTANCE);
            httpclient = HttpClients.custom().setSSLSocketFactory(scsf).build();
            //CloseableHttpClient httpclient = HttpClients.createDefault();
        } catch (Exception e) {
            log.error("Bad Request:" + url + ",create httpClient error", e);
            return null;
        }


        HttpPost post = new HttpPost(url);
        String contentType = headers.get(org.apache.http.protocol.HTTP.CONTENT_TYPE);
        contentType = contentType == null ? "application/json;charset=UTF-8" : contentType;
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIME_OUT).setConnectTimeout(CONNECT_TIME_OUT).build();
        //设置请求和传输超时时间
        post.setConfig(requestConfig);

        headers.put(org.apache.http.protocol.HTTP.CONTENT_TYPE, contentType);
        Integer statusCode;
        for (String key : headers.keySet()) {
            post.addHeader(key, headers.get(key));
        }
        String result = null;
        try {
            StringEntity httpEntity = new StringEntity(content, "UTF-8");
            post.setEntity(httpEntity);
            HttpResponse response = httpclient.execute(post);
            statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            log.error("Bad Request:" + url, e);
            return result;
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                return result;
            }
        }
        if (isLog) {
            long l = System.currentTimeMillis() - startTime;
            log.info("[{}] URL:{} COST {} ms <-->  result:{}", statusCode, url, l, FastJsonUtils.formatJson(result));
        }

        return result;
    }

    /**
     * 上传文件到服务器
     *
     * @param inputStream
     * @param serverUrl
     * @return
     */
    public static boolean uploadFile(InputStream inputStream, String fileName, String serverUrl) {

        boolean result = false;

        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(serverUrl);

        try {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIME_OUT).setConnectTimeout(CONNECT_TIME_OUT).build();
            //设置请求和传输超时时间
            post.setConfig(requestConfig);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            builder.addBinaryBody("upfile", inputStream, ContentType.APPLICATION_OCTET_STREAM, fileName);

            HttpEntity httpEntity = builder.build();

            post.setEntity(httpEntity);

            HttpResponse response = client.execute(post);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    public static void uploadFile(String filePath, String FileUploadURL) {
        //1.使用默认的配置的httpclient
        CloseableHttpClient client = HttpClients.createDefault();
        File targetFile = new File(filePath);
        try {
            if (targetFile == null || !targetFile.exists()) {
                throw new FileNotFoundException();
            }
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();

            multipartEntityBuilder
                    .addBinaryBody("uploadFile", targetFile)
                    .setMode(HttpMultipartMode.RFC6532);
            HttpPost httpPost = new HttpPost(FileUploadURL);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIME_OUT).setConnectTimeout(CONNECT_TIME_OUT).build();
            //设置请求和传输超时时间
            httpPost.setConfig(requestConfig);

            httpPost.setEntity(multipartEntityBuilder.build());
            HttpResponse httpResponse = client.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            String content = EntityUtils.toString(httpEntity);
            System.out.println(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从网络Url中下载文件
     *
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    public static boolean downLoadFromUrl(String urlStr, String fileName, String savePath) {
        try {

            if (urlStr != null && urlStr.startsWith("https://")) {
                /**
                 * 跳过https证书认证
                 */
                //  直接通过主机认证
                HostnameVerifier hv = new HostnameVerifier() {
                    @Override
                    public boolean verify(String urlHostName, SSLSession session) {
                        return true;
                    }
                };
                //  配置认证管理器
                TrustManager[] trustAllCerts = {new TrustAllTrustManager()};
                SSLContext sc = SSLContext.getInstance("SSL");
                SSLSessionContext sslsc = sc.getServerSessionContext();
                sslsc.setSessionTimeout(0);
                sc.init(null, trustAllCerts, null);
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                //  激活主机认证
                HttpsURLConnection.setDefaultHostnameVerifier(hv);
            }


            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //得到输入流
            InputStream inputStream = conn.getInputStream();

            //文件保存位置
            File saveDir = new File(savePath);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
            File file = new File(savePath + File.separator + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);

            byte[] b = new byte[1024];
            while ((inputStream.read(b)) != -1) {
                fos.write(b);// 写入数据
            }

            if (fos != null) {
                fos.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }

        } catch (Exception e) {
            String message = e.getMessage();
            if ((e instanceof IOException) && message.contains("Server returned HTTP response code: 403")) {
                log.warn("http downLoadFromUrl FAILED!" + message);
            } else {
                log.error("http downLoadFromUrl ERROR!", e);
            }
            return false;
        }
        return true;
    }

    /**
     * 从网络Url中下载文件
     *
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    public static File downLoadFromUrl(String reqId, String urlStr, String fileName, String savePath) {
        long start = System.currentTimeMillis();
        FileOutputStream fos = null;
        InputStream inputStream = null;
        File file = null;
        try {

            if (urlStr != null && urlStr.startsWith("https://")) {
                /**
                 * 跳过https证书认证
                 */
                //  直接通过主机认证
                HostnameVerifier hv = new HostnameVerifier() {
                    @Override
                    public boolean verify(String urlHostName, SSLSession session) {
                        return true;
                    }
                };
                //  配置认证管理器
                javax.net.ssl.TrustManager[] trustAllCerts = {new TrustAllTrustManager()};
                SSLContext sc = SSLContext.getInstance("SSL");
                SSLSessionContext sslsc = sc.getServerSessionContext();
                sslsc.setSessionTimeout(0);
                sc.init(null, trustAllCerts, null);
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                //  激活主机认证
                HttpsURLConnection.setDefaultHostnameVerifier(hv);
            }


            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时间为10秒
            conn.setConnectTimeout(10 * 1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            int responseCode = conn.getResponseCode();

            if (responseCode == 200) {
                //得到输入流
                inputStream = conn.getInputStream();

                //文件保存位置
                File saveDir = new File(savePath);
                if (!saveDir.exists()) {
                    saveDir.mkdirs();
                }
                file = new File(savePath + File.separator + fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                fos = new FileOutputStream(file);

                byte[] b = new byte[1024];
                while ((inputStream.read(b)) != -1) {
                    fos.write(b);// 写入数据
                }

            } else {
                log.warn("[" + reqId + "] [" + responseCode + "] http downLoadFromUrl FAILED!");
                return file;
            }


        } catch (Exception e) {
            String message = e.getMessage();
            if ((e instanceof IOException) && message.contains("Server returned HTTP response code: 403")) {
                log.warn("[" + reqId + "] http downLoadFromUrl FAILED!" + message);
            } else {
                log.error("[" + reqId + "] http downLoadFromUrl ERROR!", e);
            }
            return file;
        } finally {
            try {
                long end = System.currentTimeMillis();
                log.info("[" + reqId + "] download cost: " + (end - start) + " ms");
                if (fos != null) {
                    fos.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.error("[" + reqId + "] close stream err. ", e);
            }
        }
        if (file != null) {
            long length = file.length();
            log.info("[" + reqId + "] download success. file length [" + length + "] urlStr: " + urlStr + " ; savePath: " + savePath);
        }
        return file;
    }


    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }


    /**
     * 跳过证书认证类
     */
    private static class TrustAllTrustManager implements TrustManager, X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

}
