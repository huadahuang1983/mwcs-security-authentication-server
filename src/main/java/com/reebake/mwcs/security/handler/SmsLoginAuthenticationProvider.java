package com.reebake.mwcs.security.handler;

import com.reebake.mwcs.security.dto.SmsLoginRequest;
import com.reebake.mwcs.security.model.SmsLoginAuthenticationToken;
import com.reebake.mwcs.security.model.User;
import com.reebake.mwcs.security.sms.SmsProperties;
import com.reebake.mwcs.security.sms.SmsService;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 短信验证码登录认证提供者
 */
@Setter
@RequiredArgsConstructor
public class SmsLoginAuthenticationProvider implements AuthenticationProvider {
    
    private final UserDetailsService userDetailsService;
    private final SmsService smsService;
    private final SmsProperties smsProperties;
    private UserDetailsChecker userDetailsChecker = new DefaultPreAuthenticationChecks();
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 如果短信验证码功能未启用，抛出异常
        if (!smsProperties.isEnabled()) {
            throw new BadCredentialsException("sms login is not enabled");
        }
        
        SmsLoginRequest smsLoginRequest = (SmsLoginRequest) authentication.getCredentials();
        String phoneNumber = smsLoginRequest.getPhoneNumber();
        String smsCode = smsLoginRequest.getSmsCode();
        
        // 验证短信验证码
        if (!smsService.validateSmsCode(phoneNumber, smsCode)) {
            throw new BadCredentialsException("invalid sms code");
        }
        
        // 通过手机号加载用户（假设UserDetailsService支持手机号登录）
        User user = (User) userDetailsService.loadUserByUsername(phoneNumber);
        
        // 检查用户状态
        userDetailsChecker.check(user);
        
        // 创建已认证的令牌
        return SmsLoginAuthenticationToken.authenticated(user, smsLoginRequest, user.getAuthorities());
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return SmsLoginAuthenticationToken.class.isAssignableFrom(authentication);
    }
}