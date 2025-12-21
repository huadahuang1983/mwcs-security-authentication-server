package com.reebake.mwc.security.captcha.config;

import com.reebake.mwc.security.captcha.CaptchaProperties;
import com.reebake.mwc.security.captcha.CaptchaService;
import com.reebake.mwc.security.captcha.CaptchaStore;
import com.reebake.mwc.security.captcha.impl.CaptchaServiceImpl;
import com.reebake.mwc.security.captcha.impl.InMemoryCaptchaStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码服务配置类
 * 注册验证码相关的Bean
 */
@Configuration
@EnableConfigurationProperties({CaptchaProperties.class})
public class CaptchaConfig {
    
    /**
     * 注册验证码存储服务Bean
     * @return 验证码存储服务实例
     */
    @Bean
    @ConditionalOnMissingBean
    public CaptchaStore captchaStore() {
        return new InMemoryCaptchaStore();
    }
    
    /**
     * 注册验证码生成服务Bean
     * @param captchaStore 验证码存储服务
     * @param captchaProperties 验证码配置属性
     * @return 验证码生成服务实例
     */
    @Bean
    @ConditionalOnMissingBean
    public CaptchaService captchaService(CaptchaStore captchaStore, CaptchaProperties captchaProperties) {
        return new CaptchaServiceImpl(
                captchaStore,
                captchaProperties.getWidth(),
                captchaProperties.getHeight(),
                captchaProperties.getLength(),
                captchaProperties.getExpireSeconds()
        );
    }
}
