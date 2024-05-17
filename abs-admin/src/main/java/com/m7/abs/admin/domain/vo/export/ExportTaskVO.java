package com.m7.abs.admin.domain.vo.export;

import lombok.*;

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
public class ExportTaskVO {
     private String accountId;
     private List<String> beginTime;
     private String remarks;
     /**
      * 导出类型
      * excel
      * csv
      */
     private String type;
}
