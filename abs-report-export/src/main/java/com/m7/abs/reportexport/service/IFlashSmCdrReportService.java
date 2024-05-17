package com.m7.abs.reportexport.service;

import com.m7.abs.common.domain.dto.FlashSmReportDto;
import com.m7.abs.common.domain.dto.NsqMsgDTO;
import com.m7.abs.common.domain.entity.FlashSmCdrReportEntity;

import java.util.Date;
import java.util.Map;

/**
 * 话单报表
 *
 * @author Kejie Peng
 * @date 2023年 03月20日 13:46:34
 */
public interface IFlashSmCdrReportService {
    /**
     * 接收小号话单数据
     *
     * @param msg
     */
    void handleFlashSmCdrMsg(NsqMsgDTO<FlashSmReportDto> msg);

    /**
     * 统计当前账户的数据,并入库
     *
     * @param traceId
     * @param accountId
     * @param date
     */
    void sumCurrentFlashSmCdrReport(String traceId, String accountId, Date date);

    /**
     * 批量保存话单报表
     *
     * @param accountId
     * @param dateTimeMap
     */
    void batchSaveCdrReport(String accountId, Map<String, FlashSmCdrReportEntity> dateTimeMap);

    /**
     * 清除缓存
     */
    void clearReportRedissonCache();

}
