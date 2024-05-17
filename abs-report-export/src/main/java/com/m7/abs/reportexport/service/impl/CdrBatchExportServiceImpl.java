package com.m7.abs.reportexport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.m7.abs.common.constant.common.ExportStatusEnum;
import com.m7.abs.common.constant.common.StorageType;
import com.m7.abs.common.constant.keys.CommonSessionKeys;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.dto.MiddleNumberCdrCsv;
import com.m7.abs.common.domain.dto.MiddleNumberCdrExcel;
import com.m7.abs.common.domain.entity.AccountEntity;
import com.m7.abs.common.domain.entity.ExportTaskEntity;
import com.m7.abs.common.domain.entity.MiddleNumberCdrEntity;
import com.m7.abs.common.domain.vo.reportexport.MidNumCdrExportRequestVO;
import com.m7.abs.common.domain.vo.reportexport.MidNumCdrExportResponseVO;
import com.m7.abs.common.utils.DateUtil;
import com.m7.abs.common.utils.FileUtil;
import com.m7.abs.common.utils.MyStringUtils;
import com.m7.abs.common.utils.ZipUtil;
import com.m7.abs.reportexport.common.properties.AbsReportProperties;
import com.m7.abs.reportexport.core.storage.MixCloudStorage;
import com.m7.abs.reportexport.domain.dto.ExportChannelDTO;
import com.m7.abs.reportexport.mapper.MiddleNumberCdrMapper;
import com.m7.abs.reportexport.mapstruct.MidNumCdrMapStructMapper;
import com.m7.abs.reportexport.service.IAccountService;
import com.m7.abs.reportexport.service.ICdrBatchExportService;
import com.m7.abs.reportexport.service.IExportService;
import com.m7.abs.reportexport.service.IExportTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * 话单批量导出
 *
 * @author Kejie Peng
 * @date 2023年 03月20日 13:47:01
 */
@Slf4j
@Service
public class CdrBatchExportServiceImpl implements ICdrBatchExportService {
    @Autowired
    private MiddleNumberCdrMapper middleNumberCdrMapper;
    @Autowired
    private AbsReportProperties absReportProperties;
    @Autowired
    private MidNumCdrMapStructMapper midNumCdrMapStructMapper;
    @Autowired
    private MixCloudStorage mixCloudStorage;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IExportTaskService exportTaskService;

    @Override
    public BaseResponse<MidNumCdrExportResponseVO> checkRequestVO(MidNumCdrExportRequestVO requestVO) {
        AccountEntity accountEntity = accountService.getById(requestVO.getAccountId());
        if (accountEntity == null) {
            return BaseResponse.fail("ACCOUNT NOT FOUND");
        }
        return BaseResponse.success(MidNumCdrExportResponseVO.builder()
                .billAccountId(accountEntity.getBillAccountId()).build());
    }

    @Override
    public BaseResponse<MidNumCdrExportResponseVO> getExpectInfo(MidNumCdrExportRequestVO exportReq) {
        String currentDate = DateUtil.parseDateToStr(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD);
        String zipFileName = "midNumCdr-" + System.currentTimeMillis();
        String ossKey = "MID_NUM_CDR_ZIP" + File.separator + currentDate + File.separator + exportReq.getTaskId() + File.separator + zipFileName + ".zip";
        return BaseResponse.success(MidNumCdrExportResponseVO.builder()
                .storageConf(mixCloudStorage.getAllFileHost())
                .zipFileName("midNumCdr-" + System.currentTimeMillis())
                .key(ossKey)
                .build());
    }

    @Override
    @Async("cdrExportExecutor")
    public void sumExpectCount(MidNumCdrExportRequestVO exportReq) {
        String startTime = exportReq.getStartTime();
        String endTime = exportReq.getEndTime();
        String taskId = exportReq.getTaskId();
        LambdaQueryWrapper<MiddleNumberCdrEntity> queryWrapper = getQueryWrapper(exportReq, DateUtil.parseStrToDate(startTime, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS), DateUtil.parseStrToDate(endTime, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));
        Integer expectCount = middleNumberCdrMapper.selectCount(queryWrapper);
        if (expectCount != null) {
            exportTaskService.updateExpectCount(taskId, expectCount);
        }
    }

