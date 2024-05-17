package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("x_num_group_channel_conf")
public class XNumGroupChannelConfEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID主键
     */
    private String id;

    /**
     * 分组ID
     */
    private String groupId;

    /**
     * 通道ID
     */
    private String channelId;

    /**
     * 关联group id;和其他通道关联的ID
     */
    private String associationId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 逻辑删除:
     * 0:未删除;1:已删除;
     */
    @TableLogic
    private int deleted;

}
