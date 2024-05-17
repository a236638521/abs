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
@TableName("sys_dept")
public class SysDeptEntity extends BaseEntity<SysDeptEntity> {

    private static final long serialVersionUID = 1L;


    /**
     * 机构名称
     */
    @TableField("name")
    private String name;

    /**
     * 上级机构ID，一级机构为0
     */

    @TableField("parent_id")
    private String parentId;

    /**
     * 排序
     */
    @TableField("order_num")
    private Integer orderNum;


    /**
     * 是否删除  -99：已删除  0：正常
     */
    @TableField("status")
    private Integer status;


    // 非数据库字段
    @TableField(exist = false)
    private String parentName;
    // 非数据库字段
    @TableField(exist = false)
    private Integer level;
    // 非数据库字段
    @TableField(exist = false)
    private List<SysDeptEntity> children;


}
