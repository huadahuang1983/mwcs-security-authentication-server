package com.reebake.mwcs.security.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reebake.mwcs.security.dto.LoginRequest;
import com.reebake.mwcs.security.dto.SmsLoginRequest;
import com.reebake.mwcs.security.model.SmsLoginAuthenticationToken;
import com.reebake.mwcs.security.model.UsernameLoginAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class LoginAuthenticationConverter implements AuthenticationConverter {
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public Authentication convert(HttpServletRequest request) {
        JsonNode root = objectMapper.readTree(request.getInputStream());
        
        // 根据请求参数判断是用户名密码登录还是短信验证码登录
        if (root.has("smsCode")) {
            // 短信验证码登录
            SmsLoginRequest smsLoginRequest = objectMapper.treeToValue(root, SmsLoginRequest.class);
            if (StringUtils.hasText(smsLoginRequest.getPhoneNumber()) && StringUtils.hasText(smsLoginRequest.getSmsCode())) {
                return new SmsLoginAuthenticationToken(smsLoginRequest.getPhoneNumber(), smsLoginRequest);
            }
        } else {
            // 用户名密码登录
            LoginRequest loginRequest = objectMapper.treeToValue(root, LoginRequest.class);
            if (StringUtils.hasText(loginRequest.getUsername()) && StringUtils.hasText(loginRequest.getPassword())) {
                return new UsernameLoginAuthenticationToken(loginRequest.getUsername(), loginRequest);
            }
        }
        
        return null;
    }

}
