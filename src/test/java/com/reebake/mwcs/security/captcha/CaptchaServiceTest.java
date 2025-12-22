package com.reebake.mwcs.security.captcha;

import com.reebake.mwcs.security.captcha.impl.CaptchaServiceImpl;
import com.reebake.mwcs.security.captcha.impl.InMemoryCaptchaStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 验证码服务测试类
 */
class CaptchaServiceTest {
    
    private CaptchaService captchaService;
    private CaptchaStore captchaStore;
    private CaptchaProperties captchaProperties;
    
    @BeforeEach
    void setUp() {
        captchaStore = new InMemoryCaptchaStore();
        captchaProperties = new CaptchaProperties();
        captchaService = new CaptchaServiceImpl(
                captchaStore,
                captchaProperties.getWidth(),
                captchaProperties.getHeight(),
                captchaProperties.getLength(),
                captchaProperties.getExpireSeconds()
        );
    }
    
    @Test
    void testGenerateCaptcha() throws IOException {
        // 生成验证码
        CaptchaService.CaptchaInfo captchaInfo = captchaService.generateCaptcha();
        
        // 验证生成的验证码信息
        assertNotNull(captchaInfo);
        assertNotNull(captchaInfo.captchaId());
        assertNotNull(captchaInfo.captchaText());
        assertEquals(captchaProperties.getLength(), captchaInfo.captchaText().length());
        assertNotNull(captchaInfo.captchaImage());
        assertTrue(captchaInfo.captchaImage().length > 0);
        
        // 验证验证码已存储
        String storedCaptcha = captchaStore.getCaptcha(captchaInfo.captchaId());
        assertNotNull(storedCaptcha);
        assertEquals(captchaInfo.captchaText(), storedCaptcha);
    }
    
    @Test
    void testValidateCaptcha() throws IOException {
        // 生成验证码
        CaptchaService.CaptchaInfo captchaInfo = captchaService.generateCaptcha();
        
        // 测试正确的验证码
        boolean isValid = captchaService.validateCaptcha(
                captchaInfo.captchaId(), 
                captchaInfo.captchaText()
        );
        assertTrue(isValid);
        
        // 测试验证码已被删除
        String storedCaptcha = captchaStore.getCaptcha(captchaInfo.captchaId());
        assertNull(storedCaptcha);
        
        // 测试错误的验证码
        CaptchaService.CaptchaInfo anotherCaptcha = captchaService.generateCaptcha();
        boolean isInvalid = captchaService.validateCaptcha(
                anotherCaptcha.captchaId(), 
                "wrong_captcha"
        );
        assertFalse(isInvalid);
        
        // 测试空参数
        assertFalse(captchaService.validateCaptcha(null, "test"));
        assertFalse(captchaService.validateCaptcha("test", null));
        assertFalse(captchaService.validateCaptcha(null, null));
    }
    
    @Test
    void testValidateExpiredCaptcha() throws IOException, InterruptedException {
        // 创建一个过期时间很短的验证码服务
        CaptchaService shortLivedCaptchaService = new CaptchaServiceImpl(
                captchaStore,
                captchaProperties.getWidth(),
                captchaProperties.getHeight(),
                captchaProperties.getLength(),
                1 // 1秒过期
        );
        
        // 生成验证码
        CaptchaService.CaptchaInfo captchaInfo = shortLivedCaptchaService.generateCaptcha();
        
        // 等待验证码过期
        Thread.sleep(1500);
        
        // 测试过期的验证码
        boolean isValid = shortLivedCaptchaService.validateCaptcha(
                captchaInfo.captchaId(), 
                captchaInfo.captchaText()
        );
        assertFalse(isValid);
    }
}
