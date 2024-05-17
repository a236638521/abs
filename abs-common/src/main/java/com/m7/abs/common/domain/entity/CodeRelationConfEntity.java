package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.m7.abs.common.domain.base.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 代码对应关系配置表
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
@TableName("code_relation_conf")
public class CodeRelationConfEntity extends BaseEntity<CodeRelationConfEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * 项目功能id
     */
    private String projectFunId;

    /**
     * 功能接口配置ID
     */
    private String interfaceId;

    /**
     * 功能代码ID
     */
    private String funCodeId;

    /**
     * 接口代码ID
     */
    private String interfaceCodeId;

    /**
     * 状态;0:初始 1:启用 2:禁用
     */
    private Integer status;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
