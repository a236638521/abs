package com.m7.abs.common.properties.nsq;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuhf
 */
@Data
@NoArgsConstructor
public class SubscriberProperties {
    private Boolean enable;
    /**
     * lookupd 地址
     */
    private List<String> lookupdHosts = new ArrayList<>(0);

}
