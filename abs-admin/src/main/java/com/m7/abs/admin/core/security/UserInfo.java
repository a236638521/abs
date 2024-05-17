package com.m7.abs.admin.core.security;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户信息
 *
 * @author ChengLi
 * @、这里我写了几个较为常用的字段，id，name，username，password，可以根据实际的情况自己增加
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserInfo implements UserDetails {
    private static final long serialVersionUID = -1041327031937199938L;

    /**
     * 用户ID
     */
    private String id;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 登录名称
     */
    private String username;

    /**
     * 登录密码
     */
    private String password;

    private boolean isAccountNonExpired = true;

    private boolean isAccountNonLocked = true;

    private boolean isCredentialsNonExpired = true;

    private boolean isEnabled = true;

    private Set<AuthorityInfo> authorities = new HashSet<AuthorityInfo>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }


}
