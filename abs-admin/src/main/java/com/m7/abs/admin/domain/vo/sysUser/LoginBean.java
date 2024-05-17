package com.m7.abs.admin.domain.vo.sysUser;

import lombok.*;

/**
 * 登录接口封装对象
 *
 * @author Louis
 * @date Oct 29, 2018
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginBean {

    private String account;
    private String password;
    private String captcha;
}
