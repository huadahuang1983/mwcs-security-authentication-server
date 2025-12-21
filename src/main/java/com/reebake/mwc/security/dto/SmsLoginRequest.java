package com.reebake.mwc.security.dto;

import lombok.Data;

@Data
public class SmsLoginRequest {
    private String phoneNumber;
    private String smsCode;
}