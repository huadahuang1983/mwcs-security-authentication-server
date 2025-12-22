package com.reebake.mwcs.security.dto;

import lombok.Data;

@Data
public class SmsCodeResponse {
    private Integer serialNo;
    private Boolean success = true;
    private String sendId;
    private String result;

    public static SmsCodeResponse failure(String result) {
        SmsCodeResponse response = new SmsCodeResponse();
        response.setResult(result);
        response.setSuccess(false);

        return response;
    }

    public static SmsCodeResponse success(String result) {
        SmsCodeResponse response = new SmsCodeResponse();
        response.setResult(result);

        return response;
    }
}
