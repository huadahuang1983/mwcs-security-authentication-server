package com.reebake.mwcs.security.service;

import com.reebake.mwcs.security.dto.AuthResponse;
import com.reebake.mwcs.security.model.User;

public interface AuthenticationService {

    /**
     * 根据用户创建 JwtToken 响应
     * @param user 用户
     * @return JwtToken 响应
     */
    AuthResponse generateAuthResponse(User user);

    /**
     * 根据refreshToken生成JwtToken 响应
     * @param refreshToken 刷新 Token
     * @return JwtToken 响应
     */
    AuthResponse refreshToken(String refreshToken);

}