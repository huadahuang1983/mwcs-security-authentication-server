package com.reebake.mwc.security.sms.config;

import com.reebake.mwc.security.sms.SmsCodeStore;
import com.reebake.mwc.security.sms.SmsProperties;
import com.reebake.mwc.security.sms.SmsService;
import com.reebake.mwc.security.sms.impl.InMemorySmsCodeStore;
import com.reebake.mwc.security.sms.impl.DefaultSmsServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 短信验证码配置类
 * 用于注册短信验证码相关的组件
 */
@Configuration
@EnableConfigurationProperties(SmsProperties.class)
public class SmsConfig {
    /**
     * 短信验证码存储组件
     * 默认使用内存存储，可根据需要替换为其他实现
     */
    @Bean
    @ConditionalOnMissingBean
    public SmsCodeStore smsCodeStore(SmsProperties smsProperties) {
        return new InMemorySmsCodeStore(smsProperties.getSendIntervalSeconds());
    }
    
    /**
     * 短信服务组件
     * 默认使用模拟实现，可根据需要替换为其他实现
     */
    @Bean
    @ConditionalOnMissingBean
    public SmsService smsService(SmsProperties smsProperties, SmsCodeStore smsCodeStore) {
        return new DefaultSmsServiceImpl(smsProperties, smsCodeStore);
    }
}