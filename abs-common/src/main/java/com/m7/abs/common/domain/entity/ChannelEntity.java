package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.m7.abs.common.domain.base.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 通道表
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
@TableName("channel")
public class ChannelEntity extends BaseEntity<ChannelEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * 通道名称
     */
    private String name;

    /**
     * 通道代码
     */
    private String code;

    /**
     * 描述
     */
    private String description;

    /**
     * 项目ID
     */
    private String projectId;

/**
     * 状态;0:初始 1:启用 2:禁用
     */
    private Integer status;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
