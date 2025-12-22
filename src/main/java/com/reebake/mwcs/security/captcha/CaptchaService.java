package com.reebake.mwcs.security.captcha;

import java.io.IOException;

/**
 * 验证码服务接口
 * 用于生成和验证验证码
 */
public interface CaptchaService {
    
    /**
     * 生成验证码
     * @return 验证码信息，包含图像数据和验证码字符
     * @throws IOException 生成验证码时的IO异常
     */
    CaptchaInfo generateCaptcha() throws IOException;
    
    /**
     * 验证验证码
     * @param captchaId 验证码ID
     * @param userInput 用户输入的验证码
     * @return 验证结果，true表示验证通过，false表示验证失败
     */
    boolean validateCaptcha(String captchaId, String userInput);
    
    /**
     * 验证码信息类
     */
    record CaptchaInfo(String captchaId, String captchaText, byte[] captchaImage) {
    }
}