    @Override
    @Async("cdrExportExecutor")
    public void batchExport(ExportChannelDTO channelDTO) {
        long start = System.currentTimeMillis();

        MidNumCdrExportRequestVO requestVO = channelDTO.getRequestVO();
        ExportChannelDTO.StorageInfo storage = channelDTO.getStorage();

        String taskId = requestVO.getTaskId();
        ExportTaskEntity exportTaskEntity = exportTaskService.getById(taskId);
        if (exportTaskEntity == null) {
            log.warn("unknown task,taskId:{}", taskId);
            exportTaskService.updateTaskStatus(taskId, ExportStatusEnum.FAIL);
            return;
        }

        exportTaskService.updateTaskStatus(taskId, ExportStatusEnum.EXECUTION);

        String type = requestVO.getType();

        if (StringUtils.isEmpty(requestVO.getTaskId())) {
            taskId = MyStringUtils.randomUUID();
        }
        String currentDate = DateUtil.parseDateToStr(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD);
        MDC.put(CommonSessionKeys.REQ_ID_KEY, taskId);

        IExportService exportService = null;
        switch (type) {
            case "excel":
                exportService = new ExcelExportServiceImpl(MiddleNumberCdrExcel.class);
                break;
            case "csv":
                exportService = new CsvExportServiceImpl<MiddleNumberCdrCsv>();
                break;
            default:
                throw new RuntimeException("Unknown export type");
        }

        channelDTO.setExportService(exportService);
        /**
         * 拆分跨度较长的时间,避免时间范围过长,导致查询缓慢
         */
        List<Date> dayList = this.getDayList(requestVO.getStartTime(), requestVO.getEndTime());
        if (dayList != null && dayList.size() >= 2) {
            String filePath = absReportProperties.getExportExcel().getTempFileBasePath() + File.separator + currentDate + File.separator + taskId + File.separator;
            FileUtil.createFileDirs(filePath);
            String fileName = "midNumCdr-" + channelDTO.getNewFileCount() + "-" + System.currentTimeMillis();

            storage.setFilePath(filePath);
            storage.setFileName(fileName);
            channelDTO.setStorage(storage);

            exportService.init(filePath, fileName);
            log.info("创建文件:{}", filePath + fileName);
            boolean flag=false;
            try {
                /**
                 * 执行导出任务
                 */
                for (int i = 0; i < dayList.size() - 1; i++) {
                    Date startDate = dayList.get(i);
                    Date endDate = dayList.get(i + 1);
                    this.doQueryAndWriteExcel(channelDTO, startDate, endDate);
                    flag=true;
                }
            } catch (Exception e) {
                log.error("导出文件失败", e);
            } finally {
                exportService.stop();
                if (flag){
                    long end = System.currentTimeMillis();
                    log.info("导出excel完毕,共计插入{}条数据,插入{}次,总计耗时{}ms", channelDTO.getRecordCount(), channelDTO.getCount(), end - start);
                    boolean b = zipExcelFiles(channelDTO);
                    if (b) {
                        exportTaskService.updateTaskStatus(taskId, ExportStatusEnum.COMPETE);
                    } else {
                        exportTaskService.updateTaskStatus(taskId, ExportStatusEnum.FAIL);
                    }
                }else{
                    exportTaskService.updateTaskStatus(taskId, ExportStatusEnum.FAIL);
                }
            }
        } else {
            log.info("时间范围获取失败,{}~{}", requestVO.getStartTime(), requestVO.getEndTime());
        }


    }


