package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.m7.abs.common.domain.base.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 项目表
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
@TableName("project")
public class ProjectEntity extends BaseEntity<ProjectEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 代码;用于标记项目,和程序中的code一致
     */
    private String code;

    /**
     * 描述
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
