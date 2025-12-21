package com.reebake.mwc.security.config;

import com.reebake.mwc.security.captcha.CaptchaService;
import com.reebake.mwc.security.captcha.CaptchaProperties;
import com.reebake.mwc.security.handler.SmsLoginAuthenticationProvider;
import com.reebake.mwc.security.handler.UsernameLoginAuthenticationProvider;
import com.reebake.mwc.security.sms.SmsProperties;
import com.reebake.mwc.security.sms.SmsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthServerProviderConfig {

    @Bean
    @ConditionalOnMissingBean
    public UsernameLoginAuthenticationProvider usernameLoginAuthenticationProvider(
            UserDetailsService userDetailsService, 
            PasswordEncoder passwordEncoder, 
            CaptchaService captchaService,
            CaptchaProperties captchaProperties) {
        return new UsernameLoginAuthenticationProvider(userDetailsService, passwordEncoder, captchaService, captchaProperties);
    }
    
    @Bean
    @ConditionalOnMissingBean
    public SmsLoginAuthenticationProvider smsLoginAuthenticationProvider(
            UserDetailsService userDetailsService, 
            SmsService smsService,
            SmsProperties smsProperties) {
        return new SmsLoginAuthenticationProvider(userDetailsService, smsService, smsProperties);
    }

}
