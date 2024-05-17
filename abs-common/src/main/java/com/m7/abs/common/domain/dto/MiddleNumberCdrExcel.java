package com.m7.abs.common.domain.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.m7.abs.common.easyexcel.CustomBooleanConverter;
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
@ContentRowHeight(18)
@HeadRowHeight(20)
@ColumnWidth(20)
public class MiddleNumberCdrExcel {
    @ExcelProperty(value = {"账户编号"}, index = 0)
    private String accountId;

    @ExcelProperty(value = {"通话记录编号"}, index = 1)
    private String id;

    /**
     * 绑定记录ID
     */
    @ExcelProperty(value = {"绑定记录编号"}, index = 2)
    private String mappingId;

    /**
     * 主叫号码
     */
    @ExcelProperty(value = {"主叫"}, index = 3)
    private String caller;
    /**
     * 中间号
     */
    @ExcelProperty(value = {"中间号"}, index = 4)
    private String telX;
    /**
     * 被叫号码
     */
    @ExcelProperty(value = {"被叫"}, index = 5)
    private String callee;
    /**
     * 通话结果
     */
    @ExcelProperty(value = {"通话结果"}, index = 6)
    private String result;
    /**
     * 是否录音
     */
    @ExcelProperty(value = {"是否录音"}, index = 7, converter = CustomBooleanConverter.class)
    private boolean callRecording;
    /**
     * 通话时长
     */
    @ExcelProperty(value = {"通话时长"}, index = 8)
    private long billDuration;
    /**
     * 呼叫开始时间 格式:yyyy-MM-dd HH:mm:ss
     */
    @ExcelProperty(value = {"呼叫时间"}, index = 9)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;
    /**
     * 振铃时间
     */
    @ExcelProperty(value = {"振铃时间"}, index = 10)
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
    @ExcelProperty(value = {"挂断时间"}, index = 12)
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
