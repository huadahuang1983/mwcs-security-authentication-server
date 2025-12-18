package com.reebake.mwc.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reebake.mwc.security.handler.*;
import com.reebake.mwc.security.jwt.JwtProperties;
import com.reebake.mwc.security.jwt.JwtTokenGenerator;
import com.reebake.mwc.security.jwt.JwtTokenValidator;
import com.reebake.mwc.security.service.AuthenticationService;
import com.reebake.mwc.security.service.impl.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@RequiredArgsConstructor
public class AuthServerSecurityConfig {
    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;
    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

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
    public JwtTokenGenerator jwtTokenGenerator() {
        return new JwtTokenGenerator(jwtProperties, privateKey);
    }

    @Bean
    @ConditionalOnMissingBean
    public TokenBlacklist tokenBlacklist() {
        return new DefaultTokenBlacklist(jwtProperties);
    }

    @Bean
    public JwtTokenValidator jwtTokenValidator() {
        return new JwtTokenValidator(jwtProperties, publicKey);
    }

    @Bean
    public JwtLogoutSuccessHandler jwtLogoutSuccessHandler(TokenBlacklist tokenBlacklist) {
        return new JwtLogoutSuccessHandler(tokenBlacklist);
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
