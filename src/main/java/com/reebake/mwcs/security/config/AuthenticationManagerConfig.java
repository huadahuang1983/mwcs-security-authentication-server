package com.reebake.mwcs.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;

import java.util.List;

/**
 * AuthenticationManager单独配置，避免循环依赖
 */
public class AuthenticationManagerConfig {

    @Autowired
    private List<AuthenticationProvider> authenticationProviders;
    @Autowired
    private AuthenticationEventPublisher authenticationEventPublisher;

    @Bean
    @ConditionalOnMissingBean(AuthenticationManager.class)
    AuthenticationManager authenticationManager() {
        ProviderManager authenticationManager = new ProviderManager(authenticationProviders);
        authenticationManager.setAuthenticationEventPublisher(authenticationEventPublisher);
        return authenticationManager;
    }

}
