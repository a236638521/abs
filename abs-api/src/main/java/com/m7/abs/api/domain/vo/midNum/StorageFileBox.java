package com.m7.abs.api.domain.vo.midNum;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StorageFileBox {
    private String storageId;
    private String fileHost;
}
