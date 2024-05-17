package com.m7.abs.admin.core.security;

import com.m7.abs.admin.domain.vo.sysUser.SysUserVO;
import com.m7.abs.admin.sys.service.ISysUserService;
import com.m7.abs.common.domain.entity.SysUserEntity;
import com.m7.abs.common.utils.FastJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author coder
 * @see org.springframework.security.cas.userdetails.GrantedAuthorityFromAssertionAttributesUserDetailsService
 */
@Slf4j
public class MyCasUserDetailsServiceImpl implements AuthenticationUserDetailsService<CasAssertionAuthenticationToken> {

    @Autowired
    private ISysUserService userService;

    @Override
    public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) throws UsernameNotFoundException {

        String username = token.getName();

        CasUserDetails casUserDetails = null;
        Map<String, Object> attributes = token.getAssertion().getPrincipal().getAttributes();

        try {
            String userDetails = (String) attributes.get("userDetails");
            if (userDetails != null) {
                casUserDetails = FastJsonUtils.toBean(userDetails, CasUserDetails.class);
            }
        } catch (Exception e) {
            log.error("format CasUserDetails from json str error", e);
        }

        SysUserVO user = userService.findByName(username);
        if (user == null) {
            if (casUserDetails == null || StringUtils.isEmpty(casUserDetails.getId())) {
                throw new UsernameNotFoundException("该用户不存在");
            } else {
                createNewUser(user, username, casUserDetails);
            }
        }
        // 用户权限列表，根据用户拥有的权限标识与如 @PreAuthorize("hasAuthority('sys:menu:view')") 标注的接口对比，决定是否可以调用接口
        Set<String> permissions = userService.findPermissions(username);
        permissions.add("sys:isLogin");
        Set<AuthorityInfo> authorities = permissions.stream().map(AuthorityInfo::new).collect(Collectors.toSet());

        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setName(user.getNickname());
        userInfo.setAuthorities(authorities);
        return userInfo;
    }

    /**
     * 创建新的账户
     *
     * @param casUserDetails
     */
    private void createNewUser(SysUserVO user, String username, CasUserDetails casUserDetails) {
        SysUserEntity record = new SysUserEntity();
        record.setCasId(casUserDetails.getId());
        record.setUsername(username);
        record.setNickname(casUserDetails.getName());
        record.setEmail(casUserDetails.getEmail());
        record.setMobile(casUserDetails.getMobile());
        record.setStatus(1);
        record.setCreateTime(new Date());
        String s = userService.saveSysUser(record);
        user.setId(s);
        user.setUsername(username);
        user.setNickname(casUserDetails.getName());
    }
}
