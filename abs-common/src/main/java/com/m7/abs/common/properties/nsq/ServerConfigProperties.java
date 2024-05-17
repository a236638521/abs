package com.m7.abs.common.properties.nsq;

import com.sproutsocial.nsq.Config;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhuhf
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServerConfigProperties extends Config {
    /**
     * maximum RDY count for a client (default 2500)
     */
    private Integer maxRdyCount;
    /**
     * maximum duration before a message will timeout (default 15m0s)
     */
    private Integer maxMsgTimeout;
    /**
     * max deflate compression level a client can negotiate (> values == > nsqd CPU usage) (default 6)
     */
    private Integer maxDeflateLevel;
}
