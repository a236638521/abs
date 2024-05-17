package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.m7.abs.common.constant.common.ExportStatusEnum;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Kejie Peng
 * @since 2023-03-24
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@ToString
@TableName("export_task")
public class ExportTaskEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID主键
     */
    @TableId("id")
    private String id;

    /**
     * 账户ID
     */
    private String accountId;

    /**
     * 项目代码
     */
    private String projectCode;

    /**
     * url
     */
    private String downloadUrl;

    /**
     * 状态：1、待执行；2、执行中；3、已暂停；4、执行成功；5、执行失败；
     */
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "last_update_time", fill = FieldFill.UPDATE)
    private Date lastUpdateTime;

    /**
     * 逻辑删除;0:未删除 1:已删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 备注
     */
    private String remarks;
    /**
     * 预计下载量
     */
    private Integer expectCount;
}
