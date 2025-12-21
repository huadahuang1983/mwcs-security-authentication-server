package com.reebake.mwc.security.sms.impl;

import cn.hutool.core.lang.Validator;
import com.reebake.mwc.security.sms.SmsCodeStore;
import com.reebake.mwc.security.sms.SmsProperties;
import com.reebake.mwc.security.sms.SmsService;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * 默认短信验证码服务实现
 * 实际项目中应替换为真实的短信服务提供商API调用
 */
@Slf4j
public class DefaultSmsServiceImpl implements SmsService {
    
    private final SmsProperties smsProperties;
    private final SmsCodeStore smsCodeStore;
    private final Random random;
    
    public DefaultSmsServiceImpl(SmsProperties smsProperties, SmsCodeStore smsCodeStore) {
        this.smsProperties = smsProperties;
        this.smsCodeStore = smsCodeStore;
        this.random = new Random();
    }
    
    @Override
    public boolean sendSmsCode(String phoneNumber) {
        if(!Validator.isMobile(phoneNumber)) {
            return false;
        }
        // 检查是否在发送间隔内
        if (smsCodeStore.isInSendInterval(phoneNumber)) {
            log.warn("手机号 {} 发送短信验证码过于频繁", phoneNumber);
            return false;
        }
        
        // 生成随机验证码
        String smsCode = generateSmsCode(smsProperties.getLength());
        
        // 模拟发送短信
        String message = smsProperties.getTemplate()
                .replace("{code}", smsCode)
                .replace("{expire}", java.lang.String.valueOf(smsProperties.getExpireSeconds() / 60));
        
        log.info("向手机号 {} 发送短信: {}", phoneNumber, message);
        
        // 存储验证码
        smsCodeStore.saveSmsCode(phoneNumber, smsCode, smsProperties.getExpireSeconds());
        
        return true;
    }
    
    @Override
    public boolean validateSmsCode(String phoneNumber, String smsCode) {
        return smsCodeStore.validateSmsCode(phoneNumber, smsCode);
    }

    @Override
    public boolean isInSendInterval(String phoneNumber) {
        return smsCodeStore.isInSendInterval(phoneNumber);
    }

    /**
     * 生成指定长度的随机数字验证码
     */
    public String generateSmsCode(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}