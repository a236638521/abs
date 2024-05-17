package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.m7.abs.common.domain.base.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 小号池
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-14
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString
@TableName("middle_number_pool")
public class MiddleNumberPoolEntity extends BaseEntity<MiddleNumberPoolEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * 中间号
     */
    private String number;

    /**
     * 账户ID
     */
    private String accountId;

    /**
     * 通道ID
     */
    private String channelId;

    /**
     * 中间号类型;AXB;AX;AXYB (多种模式用;号隔开)
     */
    private String type;

    /**
     * 区号
     */
    private String areaCode;

/**
     * 状态;0:初始 1:启用 2:禁用
     */
    private Integer status;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
