package com.m7.abs.reportexport.core.storage;

import com.m7.abs.common.constant.keys.CommonSessionKeys;
import com.m7.abs.common.domain.base.StorageFileBox;
import com.m7.abs.common.utils.DateUtil;
import com.m7.abs.common.utils.FastJsonUtils;
import com.m7.abs.common.utils.FileUtil;
import com.m7.abs.reportexport.common.properties.AbsReportProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhuhf
 */
@Slf4j
@Component
public class MixCloudStorage implements CloudStorage, ApplicationListener<ApplicationReadyEvent> {
    private List<CloudStorage> cloudStorageList;
    private static final String OSS_DATA_TEMPORARY_FILE_PATH = File.separator + "file" + File.separator + "temp";
    @Resource
    private AbsReportProperties absReportProperties;

    @Override
    public void uploadFile(InputStream inputStream, String key) throws Exception {
        String currentDate = DateUtil.parseDateToStr(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD);
        String fileTempPath = absReportProperties.getRecordingDump().getTempFileBasePath() + OSS_DATA_TEMPORARY_FILE_PATH + File.separator + currentDate;
        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        String filePathName = fileTempPath + File.separator + fileName;

        try {
            saveTempFile(inputStream, fileTempPath, fileName);
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

    public void multipartUploadFile(InputStream inputStream, String key, List<String> storageIds) throws Exception {
        String reqId = MDC.get(CommonSessionKeys.REQ_ID_KEY);
        String fileTempPath = absReportProperties.getRecordingDump().getTempFileBasePath() + OSS_DATA_TEMPORARY_FILE_PATH + File.separator + DateUtil.parseDateToStr(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD);
        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        String filePathName = fileTempPath + File.separator + fileName;
        try {
            File file = saveTempFile(inputStream, fileTempPath, fileName);
            if (storageIds != null) {
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
                                cloudStorage.multipartUploadFile(file, key);
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
            if (inputStream != null) {
                inputStream.close();
            }
            //删除临时文件,如文件夹为空同时清除文件夹
            FileUtil.deleteTempFiles(filePathName);
        }
    }

    public void multipartUploadFile(File file, String key, List<String> storageIds) throws Exception {
        String reqId = MDC.get(CommonSessionKeys.REQ_ID_KEY);
        if (storageIds != null) {
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
                            cloudStorage.multipartUploadFile(file, key);
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

    }

    private File saveTempFile(InputStream inputStream, String filePath, String fileName) {
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
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public void multipartUploadFile(File file, String key) {

    }

    @Override
    public String fileAddress(String key) {
        throw new IllegalArgumentException("当前实现类不支持此方法");
    }

    @Override
    public String getFileHost() {
        throw new IllegalArgumentException("当前实现类不支持此方法");
    }

    @Override
    public String generatePreSignedUrl(String key, Date expireDate) {
        String url = null;
        if (cloudStorageList != null && cloudStorageList.size() > 0) {
            url = cloudStorageList.get(0).generatePreSignedUrl(key, expireDate);
        }
        return url;
    }

    public List<StorageFileBox> getAllFileHost() {
        List<StorageFileBox> allFileHost = new ArrayList<>();
        cloudStorageList.stream().forEach(cloudStorage -> {
            allFileHost.add(StorageFileBox.builder()
                    .fileHost(cloudStorage.getFileHost())
                    .storageId(cloudStorage.getId())
                    .build());
        });
        return allFileHost;
    }


    @Override
    public void shutdown() {
        cloudStorageList.parallelStream().forEach(CloudStorage::shutdown);
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        Map<String, CloudStorage> beansOfType = event.getApplicationContext().getBeansOfType(CloudStorage.class);
        cloudStorageList = beansOfType.values().stream()
                .filter(cloudStorage -> !Objects.equals(cloudStorage.getId(), this.getId()))
                .collect(Collectors.toList());
        if (cloudStorageList == null || cloudStorageList.size() == 0) {
            throw new IllegalArgumentException("未指定启用的云存储类型");
        } else {
            log.info("云存储已初始化:{}", FastJsonUtils.toJSONString(cloudStorageList));
        }
    }
}
