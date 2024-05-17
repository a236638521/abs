package com.m7.abs.admin.core.config;

import com.cas.client.spring.security.config.CasSecurityAutoConfiguration;
import com.cas.client.spring.security.config.SecurityAntPatternAdaptor;
import com.cas.client.spring.security.constant.CasClientConstant;
import com.cas.client.spring.security.properties.CasClientProperties;
import com.cas.client.spring.security.properties.CorsProperties;
import com.cas.client.spring.security.rest.TokenAuthenticationFilter;
import com.google.common.collect.ImmutableList;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration(CasClientConstant.CAS_SECURITY_AUTO_CONFIGURATION_NAME)
@ConditionalOnProperty(value = "abs.auth.method", havingValue = "cas")
public class SecurityCasConfig extends CasSecurityAutoConfiguration {
    public SecurityCasConfig(
            AuthenticationEntryPoint authenticationEntryPoint,
            CasClientProperties casClientProperties,
            SingleSignOutFilter singleSignOutFilter,
            CasAuthenticationFilter casAuthenticationFilter,
            TokenAuthenticationFilter tokenAuthenticationFilter,
            SecurityAntPatternAdaptor securityAntPatternAdaptor) {
        super(authenticationEntryPoint,
                casClientProperties,
                singleSignOutFilter,
                casAuthenticationFilter,
                tokenAuthenticationFilter,
                securityAntPatternAdaptor);
    }

    @Override
    protected void extendConfigure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .headers().frameOptions().disable();
    }

    @Override
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsProperties corsProperties = casClientProperties.getCorsProperties();
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(ImmutableList.of("*"));
        configuration.setAllowedMethods(corsProperties.getAllowedMethods());
        configuration.setAllowCredentials(corsProperties.getAllowCredentials());
        configuration.setAllowedHeaders(corsProperties.getAllowedHeaders());
        configuration.setExposedHeaders(ImmutableList.of(HttpHeaders.LOCATION));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(corsProperties.getMappingPattern(), configuration);
        return source;
    }

}
