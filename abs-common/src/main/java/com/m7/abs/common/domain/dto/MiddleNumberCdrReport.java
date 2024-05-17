package com.m7.abs.common.domain.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.m7.abs.common.constant.common.MidNumCallStatusEnum;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-21
 */
@Getter
@Setter
public class MiddleNumberCdrReport {
    private String accountId;

    private String id;

    /**
     * 绑定记录ID
     */

    private String mappingId;

    /**
     * 主叫号码
     */
    private String caller;
    /**
     * 中间号
     */
    private String telX;
    /**
     * 被叫号码
     */
    private String callee;
    /**
     * 通话结果
     */
    private String result;
    /**
     * 是否录音
     */
    private boolean callRecording;
    /**
     * 通话时长
     */
    private long billDuration;
    /**
     * 计费时长
     */
    private long rateDuration;
    /**
     * 呼叫开始时间 格式:yyyy-MM-dd HH:mm:ss
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;
    /**
     * 振铃时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date alertingTime;
    /**
     * 接听时间
     */
    @ExcelProperty(value = {"接听时间"}, index = 11)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date connectTime;
    /**
     * 挂断时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date releaseTime;

    /**
     * 录音文件代理地址
     */
    @ExcelIgnore
    private String recordFileProxy;
    /**
     * 录音文件host
     */
    @ExcelIgnore
    private String recordFileHost;
    /**
     * 录音文件路径
     */
    @ExcelProperty(value = {"录音地址"}, index = 13)
    private String recordFilePath;

    /**
     * 主叫运营商
     */
    private String callerCarrier;
    /**
     * 被叫运营商
     */
    private String calledCarrier;

    public String getRecordFilePath() {
        if (StringUtils.isNotEmpty(recordFilePath)) {
            if (StringUtils.isNotEmpty(recordFileProxy)) {
                return recordFileProxy + recordFilePath;
            } else {
                return recordFileHost + recordFilePath;
            }
        } else {
            return null;
        }
    }

}
