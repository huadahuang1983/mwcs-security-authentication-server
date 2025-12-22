package com.reebake.mwcs.security.sms;

/**
 * 短信验证码存储接口
 * 用于临时存储短信验证码信息
 */
public interface SmsCodeStore {
    
    /**
     * 保存短信验证码
     * @param phoneNumber 手机号
     * @param smsCode 验证码
     * @param expireSeconds 过期时间（秒）
     */
    void saveSmsCode(String phoneNumber, String smsCode, int expireSeconds);

    /**
     * 验证短信验证码
     * @param phoneNumber 手机号
     * @param smsCode 验证码
     * @return 是否验证通过
     */
    boolean validateSmsCode(String phoneNumber, String smsCode);

    /**
     * 检查手机号是否在发送间隔内
     * @param phoneNumber 手机号
     * @return 是否在发送间隔内
     */
    boolean isInSendInterval(String phoneNumber);
}