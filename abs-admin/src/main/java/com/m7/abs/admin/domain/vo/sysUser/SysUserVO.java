package com.m7.abs.admin.domain.vo.sysUser;


import com.m7.abs.common.domain.base.BaseEntity;
import com.m7.abs.common.domain.entity.SysUserRoleEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public class SysUserVO extends BaseEntity {

    private String username;

    private String nickname;

    private String password;

    private String salt;

    private String email;

    private String mobile;

    private Byte status;

    private String deptId;

    private String deptName;

    private Byte delFlag;

    private String roleNames;

    private List<SysUserRoleEntity> userRoles = new ArrayList<>();


}