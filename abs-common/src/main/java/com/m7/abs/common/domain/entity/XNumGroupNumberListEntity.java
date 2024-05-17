package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@TableName("x_num_group_number_list")
public class XNumGroupNumberListEntity{

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
     * 号码
     */
    private String number;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
