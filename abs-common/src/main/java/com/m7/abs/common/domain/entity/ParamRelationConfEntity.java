package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.m7.abs.common.domain.base.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 参数对应关系配置表
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
@TableName("param_relation_conf")
public class ParamRelationConfEntity extends BaseEntity<ParamRelationConfEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * 功能接口配置ID
     */
    private String funInterfaceConfId;

    /**
     * 功能参数ID
     */
    private String funParamId;

    /**
     * 接口参数ID
     */
    private String interfaceParamId;

    /**
     * 接口参数类型;BODY;HEADER;QUERY;RESPONSE
     */
    private String interfaceParamType;

/**
     * 状态;0:初始 1:启用 2:禁用
     */
    private Integer status;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
