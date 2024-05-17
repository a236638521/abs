package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 账户通道关系表
 * </p>
 *
 * @author Kejie Peng
 * @since 2022-06-16
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("account_flash_sm_conf")
public class AccountFlashSmConfEntity {

    private static final long serialVersionUID = 1L;
    @TableId("id")
    private String id;
    /**
     * 账户ID
     */
    private String accountId;

    /**
     * 闪信模板ID
     */
    private String flashSmTemplateId;

    /**
     * 闪信发送账号ID
     */
    private String flashSmAccountId;

    /**
     * 创建人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 状态 1:启用 2:禁用
     */
    private Integer status;

    /**
     * 逻辑删除;0:未删除 1:已删除
     */
    private Integer deleted;

}
