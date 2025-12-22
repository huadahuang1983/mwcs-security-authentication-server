package com.reebake.mwcs.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mwcs.jwt")
@Getter
@Setter
public class JwtProperties {

    private String issuer = "mwcs-security";
    private long expirationTimeInMinutes = 30;

}