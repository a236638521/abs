package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.m7.abs.common.domain.base.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

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
@TableName("sys_dict")
public class SysDictEntity extends BaseEntity<SysDictEntity> {

    private static final long serialVersionUID = 1L;


    /**
     * 数据值
     */
    @TableField("value")
    private String value;

    /**
     * 标签名
     */
    @TableField("label")
    private String label;

    /**
     * 类型
     */
    @TableField("type")
    private String type;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 排序（升序）
     */
    @TableField("sort")
    private BigDecimal sort;


    /**
     * 备注信息
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 是否删除  -99：已删除  0：正常
     */
    @TableField("status")
    private Integer status;


}
