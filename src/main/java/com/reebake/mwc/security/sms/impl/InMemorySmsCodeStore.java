package com.reebake.mwc.security.sms.impl;

import com.reebake.mwc.security.sms.SmsCodeStore;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于内存的短信验证码存储实现
 * 使用ConcurrentHashMap存储短信验证码信息
 */
@Slf4j
public class InMemorySmsCodeStore implements SmsCodeStore {
    
    /**
     * 短信验证码存储项
     */
    private static class SmsCodeItem {
        String smsCode;
        Instant expireTime;
        Instant lastSendTime;
        
        SmsCodeItem(String smsCode, Instant expireTime, Instant lastSendTime) {
            this.smsCode = smsCode;
            this.expireTime = expireTime;
            this.lastSendTime = lastSendTime;
        }
        
        boolean isExpired() {
            return Instant.now().isAfter(expireTime);
        }
        
        Instant getLastSendTime() {
            return lastSendTime;
        }
    }
    
    private final Map<String, SmsCodeItem> smsCodeMap = new ConcurrentHashMap<>();
    private final int sendIntervalSeconds;
    
    public InMemorySmsCodeStore(int sendIntervalSeconds) {
        this.sendIntervalSeconds = sendIntervalSeconds;
    }
    
    /**
     * 保存短信验证码
     * @param phoneNumber 手机号
     * @param smsCode 验证码
     * @param expireSeconds 过期时间（秒）
     */
    @Override
    public void saveSmsCode(String phoneNumber, String smsCode, int expireSeconds) {
        Instant now = Instant.now();
        Instant expireTime = now.plusSeconds(expireSeconds);// 保存验证码
        smsCodeMap.put(phoneNumber, new SmsCodeItem(smsCode, expireTime, now));
        log.trace("Saved SMS code for phone: {} with expire time: {}", phoneNumber, expireTime);
    }
    
    /**
     * 验证短信验证码
     * @param phoneNumber 手机号
     * @param smsCode 用户输入的验证码
     * @return 是否验证通过
     */
    @Override
    public boolean validateSmsCode(String phoneNumber, String smsCode) {
        SmsCodeItem item = smsCodeMap.get(phoneNumber);
        if (item == null) {
            log.trace("SMS code not found for phone: {}", phoneNumber);
            return false;
        }
        
        if (item.isExpired()) {
            // 如果验证码已过期，删除它
            smsCodeMap.remove(phoneNumber);
            log.trace("SMS code expired and removed for phone: {}", phoneNumber);
            return false;
        }
        
        // 验证验证码是否匹配
        boolean isValid = item.smsCode.equals(smsCode);
        if (isValid) {
            // 验证成功后删除验证码
            smsCodeMap.remove(phoneNumber);
            log.trace("SMS code validated successfully and removed for phone: {}", phoneNumber);
        } else {
            log.trace("SMS code validation failed for phone: {}", phoneNumber);
        }
        
        return isValid;
    }
    
    /**
     * 检查手机号是否在发送间隔内
     * @param phoneNumber 手机号
     * @return 是否在发送间隔内
     */
    @Override
    public boolean isInSendInterval(String phoneNumber) {
        SmsCodeItem item = smsCodeMap.get(phoneNumber);
        if (item == null) {
            return false;
        }
        
        Instant now = Instant.now();
        Instant lastSendTime = item.getLastSendTime();
        Instant allowedSendTime = lastSendTime.plusSeconds(sendIntervalSeconds);
        
        return now.isBefore(allowedSendTime);
    }
}