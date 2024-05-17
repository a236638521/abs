package com.m7.abs.support.core.storage;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.*;
import com.m7.abs.common.constant.common.StorageType;
import com.m7.abs.support.common.constant.AbsSupportProperties.EosProperties;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 中国移动 eos
 *
 * @author zhuhf
 */
@Slf4j
public class MobileEos implements CloudStorage {
    private AmazonS3Client client;
    private final String bucketFilePrefix;
    private final EosProperties eosProperties;

    public MobileEos(EosProperties eosProperties) {
        this.eosProperties = eosProperties;
        String hostname = eosProperties.getHostname().endsWith("/")
                ? eosProperties.getHostname()
                : (eosProperties.getHostname() + "/");
        if (eosProperties.getPathStyleAccess()) {
            bucketFilePrefix = hostname + eosProperties.getBucketName() + "/";
        } else {
            this.bucketFilePrefix = hostname.replace("http://", "http://" + eosProperties.getBucketName() + ".")
                    .replace("https://", "https://" + eosProperties.getBucketName() + ".");
        }
        initClient();
    }


    @Override
    public void uploadFile(InputStream inputStream, String key) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            PutObjectRequest request = new PutObjectRequest(eosProperties.getBucketName(), key, inputStream, metadata);
            request.setCannedAcl(eosProperties.getCannedAcl());
            client.putObject(request);
        } catch (SdkClientException e) {
            log.error("上传文件异常", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void multipartUploadFile(File file, String key) {
        // MultipartFile file是接收自前端的文件
        String bucketName = eosProperties.getBucketName();
        long size = file.length();
        long minPartSize = 5 * 1024 * 1024;
        // 得到总共的段数，和 分段后，每个段的开始上传的字节位置
        List<Long> positions = new ArrayList<>();
        long filePosition = 0;
        while (filePosition < size) {
            positions.add(filePosition);
            filePosition += Math.min(minPartSize, (size - filePosition));
        }
        log.info("总大小：{}，分为{}段", size, positions.size());
        // 创建一个列表保存所有分传的 PartETag, 在分段完成后会用到
        List<PartETag> partETags = new ArrayList<>();
        // 第一步，初始化，声明下面将有一个 Multipart Upload
        InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, key);
        /**
         * 设置文件公共可读，临时设置
         */
        initRequest.setCannedACL(eosProperties.getCannedAcl());
        InitiateMultipartUploadResult initResponse = client.initiateMultipartUpload(initRequest);
        log.info("开始上传[EOS]");
        long begin = System.currentTimeMillis();
        try {
            for (int i = 0; i < positions.size(); i++) {
                long time1 = System.currentTimeMillis();
                UploadPartRequest uploadRequest = new UploadPartRequest()
                        .withBucketName(bucketName)
                        .withKey(key)
                        .withUploadId(initResponse.getUploadId())
                        .withPartNumber(i + 1)
                        .withFileOffset(positions.get(i))
                        .withFile(file)
                        .withPartSize(Math.min(minPartSize, (size - positions.get(i))));
                // 第二步，上传分段，并把当前段的 PartETag 放到列表中
                partETags.add(client.uploadPart(uploadRequest).getPartETag());
                long time2 = System.currentTimeMillis();
                log.info("第{}段上传耗时：{} ms", i + 1, (time2 - time1));
            }
            // 第三步，完成上传，合并分段
            CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucketName, key,
                    initResponse.getUploadId(), partETags);
            client.completeMultipartUpload(compRequest);
        } catch (Exception e) {
            client.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, key, initResponse.getUploadId()));
            log.error("Failed to upload, " + e.getMessage());
            throw new RuntimeException(e);
        }
        long end = System.currentTimeMillis();
        log.info("EOS 总上传耗时：{} ms", (end - begin));
    }

    @Override
    public String fileAddress(String key) {
        return bucketFilePrefix + key;
    }

    @Override
    public String getFileHost() {
        return bucketFilePrefix.endsWith("/") ? bucketFilePrefix.substring(0, bucketFilePrefix.length() - 1) : bucketFilePrefix;
    }

    @Override
    public String generatePreSignedUrl(String key, Date expireDate) {
        if (key.startsWith("/")) {
            key = key.substring(1);
        }
        String bucketName = eosProperties.getBucketName();
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, key);
        request.setExpiration(expireDate);
        ResponseHeaderOverrides headerOverrides = new ResponseHeaderOverrides();
        headerOverrides.setContentDisposition("inline");
        request.setResponseHeaders(headerOverrides);
        URL url = client.generatePresignedUrl(request);
        return url.getProtocol() + "://" + url.getHost() + url.getPath() + "?" + url.getQuery();
    }

    private void initClient() {
        ClientConfiguration opts = new ClientConfiguration();
        opts.setSignerOverride("S3SignerType");
        AWSCredentials credentials =
                new BasicAWSCredentials(eosProperties.getAccessKey(), eosProperties.getSecretKey());
        S3ClientOptions s3ClientOptions = new S3ClientOptions();
        s3ClientOptions.setPathStyleAccess(eosProperties.getPathStyleAccess());
        AmazonS3Client client = new AmazonS3Client(credentials, opts);
        client.setEndpoint(getEndpoint());
        this.client = client;
        if (!client.doesBucketExist(eosProperties.getBucketName())) {
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(eosProperties.getBucketName());
            createBucketRequest.setCannedAcl(eosProperties.getCannedAcl());
            client.createBucket(createBucketRequest);
        }
    }

    @Override
    public void shutdown() {
        if (client != null) {
            client.shutdown();
        }
    }

    @Override
    public String getId() {
        return StorageType.YD_EOS_STORAGE;
    }

    /**
     * 判断内外网地址
     *
     * @return endpoint: 新建oss使用的地址
     */
    public String getEndpoint() {
        String endpoint = eosProperties.getInternalHostname();
        try {
            URL url = new URL(endpoint);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(2000);
            con.getResponseCode(); //判断内网链接地址是否可用
        } catch (Exception e) {
            log.debug("连接失败,endpoint: {}", endpoint, e);
            endpoint = eosProperties.getHostname();
        }
        return endpoint;
    }

}
