package com.reebake.mwcs.security.config;

import com.reebake.mwcs.security.captcha.config.CaptchaConfig;
import com.reebake.mwcs.security.controller.AuthenticationController;
import com.reebake.mwcs.security.jwt.config.JwtConfig;
import com.reebake.mwcs.security.sms.config.SmsConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AuthenticationManagerConfig.class, AuthServerCryptoConfig.class, AuthServerSecurityConfig.class, AuthServerFilterConfig.class,
        AuthServerProviderConfig.class, AuthenticationController.class, JwtConfig.class, CaptchaConfig.class, SmsConfig.class})
public class AuthServerAutoConfiguration {
}
