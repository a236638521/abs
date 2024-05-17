package com.m7.abs.support.core.storage;

import com.m7.abs.common.constant.keys.CommonSessionKeys;
import com.m7.abs.common.domain.base.StorageFileBox;
import com.m7.abs.common.domain.vo.support.PreSignedUrlVO;
import com.m7.abs.common.utils.DateUtil;
import com.m7.abs.common.utils.FastJsonUtils;
import com.m7.abs.common.utils.FileUtil;
import com.m7.abs.common.utils.MyStringUtils;
import com.m7.abs.support.common.constant.AbsSupportProperties;
import com.m7.abs.support.core.exception.SaveToOssFailedException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhuhf
 */
@Slf4j
@Component
public class MixCloudStorage implements ApplicationListener<ApplicationReadyEvent> {
    private List<CloudStorage> cloudStorageList;
    private static final String OSS_DATA_TEMPORARY_FILE_PATH = "data" + File.separator + "file" + File.separator + "temp";
    @Resource
    private AbsSupportProperties supportProperties;

    public void uploadFile(InputStream inputStream, String key) throws Exception {
        String currentDate = DateUtil.parseDateToStr(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD);
        String fileTempPath = supportProperties.getRecordingDump().getTempFileBasePath() + OSS_DATA_TEMPORARY_FILE_PATH + File.separator + currentDate;
        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        String filePathName = fileTempPath + File.separator + fileName;

        try {
            writeIntoFile(inputStream, fileTempPath, fileName);
            cloudStorageList.stream().forEach(cloudStorage -> {
                try {
                    FileInputStream fileInputStream = new FileInputStream(filePathName);
                    cloudStorage.uploadFile(new BufferedInputStream(fileInputStream), key);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            //删除临时文件,如文件夹为空同时清除文件夹
            FileUtil.deleteTempFiles(filePathName);
        }

    }

    public void multipartUploadFile(String fileUrl, String key, List<String> storageIds) throws Exception {
        String reqId = MDC.get(CommonSessionKeys.REQ_ID_KEY);
        File tempFile = null;
        try {
            tempFile = saveTempFile(fileUrl, 3);

            if (tempFile == null) {
                log.info("download file failed!");
                return;
            }

            if (storageIds != null) {
                log.info("Start upload file,file size:{}", tempFile.length());
                File finalTempFile = tempFile;
                storageIds.parallelStream().forEach(storageId -> {
                    String reqIdInner = MDC.get(CommonSessionKeys.REQ_ID_KEY);
                    if (StringUtils.isEmpty(reqIdInner)) {
                        MDC.put(CommonSessionKeys.REQ_ID_KEY, reqId);
                    }
                    Optional<CloudStorage> first = cloudStorageList.parallelStream().filter(storage -> storage.getId().equals(storageId)).findFirst();
                    if (first != null && first.isPresent()) {
                        CloudStorage cloudStorage = first.get();
                        if (cloudStorage != null) {
                            try {
                                cloudStorage.multipartUploadFile(finalTempFile, key);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else {
                        log.info("unknown storage:" + storageId);
                    }
                });
            } else {
                log.info("storageIds is empty");
            }

        } finally {
            if (tempFile != null) {
                FileUtil.deleteFilesAndDir(tempFile);
            }
        }
    }

    /**
     * 保存文件至本地,并校验文件完整性
     * 文件下载不完整,则需重试 retryTimes 次
     *
     * @param fileUrl
     * @param retryTimes
     * @return
     */
    public File saveTempFile(String fileUrl, int retryTimes) {

        InputStream stream = null;
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(supportProperties.getRecordingDump().getDownloadFileConnectTimeOut());
            conn.setReadTimeout(supportProperties.getRecordingDump().getDownloadFileReadTimeOut());
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                int contentLength = conn.getContentLength();
                stream = conn.getInputStream();
                String fileTempPath = supportProperties.getRecordingDump().getTempFileBasePath() + OSS_DATA_TEMPORARY_FILE_PATH + File.separator + DateUtil.parseDateToStr(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD);
                String fileName = MyStringUtils.randomUUID();
                File file = writeIntoFile(stream, fileTempPath, fileName);
                if (file != null) {
                    log.info("save temp file ,expect:{},real:{}", contentLength, file.length());
                }
                if (contentLength != -1 && file != null && contentLength != file.length()) {
                    /**
                     * 下载文件大小不一致,重试
                     */
                    retryTimes--;
                    if (retryTimes < 0) {
                        log.warn("Exceeded maximum retry count, stop saving file.");
                        return file;
                    } else {
                        log.warn("files are different sizes ,expect:{},real:{},remaining {} times,file url:{}", contentLength, file.length(), retryTimes, fileUrl);
                        file.delete();
                        return saveTempFile(fileUrl, retryTimes);
                    }
                } else {
                    return file;
                }
            } else {
                log.error("Abnormal file link status,responseCode:{},fileUrl:{}", responseCode, fileUrl);
                return null;
            }

        } catch (Exception e) {
            log.error("Failed to transfer file,{}", fileUrl, e);
            throw new SaveToOssFailedException(e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    log.error("close stream error", e);
                    throw new SaveToOssFailedException(e);
                }
            }
        }
    }

    private File writeIntoFile(InputStream inputStream, String filePath, String fileName) {
        RandomAccessFile file = null;
        try {
            String filePathName = filePath + File.separator + fileName;
            FileUtil.createFileDirs(filePath);
            file = new RandomAccessFile(filePathName, "rw");
            int len;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes)) != -1) {
                file.write(bytes, 0, len);
            }
            return new File(filePathName);
        } catch (Exception e) {
            log.error("save temp file error", e);
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    log.error("close file error", e);
                }
            }
        }
        return null;
    }


    public List<PreSignedUrlVO> generatePreSignedUrl(List<String> storageIds, String key, Date expireDate) {
        List<PreSignedUrlVO> result = new ArrayList<>();
        if (storageIds != null) {
            storageIds.parallelStream().forEach(storageId -> {
                Optional<CloudStorage> first = cloudStorageList.parallelStream().filter(storage -> storage.getId().equals(storageId)).findFirst();
                if (first != null && first.isPresent()) {
                    CloudStorage cloudStorage = first.get();
                    if (cloudStorage != null) {
                        try {
                            String url = cloudStorage.generatePreSignedUrl(key, expireDate);
                            result.add(PreSignedUrlVO.builder()
                                    .storageId(cloudStorage.getId())
                                    .preSignedUrl(url)
                                    .build());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    log.info("unknown storage:" + storageId);
                }
            });
        } else {
            log.info("storageIds is empty");
        }

        return result;
    }

    public List<StorageFileBox> getAllFileHost() {
        List<StorageFileBox> allFileHost = new ArrayList<>();
        if (cloudStorageList == null) {
            return null;
        }
        cloudStorageList.stream().forEach(cloudStorage -> {
            allFileHost.add(StorageFileBox.builder()
                    .fileHost(cloudStorage.getFileHost())
                    .storageId(cloudStorage.getId())
                    .build());
        });
        return allFileHost;
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        Map<String, CloudStorage> beansOfType = event.getApplicationContext().getBeansOfType(CloudStorage.class);
        cloudStorageList = beansOfType.values().stream().collect(Collectors.toList());
        if (cloudStorageList == null || cloudStorageList.size() == 0) {
            throw new IllegalArgumentException("未指定启用的云存储类型");
        } else {
            log.info("云存储已初始化:{}", FastJsonUtils.toJSONString(cloudStorageList));
        }
    }
}
