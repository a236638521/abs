package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.m7.abs.common.domain.base.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

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
@TableName("sys_menu")
public class SysMenuEntity extends BaseEntity<SysMenuEntity> {

    private static final long serialVersionUID = 1L;


    /**
     * 菜单名称
     */
    @TableField("name")
    private String name;

    /**
     * 父菜单ID，一级菜单为0
     */

    @TableField("parent_id")
    private String parentId;

    /**
     * 菜单URL,类型：1.普通页面（如用户管理， /sys/user） 2.嵌套完整外部页面，以http(s)开头的链接 3.嵌套服务器页面，使用iframe:前缀+目标URL(如SQL监控， iframe:/druid/login.html, iframe:前缀会替换成服务器地址)
     */
    @TableField("url")
    private String url;

    /**
     * 授权(多个用逗号分隔，如：sys:user:add,sys:user:edit)
     */
    @TableField("perms")
    private String perms;

    /**
     * 类型   0：目录   1：菜单   2：按钮
     */
    @TableField("type")
    private Integer type;

    /**
     * 菜单图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 排序
     */
    @TableField("order_num")
    private Integer orderNum;

    /**
     * 附属菜单
     */
    @TableField("attached")
    private Boolean attached;


    /**
     * 是否删除  -99：已删除  0：正常
     */
    @TableField("status")
    private Integer status;

    /**
     * 项目ID
     */
    @TableField("project_id")
    private String projectId;

    /**
     * 非数据库字段
     */
    @TableField(exist = false)
    private String parentName;
    /**
     * 非数据库字段
     */
    @TableField(exist = false)
    private Integer level;
    /**
     * 非数据库字段
     */
    @TableField(exist = false)
    private List<SysMenuEntity> children;
    /**
     * 非数据库字段  附属菜单
     */
    @TableField(exist = false)
    private List<SysMenuEntity> attachedMenu;

    @TableField(exist = false)
    private Boolean autoAddBtu;

    @TableField(exist = false)
    private String permsPrefix;
}