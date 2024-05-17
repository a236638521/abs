package com.m7.abs.reportexport.domain.dto;

import com.m7.abs.common.domain.entity.MiddleNumberCdrEntity;
import lombok.*;

/**
 * 小号话单导出DTO
 *
 * @author Kejie Peng
 * @date 2023年 03月22日 13:39:06
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MiddleNumberCdrExportDTO extends MiddleNumberCdrEntity {
    private String accountName;
}
