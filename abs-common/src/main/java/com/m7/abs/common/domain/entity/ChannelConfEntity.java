package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.m7.abs.common.domain.base.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 通道配置
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-14
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString
@TableName("channel_conf")
public class ChannelConfEntity extends BaseEntity<ChannelConfEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * 通道ID
     */
    private String channelId;

    /**
     * 配置名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 环境;dev：开发环境；prod：生产环境
     */
    private String env;

    /**
     * https;0:否;1:是
     */
    private Integer https;

    /**
     * url host
     */
    private String host;

    /**
     * url context path
     */
    private String contextPath;

    /**
     * url port
     */
    private String port;

/**
     * 状态;0:初始 1:启用 2:禁用(一个channel只能启用一个配置)
     */
    private Integer status;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
