package com.reebake.mwcs.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reebake.mwcs.security.handler.*;
import com.reebake.mwcs.security.handler.*;
import com.reebake.mwcs.security.jwt.JwtProperties;
import com.reebake.mwcs.security.jwt.JwtTokenGenerator;
import com.reebake.mwcs.security.jwt.JwtTokenValidator;
import com.reebake.mwcs.security.service.AuthenticationService;
import com.reebake.mwcs.security.service.impl.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AuthServerSecurityConfig {
    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean
    public LoginAuthenticationConverter loginAuthenticationConverter(ObjectMapper objectMapper) {
        return new LoginAuthenticationConverter(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public TokenBlacklist tokenBlacklist() {
        return new DefaultTokenBlacklist(jwtProperties);
    }

    @Bean
    public TokenLogoutSuccessHandler tokenLogoutSuccessHandler(TokenBlacklist tokenBlacklist) {
        return new TokenLogoutSuccessHandler(tokenBlacklist);
    }

    @Bean
    public AuthenticationService authenticationService(JwtTokenGenerator tokenGenerator, JwtTokenValidator tokenValidator
        , TokenBlacklist tokenBlacklist, JwtProperties jwtProperties, UserDetailsService userDetailsService) {
        return new AuthenticationServiceImpl(tokenGenerator, tokenValidator, tokenBlacklist, jwtProperties, userDetailsService);
    }

    @Bean
    @ConditionalOnMissingBean
    public LoginAuthenticationSuccessHandler loginAuthenticationSuccessHandler(AuthenticationService authenticationService) {
        return new LoginAuthenticationSuccessHandler(authenticationService, objectMapper);
    }

}
