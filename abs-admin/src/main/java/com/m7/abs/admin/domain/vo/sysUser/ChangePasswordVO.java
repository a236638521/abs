package com.m7.abs.admin.domain.vo.sysUser;

import lombok.*;

/**
 * @author KejiePeng
 * @Title:
 * @Description
 * @date 2021/5/13 14:36
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChangePasswordVO {
    private String oldPassword = "";
    private String newPassword = "";
}
