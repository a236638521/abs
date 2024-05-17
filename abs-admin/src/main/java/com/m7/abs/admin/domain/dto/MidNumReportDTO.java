package com.m7.abs.admin.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;
import java.util.List;

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
public class MidNumReportDTO {
     /**
      * ID

      */
     private String id;

     /**
      * 账户ID
      */
     private String accountId;

     private String dateTimeStr;

     /**
      * 呼叫次数
      */
     private Integer callCount;

     /**
      * 呼叫成功次数
      */
     private Integer callComplete;

     /**
      * 呼叫成功次数
      */
     private Integer callFail;

     /**
      * 通话时长，实际通话时长，（单位秒）
      */
     private Integer billDurationCount;

     /**
      * 计费通话时长，计费字段：60进1（单位：分钟）
      */
     private Integer rateDurationCount;

     /**
      * 被叫运营商-联通
      */
     private Integer calledCarrierUnicom;

     /**
      * 被叫运营商-电信
      */
     private Integer calledCarrierTelecom;

     /**
      * 被叫运营商-移动
      */
     private Integer calledCarrierMobile;

     /**
      * 被叫运营商-未知
      */
     private Integer calledCarrierUnknown;
}
