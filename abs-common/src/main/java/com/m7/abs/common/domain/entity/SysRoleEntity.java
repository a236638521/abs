package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.m7.abs.common.domain.base.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author KejiePeng
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString
@TableName("sys_role")
public class SysRoleEntity extends BaseEntity<SysRoleEntity> {

    private static final long serialVersionUID = 1L;


    /**
     * 角色名称
     */
    @TableField("name")
    private String name;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;


    /**
     * 是否删除  -99：已删除  0：正常
     */
    @TableField("status")
    private Integer status;



}
