package com.m7.abs.admin.domain.vo.sysUser;

import com.m7.abs.common.domain.entity.SysUserEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public class SysUserPageVO extends SysUserEntity {
    private String deptName;
    private String roleNames;

}
