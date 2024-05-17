package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.m7.abs.common.domain.base.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 用户Token
 * </p>
 *
 * @author Kejie Peng
 * @since 2019-04-16
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString
@TableName("sys_user_token")
public class SysUserTokenEntity extends BaseEntity<SysUserTokenEntity> {

    private static final long serialVersionUID = 1L;


    @TableField("user_id")
    private String userId;

    /**
     * token
     */
    @TableField("token")
    private String token;

    /**
     * 过期时间
     */
    @TableField("expire_time")
    private Date expireTime;


}
