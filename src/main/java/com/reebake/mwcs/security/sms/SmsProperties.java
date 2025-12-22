package com.reebake.mwcs.security.sms;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 短信验证码配置属性
 */
@Data
@ConfigurationProperties(prefix = "mwcs.security.sms")
public class SmsProperties {
    
    /**
     * 是否启用短信验证码登录功能
     */
    private boolean enabled = true;
    
    /**
     * 短信验证码长度
     */
    private int length = 6;
    
    /**
     * 短信验证码过期时间（秒）
     */
    private int expireSeconds = 300;
    
    /**
     * 同一手机号发送间隔（秒）
     */
    private int sendIntervalSeconds = 60;
    
    /**
     * 短信验证码模板
     */
    private String template = "您的验证码是：{code}，有效时间{expire}分钟，请勿泄露。";
}