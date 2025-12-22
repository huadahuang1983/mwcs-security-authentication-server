package com.reebake.mwcs.security.controller;

import com.reebake.mwcs.security.captcha.CaptchaService;
import com.reebake.mwcs.security.captcha.CaptchaProperties;
import com.reebake.mwcs.security.dto.AuthResponse;
import com.reebake.mwcs.security.dto.SmsCodeRequest;
import com.reebake.mwcs.security.dto.SmsCodeResponse;
import com.reebake.mwcs.security.sms.SmsProperties;
import com.reebake.mwcs.security.sms.SmsService;
import com.reebake.mwcs.security.service.AuthenticationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final CaptchaService captchaService;
    private final CaptchaProperties captchaProperties;
    private final SmsService smsService;
    private final SmsProperties smsProperties;

    public AuthenticationController(AuthenticationService authenticationService, CaptchaService captchaService, 
                                   CaptchaProperties captchaProperties, SmsService smsService, SmsProperties smsProperties) {
        this.authenticationService = authenticationService;
        this.captchaService = captchaService;
        this.captchaProperties = captchaProperties;
        this.smsService = smsService;
        this.smsProperties = smsProperties;
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestHeader("Refresh-Token") String refreshToken) {
        AuthResponse authResponse = authenticationService.refreshToken(refreshToken);
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/captcha")
    public ResponseEntity<byte[]> getCaptcha() throws IOException {
        if (!captchaProperties.isEnabled()) {
            return ResponseEntity.status(403).build();
        }
        
        var captchaInfo = captchaService.generateCaptcha();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.set("Captcha-ID", captchaInfo.captchaId());
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(captchaInfo.captchaImage());
    }

    @PostMapping("/send-sms-code")
    public ResponseEntity<SmsCodeResponse> sendSmsCode(@RequestBody SmsCodeRequest smsCodeRequest) {
        if (!smsProperties.isEnabled()) {
            return ResponseEntity.status(403)
                    .body(SmsCodeResponse.failure("sms captcha is not enabled"));
        }

        if(smsService.isInSendInterval(smsCodeRequest.getPhoneNumber())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(SmsCodeResponse.failure("too many request."));
        }
        
        boolean success = smsService.sendSmsCode(smsCodeRequest.getPhoneNumber());
        if (success) {
            return ResponseEntity.ok(SmsCodeResponse.success("sms code sent successfully"));
        } else {
            return ResponseEntity.badRequest()
                    .body(SmsCodeResponse.failure("failed to send sms code, please try again later"));
        }
    }
}