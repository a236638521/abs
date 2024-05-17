package com.m7.abs.admin.domain.vo.common;
/**
 * @author PKJ
 * @date 2018/5/17 0017 上午 10:24
 * @content
 **/

import lombok.*;

/**
 * @author pkj
 * @create 2018-05-17 上午 10:24
 **/
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SysUserLoginVO {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 验证码
     */
    private String verifyCode;
    /**
     * 是否记住我:1:记住;其他:不记住
     */
    private Integer rememberMe;
    /**
     * 跳转返回路径
     */
    private String returnUrl;

}
