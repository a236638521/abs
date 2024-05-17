package com.m7.abs.admin.core.config;


import com.cas.client.spring.security.config.CasAutoConfiguration;
import com.cas.client.spring.security.config.CasSecurityAutoConfiguration;
import com.google.common.collect.ImmutableList;
import com.m7.abs.admin.core.local_scurity.JwtAuthenticationFilter;
import com.m7.abs.admin.core.local_scurity.JwtAuthenticationProvider;
import com.m7.abs.admin.core.local_scurity.MyLoginOutSuccessHandler;
import com.m7.abs.admin.core.local_scurity.SecurityAuthenticationFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @功能描述: 当然，该配置的核心部分是 @EnableOAuth2Sso 注解，我们用它来启用单点登录。
 * <p>
 * 请注意，我们需要继承 WebSecurityConfigurerAdapter — 如果没有它，所有路径都将被保护
 * — 因此用户在尝试访问任何页面时将被重定向到登录页面。 在当前这个示例中，索引页面和登录页面可以在没有身份验证的情况下可以访问。
 * @param:
 * @return:
 * @auther: Kejie Peng
 * @date: 2018/12/14 14:57
 */


//注释掉SpringSecurity EnableOAuth2Sso  暂时不开发这块功能
//@EnableOAuth2Sso
@Configuration
@EnableAutoConfiguration(exclude = {CasAutoConfiguration.class, CasSecurityAutoConfiguration.class})
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ConditionalOnProperty(value = "abs.auth.method", havingValue = "local", matchIfMissing = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    @Qualifier("myUserDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 忽略URL
        web.ignoring().antMatchers("/static/**");
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 使用自定义身份验证组件
        auth.authenticationProvider(new JwtAuthenticationProvider(userDetailsService));
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 禁用 csrf, 由于使用的是JWT，我们这里不需要csrf
        http.cors().configurationSource(this.corsConfigurationSource())
                .and().csrf().disable()
                .authorizeRequests()
                //处理跨域请求中的Preflight请求
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                // 跨域预检请求
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 登录 错误页面 静态文件
                .antMatchers("/websocket").permitAll()
                .antMatchers("/ready").permitAll()
                .antMatchers("/getAuthMethod").permitAll()
                .antMatchers("/error").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/logout").permitAll()
                // 验证码
                .antMatchers("/captcha.jpg").permitAll()
                // 其他所有请求需要身份认证
                .anyRequest().authenticated();


        // 退出登录处理器
        http.logout().logoutUrl("/logout").logoutSuccessHandler(new MyLoginOutSuccessHandler()).invalidateHttpSession(true);
        // token验证过滤器
        http.addFilterBefore(new JwtAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);

    }

    protected CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(ImmutableList.of("*"));
        configuration.setAllowedMethods(ImmutableList.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(ImmutableList.of("*"));
        configuration.setExposedHeaders(ImmutableList.of(HttpHeaders.LOCATION));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new SecurityAuthenticationFailureHandler();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

}
