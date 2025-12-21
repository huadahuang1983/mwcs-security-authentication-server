package com.reebake.mwc.security.sms;

/**
 * 短信验证码发送服务接口
 */
public interface SmsService {
    
    /**
     * 发送短信验证码
     * @param phoneNumber 手机号
     * @return 是否发送成功
     */
    boolean sendSmsCode(String phoneNumber);

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