package com.m7.abs.common.domain.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.m7.abs.common.domain.base.CsvExportable;
import com.m7.abs.common.utils.DateUtil;
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
public class MiddleNumberCdrCsv implements CsvExportable {
    /**
     * 账户编号
     */
    private String accountId;

    /**
     * 通话记录编号
     */
    private String id;

    /**
     * 绑定记录编号
     */
    private String mappingId;

    /**
     * 主叫
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
     * 呼叫开始时间 格式:yyyy-MM-dd HH:mm:ss
     */
    private Date beginTime;
    /**
     * 振铃时间
     */
    private Date alertingTime;
    /**
     * 接听时间
     */
    private Date connectTime;
    /**
     * 挂断时间
     */
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
    private String recordFilePath;

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

    public String isCallRecording() {
        if (this.callRecording) {
            return "是";
        } else {
            return "否";
        }
    }

    @Override
    public String[] outputCsvTitleLine() {
        String[] title = {"账户编号", "通话记录编号", "绑定记录编号", "主叫", "中间号", "被叫", "通话结果", "是否录音", "通话时长", "呼叫时间", "振铃时间", "接听时间", "挂断时间", "录音地址"};
        return title;
    }

    @Override
    public String[] outputCsvDataLine() {
        //如果预计字符串可能包含双引号，则需要调用CSVFormat方法处理
        String[] dataLine = {this.accountId,
                this.id,
                this.mappingId,
                CsvExportable.CSVFormat(this.caller),
                CsvExportable.CSVFormat(this.telX),
                CsvExportable.CSVFormat(this.callee),
                this.result,
                this.isCallRecording(),
                String.valueOf(this.billDuration),
                (this.beginTime == null ? "" : DateUtil.parseDateToStr(this.beginTime, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS)),
                (this.alertingTime == null ? "" : DateUtil.parseDateToStr(this.alertingTime, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS)),
                (this.connectTime == null ? "" : DateUtil.parseDateToStr(this.connectTime, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS)),
                (this.releaseTime == null ? "" : DateUtil.parseDateToStr(this.releaseTime, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS)),
                this.getRecordFilePath()
        };
        return dataLine;
    }
}
