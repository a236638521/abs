package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.m7.abs.common.domain.base.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 账户表
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
@TableName("account")
public class AccountEntity extends BaseEntity<AccountEntity> {

    private static final long serialVersionUID = 1L;
    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 企业ID
     */
    private String enterpriseId;
    /**
     * 账户ID
     */
    private String billAccountId;


    /**
     * 账户名称
     */
    private String name;

    /**
     * 状态;0:初始 1:启用 2:禁用
     */
    private Integer status;

    /**
     * 是否允许被叫振铃时发送闪信
     * 该功能目前单独为小号业务开放
     */
    private boolean flashSmEnable;

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
