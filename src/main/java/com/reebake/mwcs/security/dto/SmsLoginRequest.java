package com.reebake.mwcs.security.dto;

import lombok.Data;

@Data
public class SmsLoginRequest {
    private String phoneNumber;
    private String smsCode;
}