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
 * @since 2022-05-31
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flash_sm_template")
public class FlashSmTemplateEntity extends BaseEntity<FlashSmTemplateEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * 别名
     */
    private String name;

    /**
     * 使用的通道ID
     */
    private String channelId;

    /**
     * 所属账户
     */
    private String accountId;

    /**
     * 模板内容
     */
    private String content;

    /**
     * 类型  1:无参数模板  2:有参数模板
     */
    private Integer type;

    /**
     * 状态  1：启用 2:停用
     */
    private Integer status;


    /**
     * 模板编号 ST开头  数字编号10位
     */
    private String templateNumber;

    /**
     * 审核未通过原因
     */
    private String reason;

    /**
     * 参数  多个使用英文逗号分割
     */
    private String params;

    /**
     * 场景说明
     */
    private String sceneDescription;

    /**
     * 渠道分配模板ID
     */
    private String channelTemplateId;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
