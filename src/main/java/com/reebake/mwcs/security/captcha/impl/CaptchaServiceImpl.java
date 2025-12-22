package com.reebake.mwcs.security.captcha.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.reebake.mwcs.security.captcha.CaptchaService;
import com.reebake.mwcs.security.captcha.CaptchaStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * 验证码服务实现类
 * 使用Hutool库生成验证码
 */
@Slf4j
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {
    
    private final CaptchaStore captchaStore;
    private final int width;
    private final int height;
    private final int length;
    private final int expireSeconds;
    
    /**
     * 生成验证码
     * @return 验证码信息
     * @throws IOException 生成验证码时的IO异常
     */
    @Override
    public CaptchaInfo generateCaptcha() throws IOException {
        // 生成随机验证码ID
        String captchaId = UUID.randomUUID().toString();
        
        // 使用Hutool生成验证码
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(width, height, length, 20);
        String captchaText = captcha.getCode();
        
        // 将验证码图像转换为字节数组
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        captcha.write(outputStream);
        byte[] captchaImage = outputStream.toByteArray();
        outputStream.close();
        
        // 存储验证码信息
        captchaStore.saveCaptcha(captchaId, captchaText, expireSeconds);
        
        log.debug("Generated captcha with ID: {}, text: {}", captchaId, captchaText);
        return new CaptchaInfo(captchaId, captchaText, captchaImage);
    }
    
    /**
     * 验证验证码
     * @param captchaId 验证码ID
     * @param userInput 用户输入的验证码
     * @return 验证结果
     */
    @Override
    public boolean validateCaptcha(String captchaId, String userInput) {
        if (captchaId == null || userInput == null) {
            return false;
        }
        
        // 从存储中获取验证码
        String storedCaptcha = captchaStore.getCaptcha(captchaId);
        if (storedCaptcha == null) {
            log.debug("Captcha not found or expired: {}", captchaId);
            return false;
        }
        
        // 比较验证码（忽略大小写）
        boolean isValid = storedCaptcha.equalsIgnoreCase(userInput);
        
        // 无论验证是否成功，都删除验证码
        captchaStore.removeCaptcha(captchaId);
        
        log.debug("Validated captcha {}: input={}, stored={}, result={}", 
                captchaId, userInput, storedCaptcha, isValid);
        return isValid;
    }
}
