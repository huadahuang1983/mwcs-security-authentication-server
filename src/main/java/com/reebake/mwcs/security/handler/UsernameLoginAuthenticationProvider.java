package com.reebake.mwcs.security.handler;

import com.reebake.mwcs.security.captcha.CaptchaService;
import com.reebake.mwcs.security.captcha.CaptchaProperties;
import com.reebake.mwcs.security.dto.LoginRequest;
import com.reebake.mwcs.security.model.User;
import com.reebake.mwcs.security.model.UsernameLoginAuthenticationToken;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Setter
@RequiredArgsConstructor
public class UsernameLoginAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final CaptchaService captchaService;
    private final CaptchaProperties captchaProperties;
    private UserDetailsChecker userDetailsChecker = new DefaultPreAuthenticationChecks();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LoginRequest loginRequest = (LoginRequest) authentication.getCredentials();
        
        // 如果启用了验证码功能，验证验证码
        if (captchaProperties.isEnabled()) {
            if (!captchaService.validateCaptcha(loginRequest.getCaptchaId(), loginRequest.getCaptchaCode())) {
                throw new BadCredentialsException("invalid captcha code");
            }
        }
        
        String username = loginRequest.getUsername();
        User user = (User) userDetailsService.loadUserByUsername(username);

        String rawPassword = loginRequest.getPassword();
        if(!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("password not matches");
        }
        userDetailsChecker.check(user);

        return UsernameLoginAuthenticationToken.authenticated(user, loginRequest, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernameLoginAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
