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
@TableName("account_push_url")
public class AccountPushUrlEntity extends BaseEntity<AccountPushUrlEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * 账户ID
     */
    private String accountId;

    /**
     * 推送地址
     */
    private String url;

    /**
     * 推送类型
     */
    private String type;

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
