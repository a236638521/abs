package com.m7.abs.admin.domain.dto;

import lombok.*;

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
public class FlashSmReportDTO {
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
     private Integer totalCount;

     /**
      * 呼叫成功次数
      */
     private Integer complete;

     /**
      * 呼叫成功次数
      */
     private Integer fail;

     /**
      * 运营商-联通
      */
     private Integer carrierUnicom;

     /**
      * 运营商-电信
      */
     private Integer carrierTelecom;

     /**
      * 运营商-移动
      */
     private Integer carrierMobile;

     /**
      * 运营商-未知
      */
     private Integer carrierUnknown;

}
