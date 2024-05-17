package com.m7.abs.admin.domain.dto;

import com.m7.abs.common.domain.entity.MiddleNumberPoolEntity;
import lombok.*;

/**
 * @author KejiePeng
 * @Title:
 * @Description
 * @date 2021/12/27 11:01
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MiddleNumberPoolPageDTO extends MiddleNumberPoolEntity {

    /**
     * 账户ID
     */
    private String accountName;

    /**
     * 通道ID
     */
    private String channelName;

}
