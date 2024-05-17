package com.m7.abs.support.core.storage;

import com.aliyun.oss.*;
import com.aliyun.oss.model.*;
import com.m7.abs.common.constant.common.StorageType;
import com.m7.abs.support.common.constant.AbsSupportProperties;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 阿里云 oss 云存储
 *
 * @author zhuhf
 */
@Slf4j
public class AliyunOss implements CloudStorage {
    private static final String PATH_SEPARATOR = "/";
    private AbsSupportProperties.AliyunOssProperties ossProperties;
    /**
     * 填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
     */
    private final String endpoint;
    /**
     * 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
     */
    private final String accessKeyId;

    private final String accessKeySecret;
    private final String bucketName;
    private final CannedAccessControlList cannedACL;
    private String bucketFilePrefix;
    private final Lock lock = new ReentrantLock();

    private OSS ossClient;


    public AliyunOss(AbsSupportProperties.AliyunOssProperties ossProperties) {
        this.ossProperties = ossProperties;
        this.endpoint = this.getEndpoint();
        this.accessKeyId = ossProperties.getAccessKeyId();
        this.accessKeySecret = ossProperties.getAccessKeySecret();
        this.bucketName = ossProperties.getBucketName();
        this.cannedACL = ossProperties.getCannedAcl();

        bucketFilePrefix = ossProperties.getEndpoint().replace("http://", "http://" + bucketName + ".")
                .replace("https://", "https://" + bucketName + ".");
        if (!bucketFilePrefix.endsWith("/")) {
            bucketFilePrefix += "/";
        }
    }

    @Override
    public void shutdown() {
        // 关闭OSSClient。
        if (ossClient != null) {
            ossClient.shutdown();
        }
    }

    /**
     * 上传失败抛出异常
     *
     * @param inputStream in
     * @param key         oss key
     * @throws OSSException    oss ex
     * @throws ClientException client ex
     */
    @Override
    public void uploadFile(InputStream inputStream, String key) throws OSSException, ClientException {
        try {
            lazyConn();
            ossClient.putObject(bucketName, key, inputStream);
        } catch (OSSException | ClientException e) {
            log.error("aliyun upload fail", e);
            throw e;
        }
    }

    @Override
    public void multipartUploadFile(File file, String key) throws Exception {
        long begin = System.currentTimeMillis();
        try {
            lazyConn();
            // 创建InitiateMultipartUploadRequest对象。
            InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, key);
            // 初始化分片。
            InitiateMultipartUploadResult upResult = ossClient.initiateMultipartUpload(request);
            // 返回uploadId，它是分片上传事件的唯一标识。您可以根据该uploadId发起相关的操作，例如取消分片上传、查询分片上传等。
            String uploadId = upResult.getUploadId();
            // partETags是PartETag的集合。PartETag由分片的ETag和分片号组成。
            List<PartETag> partETags = new ArrayList<PartETag>();
            // 每个分片的大小，用于计算文件有多少个分片。单位为字节。
            final long partSize = 5 * 1024 * 1024L;   //5 MB。

            long fileLength = file.length();
            int partCount = (int) (fileLength / partSize);
            if (fileLength % partSize != 0) {
                partCount++;
            }
            log.info("总大小：{}，分为{}段", fileLength, partCount);
            // 遍历分片上传。
            log.info("开始上传[OSS]");
            for (int i = 0; i < partCount; i++) {
                long startPos = i * partSize;
                long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
                long time1 = System.currentTimeMillis();
                InputStream inStream = new FileInputStream(file);
                // 跳过已经上传的分片。
                inStream.skip(startPos);
                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(bucketName);
                uploadPartRequest.setKey(key);
                uploadPartRequest.setUploadId(uploadId);
                uploadPartRequest.setInputStream(inStream);
                // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100 KB。
                uploadPartRequest.setPartSize(curPartSize);
                // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出此范围，OSS将返回InvalidArgument错误码。
                uploadPartRequest.setPartNumber(i + 1);
                // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
                UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
                // 每次上传分片之后，OSS的返回结果包含PartETag。PartETag将被保存在partETags中。
                partETags.add(uploadPartResult.getPartETag());
                long time2 = System.currentTimeMillis();
                log.info("第{}段上传耗时：{} ms", i + 1, (time2 - time1));
            }

            // 创建CompleteMultipartUploadRequest对象。
            // 在执行完成分片上传操作时，需要提供所有有效的partETags。OSS收到提交的partETags后，会逐一验证每个分片的有效性。当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
            CompleteMultipartUploadRequest completeMultipartUploadRequest =
                    new CompleteMultipartUploadRequest(bucketName, key, uploadId, partETags);

            // 完成分片上传。
            CompleteMultipartUploadResult completeMultipartUploadResult = ossClient.completeMultipartUpload(completeMultipartUploadRequest);
            log.info(completeMultipartUploadResult.getETag());
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            log.error("Error Message:" + oe.getErrorMessage());
            log.error("Error Code:" + oe.getErrorCode());
            log.error("Request ID:" + oe.getRequestId());
            log.error("Host ID:" + oe.getHostId());
            throw oe;
        } catch (ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.error("Error Message:" + ce.getMessage());
            throw ce;
        }
        long end = System.currentTimeMillis();
        log.info("OSS 总上传耗时：{} ms", (end - begin));
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
        lazyConn();
        if (key.startsWith("/")) {//去除最面前的斜杠
            key = key.substring(1);
        }
        // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
        URL url = ossClient.generatePresignedUrl(bucketName, key, expireDate);
        String decodePath = "";
        try {
            decodePath = URLDecoder.decode(url.getPath(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url.getProtocol() + "://" + url.getHost() + decodePath + "?" + url.getQuery();
    }

    private void lazyConn() {
        if (ossClient == null) {
            lock.lock();
            try {
                if (ossClient == null) {
                    // 创建OSSClient实例
                    ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
                    makeSureTheBucketExists();
                }
            } finally {
                lock.unlock();
            }
        }
    }

    private void makeSureTheBucketExists() {
        try {
            ossClient.getBucketInfo(bucketName);
        } catch (OSSException ossException) {
            if (OSSErrorCode.NO_SUCH_BUCKET.equals(ossException.getErrorCode())) {
                CreateBucketRequest request = new CreateBucketRequest(bucketName);
                request.setCannedACL(cannedACL);
                ossClient.createBucket(request);
            } else {
                throw ossException;
            }
        }
    }

    @Override
    public String getId() {
        return StorageType.ALI_OSS_STORAGE;
    }

    /**
     * 判断内外网地址
     *
     * @return endpoint: 新建oss使用的地址
     */
    public String getEndpoint() {
        String endpoint = ossProperties.getInternalEndpoint();
        try {
            URL url = new URL(endpoint);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(2000);
            con.getResponseCode(); //判断内网链接地址是否可用
        } catch (Exception e) {
            log.debug("连接失败,endpoint: {}", endpoint, e);
            endpoint = ossProperties.getEndpoint();
        }
        return endpoint;
    }


}
