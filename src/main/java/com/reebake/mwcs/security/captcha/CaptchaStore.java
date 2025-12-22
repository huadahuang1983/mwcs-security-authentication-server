package com.reebake.mwcs.security.captcha;

/**
 * 验证码存储接口
 * 用于临时存储验证码信息
 */
public interface CaptchaStore {
    
    /**
     * 保存验证码
     * @param captchaId 验证码ID
     * @param captchaText 验证码文本
     * @param expireSeconds 过期时间（秒）
     */
    void saveCaptcha(String captchaId, String captchaText, int expireSeconds);
    
    /**
     * 获取验证码
     * @param captchaId 验证码ID
     * @return 验证码文本，如果不存在或已过期则返回null
     */
    String getCaptcha(String captchaId);
    
    /**
     * 删除验证码
     * @param captchaId 验证码ID
     */
    void removeCaptcha(String captchaId);
}
