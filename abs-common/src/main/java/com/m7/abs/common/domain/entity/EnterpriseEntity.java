package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.m7.abs.common.domain.base.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author Kejie Peng
 * @since 2022-02-28
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("enterprise")
public class EnterpriseEntity extends BaseEntity<EnterpriseEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 企业名称
     */
    private String name;

    /**
     * 企业编号
     */
    private String enterpriseCode;

    /**
     * 描述/备注
     */
    private String description;

    /**
     * 状态;0:初始 1:启用 2:禁用
     */
    private Integer status;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
