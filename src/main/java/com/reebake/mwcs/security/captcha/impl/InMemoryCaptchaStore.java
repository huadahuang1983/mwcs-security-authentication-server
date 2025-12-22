package com.reebake.mwcs.security.captcha.impl;

import com.reebake.mwcs.security.captcha.CaptchaStore;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于内存的验证码存储实现
 * 使用ConcurrentHashMap存储验证码信息
 */
@Slf4j
public class InMemoryCaptchaStore implements CaptchaStore {
    
    /**
     * 验证码存储项
     */
    private static class CaptchaItem {
        String text;
        Instant expireTime;
        
        CaptchaItem(String text, Instant expireTime) {
            this.text = text;
            this.expireTime = expireTime;
        }
        
        boolean isExpired() {
            return Instant.now().isAfter(expireTime);
        }
    }
    
    private final Map<String, CaptchaItem> captchaMap = new ConcurrentHashMap<>();
    
    /**
     * 保存验证码
     * @param captchaId 验证码ID
     * @param captchaText 验证码文本
     * @param expireSeconds 过期时间（秒）
     */
    @Override
    public void saveCaptcha(String captchaId, String captchaText, int expireSeconds) {
        Instant expireTime = Instant.now().plusSeconds(expireSeconds);
        captchaMap.put(captchaId, new CaptchaItem(captchaText, expireTime));
        log.trace("Saved captcha: {} with expire time: {}", captchaId, expireTime);
    }
    
    /**
     * 获取验证码
     * @param captchaId 验证码ID
     * @return 验证码文本，如果不存在或已过期则返回null
     */
    @Override
    public String getCaptcha(String captchaId) {
        CaptchaItem item = captchaMap.get(captchaId);
        if (item == null) {
            return null;
        }
        
        if (item.isExpired()) {
            // 如果验证码已过期，删除它
            captchaMap.remove(captchaId);
            log.trace("Captcha expired and removed: {}", captchaId);
            return null;
        }
        
        return item.text;
    }
    
    /**
     * 删除验证码
     * @param captchaId 验证码ID
     */
    @Override
    public void removeCaptcha(String captchaId) {
        captchaMap.remove(captchaId);
        log.trace("Removed captcha: {}", captchaId);
    }
}
