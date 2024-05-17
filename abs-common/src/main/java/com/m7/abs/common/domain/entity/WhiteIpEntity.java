package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.m7.abs.common.domain.base.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author Kejie Peng
 * @since 2022-03-01
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("white_ip")
public class WhiteIpEntity extends BaseEntity<WhiteIpEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 描述
     */
    private String description;

    /**
     * 状态;0:初始 1:启用 2:禁用
     */
    private Integer status;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
