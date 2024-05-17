package com.m7.abs.admin.core.config;


import com.cas.client.spring.security.config.SecurityAntPatternAdaptor;
import com.cas.client.spring.security.properties.CasClientProperties;
import com.m7.abs.admin.core.security.MyCasUserDetailsServiceImpl;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConditionalOnProperty(value = "abs.auth.method",havingValue = "cas")
public class SecurityCustomerConfig {
    @Bean
    public SecurityAntPatternAdaptor securityAntPatternAdaptor(
            @Qualifier("requestMappingHandlerMapping")
                    RequestMappingInfoHandlerMapping requestMappingInfoHandlerMapping) {
        return () -> {
            List<String> antPatterns = new ArrayList<>(128);
            requestMappingInfoHandlerMapping.getHandlerMethods().forEach((requestMappingInfo, handlerMethod) -> {
                if (handlerMethod.getMethod().isAnnotationPresent(PreAuthorize.class)) {
                    antPatterns.addAll(
                            requestMappingInfo.getPatternsCondition().getPatterns());
                }
            });
            return antPatterns.toArray(new String[0]);
        };
    }

    @Bean
    public CasAuthenticationProvider casAuthenticationProvider(
            TicketValidator ticketValidator, CasClientProperties properties, UserDetailsService userDetailsService) {
        CasAuthenticationProvider provider = new CasAuthenticationProvider();
        provider.setServiceProperties(properties.getServiceProperties());
        provider.setTicketValidator(ticketValidator);
        provider.setUserDetailsService(userDetailsService);
        provider.setKey(CasAuthenticationProvider.class.getSimpleName());
        provider.setAuthenticationUserDetailsService(customUserDetailsService());
        return provider;
    }

    /**
     * 用户自定义的AuthenticationUserDetailsService
     */
    @Bean
    public AuthenticationUserDetailsService<CasAssertionAuthenticationToken> customUserDetailsService() {
        return new MyCasUserDetailsServiceImpl();
    }
}
