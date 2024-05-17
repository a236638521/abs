package com.m7.abs.admin.core.security;

import com.m7.abs.admin.core.local_scurity.JwtUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;

public class SecurityUtil {

    public static UserInfo getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserInfo) {
            return (UserInfo) principal;
        } else if (principal instanceof JwtUserDetails) {
            JwtUserDetails userDetails = (JwtUserDetails) principal;
            return UserInfo.builder()
                    .username(userDetails.getUsername())
                    .authorities((Set<AuthorityInfo>) userDetails.getAuthorities()).build();
        } else if (principal instanceof String) {
            return UserInfo.builder()
                    .username((String) principal)
                    .build();
        } else {
            return null;
        }

    }

    public static String getUsername() {
        UserInfo userInfo = getUserInfo();

        if (userInfo != null) {
            return userInfo.getUsername();
        }
        return "";
    }
}
