package com.m7.abs.admin.core.local_scurity;


import com.m7.abs.admin.domain.vo.sysUser.SysUserVO;
import com.m7.abs.admin.sys.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户登录认证信息查询
 *
 * @author Louis
 * @date Nov 20, 2018
 */
@Service("myUserDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ISysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        SysUserVO user = sysUserService.findByName(userName);
        if (user == null) {
            throw new UsernameNotFoundException("该用户不存在");
        }
        // 用户权限列表，根据用户拥有的权限标识与如 @PreAuthorize("hasAuthority('sys:menu:view')") 标注的接口对比，决定是否可以调用接口
        Set<String> permissions = sysUserService.findPermissions(userName);
        permissions.add("sys:isLogin");
        Set<GrantedAuthority> grantedAuthorities = permissions.stream().map(GrantedAuthorityImpl::new).collect(Collectors.toSet());
        return new JwtUserDetails(user.getUsername(), user.getPassword(), user.getSalt(), grantedAuthorities);
    }
}