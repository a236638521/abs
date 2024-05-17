package com.m7.abs.admin.domain.vo.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 创建导出任务VO
 *
 * @author Kejie Peng
 * @date 2023年 03月27日 13:57:36
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReportSearchVO {
    @NotEmpty
    private String accountId;
    /**
     * {value: 'year', label: '年报'},
     * {value: 'month', label: '月报'},
     * {value: 'week', label: '周报'},
     * {value: 'date', label: '日报'},
     * {value: 'custom', label: '自定义'}
     */
    @NotEmpty
    private String timeType;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timeValue;
    private String beginTime;
    private String endTime;
}
