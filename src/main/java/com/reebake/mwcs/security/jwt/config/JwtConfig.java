package com.reebake.mwcs.security.jwt.config;

import com.reebake.mwcs.security.jwt.JwtProperties;
import com.reebake.mwcs.security.jwt.JwtTokenGenerator;
import com.reebake.mwcs.security.jwt.JwtTokenValidator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableConfigurationProperties({JwtProperties.class})
public class JwtConfig {

    @Bean
    public JwtTokenGenerator jwtTokenGenerator(JwtProperties jwtProperties, RSAPrivateKey privateKey) {
        return new JwtTokenGenerator(jwtProperties, privateKey);
    }

    @Bean
    public JwtTokenValidator jwtTokenValidator(JwtProperties jwtProperties, RSAPublicKey publicKey) {
        return new JwtTokenValidator(jwtProperties, publicKey);
    }


}