    /**
     * 压缩文件夹
     *
     * @param channelDTO
     */
    private boolean zipExcelFiles(ExportChannelDTO channelDTO) {
        MidNumCdrExportRequestVO requestVO = channelDTO.getRequestVO();
        ExportChannelDTO.StorageInfo storage = channelDTO.getStorage();
        String currentDate = DateUtil.parseDateToStr(new Date(), DateUtil.DATE_FORMAT_YYYYMMDD);
        /**
         * 压缩文件,并删除文件
         */
        ZipOutputStream zipOutputStream = null;
        boolean zipSuccess = false;
        String zipFilePath = absReportProperties.getExportExcel().getTempZipFileBasePath() + File.separator + currentDate + File.separator + requestVO.getTaskId() + File.separator;
        String zipFileUrl = zipFilePath + storage.getZipFileName() + ".zip";
        log.info("开始压缩文件:{}", zipFileUrl);
        try {
            FileUtil.createFileDirs(zipFilePath);
            zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFileUrl));
            ZipUtil.zip(zipOutputStream, new File(storage.getFilePath()), storage.getZipFileName());
            zipSuccess = true;
            FileUtil.deleteTempFiles(storage.getFilePath());
        } catch (Exception e) {
            log.error("压缩文件失败", e);
            return zipSuccess;
        } finally {
            try {
                if (zipOutputStream != null) {
                    zipOutputStream.close();
                }
            } catch (IOException e) {
                log.error("关闭流失败", e);
            }
        }

        log.info("成功压缩文件", zipFileUrl);
        if (zipSuccess) {
            return uploadIntoCloud(zipFileUrl, storage.getKey());
        }
        return zipSuccess;
    }

    /**
     * 上传文件至云端
     *
     * @param zipFileUrl
     * @param key
     */
    private boolean uploadIntoCloud(String zipFileUrl, String key) {
        List<String> storageIds = new ArrayList<>();
        storageIds.add(StorageType.YD_EOS_STORAGE);
        try {
            mixCloudStorage.multipartUploadFile(new File(zipFileUrl), key, storageIds);
            return true;
        } catch (Exception e) {
            log.error("上传压缩文件失败", e);
            return false;
        } finally {
            FileUtil.deleteTempFiles(zipFileUrl);
        }
    }


    /**
     * 查询数据,并写入文件
     *
     * @param channelDTO
     * @param startDate
     * @param endDate
     * @return
     */
    private void doQueryAndWriteExcel(ExportChannelDTO channelDTO, Date startDate, Date endDate) {
        MidNumCdrExportRequestVO requestVO = channelDTO.getRequestVO();
        Long eachFileRecordCount = channelDTO.getEachFileRecordCount();
        ExportChannelDTO.StorageInfo storage = channelDTO.getStorage();


        int current = 1;
        log.info("查询时间范围:{}~{}", DateUtil.parseDateToStr(startDate, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS), DateUtil.parseDateToStr(endDate, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));
        int insertSize;
        List<MiddleNumberCdrEntity> records = null;
        do {
            long start = System.currentTimeMillis();
            records = doQueryData(requestVO, current, startDate, endDate);
            long end1 = System.currentTimeMillis();
            log.info("查询耗时:{}ms", end1 - start);
            if (records != null) {
                insertSize = records.size();
                int recordSize = records.size();
                if (recordSize > 0) {
                    channelDTO.addRecordCount(recordSize);

                    if (recordSize <= eachFileRecordCount) {
                        eachFileRecordCount = eachFileRecordCount - recordSize;
                        this.writeIntoFile(channelDTO, records);
                        channelDTO.setEachFileRecordCount(eachFileRecordCount);
                    } else {
                        /**
                         * 如果超出单文件最大限制,则分文件存储
                         */
                        do {

                            int exportSize;
                            if (recordSize > eachFileRecordCount) {
                                /**
                                 * 当前剩每份文件剩余量不足
                                 * 1.消耗完所有余量
                                 * 2.导入数据
                                 * 3.分文件
                                 * 4.初始化每份文件上限
                                 * 5.继续导入剩余数据
                                 */
                                exportSize = Math.toIntExact(eachFileRecordCount);
                                List<MiddleNumberCdrEntity> middleNumberCdrEntities = records.subList(0, exportSize);
                                this.writeIntoFile(channelDTO, middleNumberCdrEntities);
                                records.removeAll(middleNumberCdrEntities);
                                eachFileRecordCount = absReportProperties.getExportExcel().getEachFileMaxSize();
                                channelDTO.setEachFileRecordCount(eachFileRecordCount);

                                //停止插入,重新定义文件名
                                channelDTO.getExportService().stop();
                                String fileName = "midNumCdr-" + channelDTO.addNewFileCount() + "-" + System.currentTimeMillis();
                                storage.setFileName(fileName);
                                channelDTO.getExportService().init(storage.getFilePath(), fileName);
                                log.info("创建文件:{}", storage.getFilePath() + fileName);
                            } else {
                                /**
                                 * 当前文件剩余量充足,导出数据,并重新计算余量
                                 */
                                eachFileRecordCount = eachFileRecordCount - recordSize;
                                this.writeIntoFile(channelDTO, records);
                                records.clear();
                                channelDTO.setEachFileRecordCount(eachFileRecordCount);
                            }

                            recordSize = records.size();
                        } while (recordSize > 0);
                    }


                } else {
                    log.info("未查询到信息.");
                }
            } else {
                insertSize = 0;
            }
            current++;
        } while (records != null && insertSize >= absReportProperties.getExportExcel().getMaxSize());

    }


    private void writeIntoFile(ExportChannelDTO channelDTO, List<MiddleNumberCdrEntity> records) {
        if (records == null || records.size() == 0) {
            return;
        }
        channelDTO.addCount();
        MidNumCdrExportRequestVO requestVO = channelDTO.getRequestVO();
        long end1 = System.currentTimeMillis();
        if ("excel".equals(requestVO.getType())) {
            List<MiddleNumberCdrExcel> cdrList = transformExcelData(requestVO.getBillAccountId(), records);
            long end2 = System.currentTimeMillis();
            channelDTO.getExportService().write(cdrList);
            long end3 = System.currentTimeMillis();
            log.info("写入excel:第{}次,插入{}条数据,整理数据耗时:{}ms,写入耗时:{}ms.", channelDTO.getCount(), records.size(), end2 - end1, end3 - end2);
        } else if ("csv".equals(requestVO.getType())) {
            List<MiddleNumberCdrCsv> cdrList = transformCsvData(requestVO.getBillAccountId(), records);
            long end2 = System.currentTimeMillis();
            channelDTO.getExportService().write(cdrList);
            long end3 = System.currentTimeMillis();
            log.info("写入csv:第{}次,插入{}条数据,整理数据耗时:{}ms,写入耗时:{}ms.", channelDTO.getCount(), records.size(), end2 - end1, end3 - end2);
        }
    }


    /**
     * 查询数据
     *
     * @param requestVO
     * @param currentPage
     * @param startDate
     * @param endDate
     * @return
     */
    private List<MiddleNumberCdrEntity> doQueryData(MidNumCdrExportRequestVO requestVO, Integer currentPage, Date startDate, Date endDate) {
        Page page = new Page();
        page.setCurrent(currentPage);
        page.setSize(absReportProperties.getExportExcel().getMaxSize());
        page.setSearchCount(false);
        LambdaQueryWrapper<MiddleNumberCdrEntity> queryWrapper = getQueryWrapper(requestVO, startDate, endDate);
        queryWrapper.orderByDesc(MiddleNumberCdrEntity::getBeginTime);
        queryWrapper.select(MiddleNumberCdrEntity::getAccountId, MiddleNumberCdrEntity::getId,
                MiddleNumberCdrEntity::getMappingId, MiddleNumberCdrEntity::getCreateTime,
                MiddleNumberCdrEntity::getCaller, MiddleNumberCdrEntity::getTelX,
                MiddleNumberCdrEntity::getCallee, MiddleNumberCdrEntity::getResult,
                MiddleNumberCdrEntity::isCallRecording, MiddleNumberCdrEntity::getBillDuration,
                MiddleNumberCdrEntity::getBeginTime, MiddleNumberCdrEntity::getAlertingTime,
                MiddleNumberCdrEntity::getConnectTime, MiddleNumberCdrEntity::getReleaseTime,
                MiddleNumberCdrEntity::getRecordFileHost, MiddleNumberCdrEntity::getRecordFileProxy,
                MiddleNumberCdrEntity::getRecordFilePath
        );
        IPage<MiddleNumberCdrEntity> result = middleNumberCdrMapper.selectPage(page, queryWrapper);
        if (result != null) {
            return result.getRecords();
        } else {
            return null;
        }
    }

    /**
     * 转换成对应的数据类型
     *
     * @param billAccountId
     * @param records
     * @return
     */
    private List<MiddleNumberCdrExcel> transformExcelData(String billAccountId, List<MiddleNumberCdrEntity> records) {
        List<MiddleNumberCdrExcel> cdrList = new ArrayList<>();
        records.stream().forEach(cdr -> {
            MiddleNumberCdrExcel middleNumberCdrExcel = midNumCdrMapStructMapper.convertCdrEntityToCdrExcel(cdr);
            /**
             * 这里默认使用billAccountId
             */
            middleNumberCdrExcel.setAccountId(billAccountId);
            cdrList.add(middleNumberCdrExcel);
        });
        return cdrList;
    }

    private List<MiddleNumberCdrCsv> transformCsvData(String billAccountId, List<MiddleNumberCdrEntity> records) {
        List<MiddleNumberCdrCsv> cdrList = new ArrayList<>();
        records.stream().forEach(cdr -> {
            MiddleNumberCdrCsv middleNumberCdrExcel = midNumCdrMapStructMapper.convertCdrEntityToCdrCsv(cdr);
            /**
             * 这里默认使用billAccountId
             */
            middleNumberCdrExcel.setAccountId(billAccountId);
            cdrList.add(middleNumberCdrExcel);
        });
        return cdrList;
    }


    private LambdaQueryWrapper<MiddleNumberCdrEntity> getQueryWrapper(MidNumCdrExportRequestVO requestVO, Date startDate, Date endDate) {
        LambdaQueryWrapper<MiddleNumberCdrEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(MiddleNumberCdrEntity::getBeginTime, startDate);
        wrapper.lt(MiddleNumberCdrEntity::getBeginTime, endDate);
        //       wrapper.eq(MiddleNumberCdrEntity::getAccountId, requestVO.getAccountId());
        wrapper.and(orWrapper -> {
            orWrapper.eq(MiddleNumberCdrEntity::getAccountId, requestVO.getBillAccountId());
            orWrapper.or();
            orWrapper.eq(MiddleNumberCdrEntity::getAccountId, requestVO.getAccountId());
        });
        return wrapper;
    }

    /**
     * 拆分时间
     * 时间大于1天,按天为间隔拆分时间
     * 时间大于12小时,按小时为间隔拆分时间
     * 否则直接返回两个时间
     *
     * @param startTime
     * @param endTime
     * @return
     */
    private List<Date> getDayList(String startTime, String endTime) {
        int field;
        Date startDate = DateUtil.parseStrToDate(startTime, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
        Date endDate = DateUtil.parseStrToDate(endTime, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
        if (DateUtil.getDistanceTimeBySec(startDate, endDate) > 86400L) {
            field = Calendar.DATE;
        } else if (DateUtil.getDistanceTimeBySec(startDate, endDate) > 43200L) {
            field = Calendar.HOUR;
        } else {
            ArrayList<Date> list = Lists.newArrayList();
            list.add(startDate);
            list.add(endDate);
            return list;
        }
        return DateUtil.dayLists(startDate, endDate, field);
    }


}
