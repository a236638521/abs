package com.m7.abs.support.core.storage;

import com.m7.abs.common.constant.common.StorageType;
import com.m7.abs.support.common.constant.AbsSupportProperties;
import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import com.obs.services.exception.ObsException;
import com.obs.services.model.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Slf4j
public class HuaweiObs implements CloudStorage {
    private ObsClient obsClient;
    private String endPoint;
    private String ak;
    private String sk;
    private String bucketName;
    private String bucketFilePrefix;
    private final AbsSupportProperties.ObsProperties obsProperties;

    public HuaweiObs(AbsSupportProperties.ObsProperties obsProperties) {
        this.obsProperties = obsProperties;
        this.endPoint = obsProperties.getEndpoint();
        this.ak = obsProperties.getAccessKey();
        this.sk = obsProperties.getSecretKey();
        this.bucketName = obsProperties.getBucketName();
        bucketFilePrefix = "https://" + this.bucketName + "." + this.endPoint;
        initClient();
    }

    @Override
    public void uploadFile(InputStream inputStream, String key) {
        try {
            obsClient.putObject(bucketName, key, inputStream);
        } catch (Exception e) {
            log.error("huawei cloud upload fail", e);
            throw e;
        }
    }

    @Override
    public void multipartUploadFile(File file, String key) throws Exception {
        long begin = System.currentTimeMillis();
        try {
            /*
             * Step 1: initiate multipart upload
             */

            InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest();
            request.setBucketName(bucketName);
            request.setObjectKey(key);
            request.setAcl(obsProperties.getCannedAcl());
            InitiateMultipartUploadResult result = obsClient.initiateMultipartUpload(request);

            /*
             * Step 2: upload a part
             */
            log.info("开始上传[OBS]");
            UploadPartResult uploadPartResult = obsClient.uploadPart(bucketName, key, result.getUploadId(), 1, new FileInputStream(file));

            /*
             * Step 3: complete multipart upload
             */
            CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest();
            completeMultipartUploadRequest.setBucketName(bucketName);
            completeMultipartUploadRequest.setObjectKey(key);
            completeMultipartUploadRequest.setUploadId(result.getUploadId());
            PartEtag partEtag = new PartEtag();
            partEtag.setPartNumber(uploadPartResult.getPartNumber());
            partEtag.setEtag(uploadPartResult.getEtag());
            completeMultipartUploadRequest.getPartEtag().add(partEtag);
            obsClient.completeMultipartUpload(completeMultipartUploadRequest);
        } catch (ObsException e) {
            log.error("Response Code: " + e.getResponseCode());
            log.error("Error Message: " + e.getErrorMessage());
            log.error("Error Code:       " + e.getErrorCode());
            log.error("Request ID:      " + e.getErrorRequestId());
            log.error("Host ID:           " + e.getErrorHostId());
            throw e;
        }
        long end = System.currentTimeMillis();
        log.info("OBS 总上传耗时：{} ms", (end - begin));
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
        TemporarySignatureRequest request = new TemporarySignatureRequest(HttpMethodEnum.GET, bucketName, key, null, 3600);
        TemporarySignatureResponse temporarySignature = obsClient.createTemporarySignature(request);
        return temporarySignature.getSignedUrl();
    }

    @Override
    public void shutdown() {
        if (obsClient != null) {
            try {
                obsClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getId() {
        return StorageType.HW_OBS_STORAGE;
    }

    private void initClient() {
        ObsConfiguration config = new ObsConfiguration();
        config.setSocketTimeout(30000);
        config.setConnectionTimeout(10000);
        config.setEndPoint(endPoint);
        /*
         * Constructs a obs client instance with your account for accessing OBS
         */
        obsClient = new ObsClient(ak, sk, config);

        /*
         * Create bucket
         */
        boolean exists = obsClient.headBucket(bucketName);
        if (!exists) {
            obsClient.createBucket(bucketName);
        }
    }

}
