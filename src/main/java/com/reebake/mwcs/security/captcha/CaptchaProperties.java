package com.reebake.mwcs.security.captcha;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 验证码配置属性
 */
@Data
@ConfigurationProperties(prefix = "mwcs.security.captcha")
public class CaptchaProperties {
    
    /**
     * 是否启用验证码功能
     */
    private boolean enabled = true;
    
    /**
     * 验证码宽度
     */
    private int width = 120;
    
    /**
     * 验证码高度
     */
    private int height = 40;
    
    /**
     * 验证码字符长度
     */
    private int length = 4;
    
    /**
     * 验证码过期时间（秒）
     */
    private int expireSeconds = 300;
}
