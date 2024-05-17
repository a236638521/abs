package com.m7.abs.admin.sys.controller;

import com.m7.abs.admin.common.constant.SessionKeys;
import com.m7.abs.admin.core.local_scurity.JwtAuthenticatioToken;
import com.m7.abs.admin.core.local_scurity.SecurityUtils;
import com.m7.abs.admin.core.security.SecurityUtil;
import com.m7.abs.admin.core.security.UserInfo;
import com.m7.abs.admin.domain.vo.sysUser.LoginBean;
import com.m7.abs.admin.domain.vo.sysUser.SysUserVO;
import com.m7.abs.admin.sys.service.ISysUserService;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录控制器
 *
 * @author Louis
 * @date Oct 29, 2018
 */
@RestController
public class SysLoginController {
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${abs.auth.method}")
    private String authMethod;

    @GetMapping(value = "/getAuthMethod")
    public BaseResponse getAuthMethod() {
        HashMap<Object, Object> result = new HashMap<>();
        result.put("authMethod", authMethod);
        return BaseResponse.success(result);
    }

    /**
     * 登录接口
     * 改为CAS后,该方法将会弃用
     */
    @PostMapping(value = "/login")
    public BaseResponse login(@RequestBody LoginBean loginBean, HttpServletRequest request, HttpSession session) throws IOException {
        String username = loginBean.getAccount();
        String password = loginBean.getPassword();
        String captcha = loginBean.getCaptcha();

        // 用户信息
        SysUserVO user = sysUserService.findByName(username);

        // 账号不存在、密码错误
        if (user == null) {
            return BaseResponse.error("账号不存在");
        }

        if (!PasswordUtils.matches(user.getSalt(), password, user.getPassword())) {
            return BaseResponse.error("密码不正确");
        }

        // 账号锁定
        if (user.getStatus() == 0) {
            return BaseResponse.error("账号已被锁定,请联系管理员");
        }
        // 系统登录认证
        JwtAuthenticatioToken token = SecurityUtils.login(request, username, password, authenticationManager);
        Map<String, String> map = new HashMap<>();
        map.put("userId", user.getId().toString());
        map.put("username", username);
        map.put("token", token.getToken());
        map.put("roleNames", user.getRoleNames());
        session.setAttribute(SessionKeys.SESSION_SYSTEM_USER_ID, user.getId());
        return BaseResponse.success(map);
    }

    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    @PreAuthorize("hasAuthority('sys:isLogin')")
    @PostMapping(value = "/getUserInfo")
    public BaseResponse getUserInfo() {
        UserInfo userInfo = SecurityUtil.getUserInfo();
        if (userInfo != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("username", userInfo.getUsername());
            map.put("userId", userInfo.getId());
            return BaseResponse.success(map);
        }

        return BaseResponse.fail("Not login.");

    }


    @PostMapping(value = "/logout")
    public BaseResponse logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return BaseResponse.success("退出登录");
    }

}
