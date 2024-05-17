package com.m7.abs.support.domain.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PreSignedUrlBO {
    /**
     * ObjectKey
     */
    private String key;
    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireDate;

    /**
     * 指定保存对象存储ID
     */
    List<String> storageIds;
}
