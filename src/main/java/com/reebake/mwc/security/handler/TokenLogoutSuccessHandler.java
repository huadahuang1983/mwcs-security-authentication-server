package com.reebake.mwc.security.handler;

import com.reebake.mwc.security.util.SecurityUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.util.StringUtils;

import java.io.IOException;

@RequiredArgsConstructor
public class TokenLogoutSuccessHandler implements LogoutSuccessHandler {
    private final TokenBlacklist tokenBlacklist;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = SecurityUtil.extractAuthentication(request);
        if(StringUtils.hasText(token)) {
            tokenBlacklist.addToken(token);
        }
    }

}
