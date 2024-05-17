package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.m7.abs.common.domain.base.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString
@TableName("sys_role_menu")
public class SysRoleMenuEntity extends BaseEntity<SysRoleMenuEntity> {

    private static final long serialVersionUID = 1L;


    /**
     * 角色ID
     */

    @TableField("role_id")
    private String roleId;

    /**
     * 菜单ID
     */
    @TableField("menu_id")
    private String menuId;


}