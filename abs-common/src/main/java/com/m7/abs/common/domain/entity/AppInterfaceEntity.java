package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 对接接口列表
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
@Accessors(chain = true)
@TableName("app_interface")
public class AppInterfaceEntity {

    private static final long serialVersionUID = 1L;
    private String id;
    /**
     * 名字
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * AK
     */
    private String accessKey;

    /**
     * SK
     */
    private String secretKey;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 逻辑删除:
     * 0:未删除
     * 1:已删除
     */
    private Integer deleted;

    /**
     * 企业ID
     */
    private String enterpriseId;


}
