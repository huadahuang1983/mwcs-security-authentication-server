package com.reebake.mwc.security.config;

import com.reebake.mwc.security.captcha.config.CaptchaConfig;
import com.reebake.mwc.security.controller.AuthenticationController;
import com.reebake.mwc.security.jwt.config.JwtConfig;
import com.reebake.mwc.security.sms.config.SmsConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AuthenticationManagerConfig.class, AuthServerCryptoConfig.class, AuthServerSecurityConfig.class, AuthServerFilterConfig.class,
        AuthServerProviderConfig.class, AuthenticationController.class, JwtConfig.class, CaptchaConfig.class, SmsConfig.class})
public class AuthServerAutoConfiguration {
}
