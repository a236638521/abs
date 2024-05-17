package com.m7.abs.api.delayed.bean;

import lombok.*;

/**
 * @author Kejie Peng
 * @date 2023年 04月14日 10:30:52
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UpdateMidNumCdrOssIdBean {
    private String ossTaskId;
    private String channelRecordId;
}
