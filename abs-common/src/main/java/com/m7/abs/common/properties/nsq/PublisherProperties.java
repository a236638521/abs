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
public class PublisherProperties {
    private Boolean enable = true;
    private List<String> nsqdHost = new ArrayList<>(0);
}
