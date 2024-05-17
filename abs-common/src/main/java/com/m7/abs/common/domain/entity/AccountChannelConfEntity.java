package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.m7.abs.common.domain.base.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 账户通道关系表
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
@TableName("account_channel_conf")
public class AccountChannelConfEntity extends BaseEntity<AccountChannelConfEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * 账户ID
     */
    private String accountId;

    /**
     * 通道ID
     */
    private String channelId;

    /**
     * 计费应用ID
     */
    private String billApplicationId;

    /**
     * AK
     */
    private String accessKey;

    /**
     * SK
     */
    private String secretKey;
    /**
     * APPID
     */
    private String appId;

    /**
     * 状态;0:初始 1:启用 2:禁用
     */
    private Integer status;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
