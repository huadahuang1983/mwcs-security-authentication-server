package com.reebake.mwcs.security.service.impl;

import com.reebake.mwcs.security.dto.AuthResponse;
import com.reebake.mwcs.security.model.User;
import com.reebake.mwcs.security.jwt.JwtProperties;
import com.reebake.mwcs.security.jwt.JwtTokenGenerator;
import com.reebake.mwcs.security.jwt.JwtTokenValidator;
import com.reebake.mwcs.security.handler.TokenBlacklist;
import com.reebake.mwcs.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtTokenGenerator tokenGenerator;
    private final JwtTokenValidator tokenValidator;
    private final TokenBlacklist tokenBlacklist;
    private final JwtProperties jwtProperties;
    private final UserDetailsService userDetailsService;

    @Override
    public AuthResponse generateAuthResponse(User user) {
        String accessToken = tokenGenerator.generateToken(user.getUsername(), getClaims(user));
        String refreshToken = tokenGenerator.generateToken(user.getUsername(), Map.of("type", "refresh"));

        return new AuthResponse(accessToken, refreshToken, jwtProperties.getExpirationTimeInMinutes() * 60);
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        if (tokenBlacklist.isTokenBlacklisted(refreshToken)) {
            throw new RuntimeException("Refresh token has been revoked");
        }

        if(!tokenValidator.validateToken(refreshToken)) {
            throw new BadCredentialsException("Token Invalid");
        }

        String tokenType = tokenValidator.getClaim(refreshToken, "type", String.class);
        if (!"refresh".equals(tokenType)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String username = tokenValidator.getSubject(refreshToken);
        User user = (User) userDetailsService.loadUserByUsername(username);

        String newAccessToken = tokenGenerator.generateToken(user.getUsername(), getClaims(user));
        String newRefreshToken = tokenGenerator.generateToken(user.getUsername(), Map.of("type", "refresh"));

        // Add old refresh token to blacklist
        tokenBlacklist.addToken(refreshToken);

        return new AuthResponse(newAccessToken, newRefreshToken, jwtProperties.getExpirationTimeInMinutes() * 60);
    }

    private Map<String, Object> getClaims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("username", user.getUsername());
        claims.put("authorities", user.getAuthorities());
        return claims;
    }
}