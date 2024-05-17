package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.m7.abs.common.domain.base.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Kejie Peng
 * @since 2022-08-02
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("x_num_group")
public class XNumGroupEntity {

    private static final long serialVersionUID = 1L;
    @TableId("id")
    private String id;
    /**
     * 账户ID
     */
    private String accountId;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 状态;0:初始 1:启用 2:禁用
     */
    private Integer status;

    /**
     * 逻辑删除:
     * 0:未删除;1:已删除;
     */
    @TableLogic
    private int deleted;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateTime;

}
