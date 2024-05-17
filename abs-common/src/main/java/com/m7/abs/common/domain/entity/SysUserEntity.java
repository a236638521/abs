package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.m7.abs.common.domain.base.BaseEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
@ToString
@EqualsAndHashCode(callSuper = false)
public class SysUserEntity extends BaseEntity<SysUserEntity> {

    private static final long serialVersionUID = 1L;

    @TableField("cas_id")
    private String casId;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 盐
     */
    @TableField("salt")
    private String salt;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 手机号
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 状态  0：禁用   1：正常 -99删除
     */
    @TableField("status")
    private Integer status;

    /**
     * 机构ID
     */

    @TableField("dept_id")
    private String deptId;

    /**
     * 非数据库字段
     */
    @TableField(exist = false)
    private List<SysUserRoleEntity> userRoles = new ArrayList<>();

}