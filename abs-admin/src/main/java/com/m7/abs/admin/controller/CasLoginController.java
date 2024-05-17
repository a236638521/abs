package com.m7.abs.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/cas")
public class CasLoginController {
    String webUrl;

    @GetMapping("/login/page")
    public String securedIndex(ModelMap modelMap) {
        log.info("/secured called");
        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        if(auth.getPrincipal() instanceof UserDetails) {
            modelMap.put("username", ((UserDetails) auth.getPrincipal()).getUsername());
        }
        return "secure/index";
    }

    /**
     * 票根验证
     * 验证通过时转发到前端主页
     * @param response
     * @throws IOException
     */
    @GetMapping("/checkTicket")
    public void index(HttpServletResponse response) throws IOException {
        // 前端页面地址
        response.sendRedirect(webUrl);
    }

}
