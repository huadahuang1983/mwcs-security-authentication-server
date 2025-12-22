package com.reebake.mwcs.security.dto;

import lombok.Data;

@Data
public class SmsCodeRequest {
    private String phoneNumber;
}