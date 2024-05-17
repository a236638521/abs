package com.m7.abs.api.domain.bo;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SaveToOssReqBO {
    /**
     * 录音文件地址
     */
    private String recordFileUrl;
    /**
     * 账户ID
     */
    private String accountId;
    /**
     * 开始时间
     */
    private Date beginTime;
    /**
     * 记录ID
     */
    private String recorderId;

    /**
     * 指定保存对象存储ID
     */
    List<String> storageIds;
}
