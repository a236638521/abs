package com.m7.abs.reportexport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.m7.abs.common.domain.dto.MiddleNumberCdrReport;
import com.m7.abs.common.domain.dto.NsqMsgDTO;
import com.m7.abs.common.domain.entity.MiddleNumberCdrReportEntity;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Kejie Peng
 * @since 2023-04-03
 */
public interface IMiddleNumberCdrReportService extends IService<MiddleNumberCdrReportEntity> {

    /**
     * 批量保存话单报表
     *
     * @param accountId
     * @param dateTimeMap
     */
    void batchSaveCdrReport(String accountId, Map<String, MiddleNumberCdrReportEntity> dateTimeMap);

    /**
     * 接收小号话单数据
     *
     * @param msg
     */
    void handleMidNumCdrMsg(NsqMsgDTO<MiddleNumberCdrReport> msg);

    /**
     * 统计当前账户的数据,并入库
     *
     * @param accountId
     * @param date
     */
    void sumCurrentMidNumCdrReport(String traceId, String accountId, Date date);

    /**
     * 清除缓存
     */
    void clearReportRedissonCache();

}
