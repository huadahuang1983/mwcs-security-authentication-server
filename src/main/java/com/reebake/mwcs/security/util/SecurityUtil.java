package com.reebake.mwcs.security.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

public class SecurityUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    public static String extractAuthentication(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        if(!StringUtils.hasText(token)) {
            return null;
        }
        if (token.startsWith(BEARER_PREFIX)) {
            token = token.substring(BEARER_PREFIX.length());
        }
        return token;
    }
}
