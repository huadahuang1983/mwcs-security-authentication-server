package com.reebake.mwc.security.dto;

import lombok.Data;

@Data
public class SmsCodeRequest {
    private String phoneNumber;
}