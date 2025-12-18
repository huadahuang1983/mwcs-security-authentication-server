package com.reebake.mwc.security.controller;

import com.reebake.mwc.security.dto.AuthResponse;
import com.reebake.mwc.security.service.AuthenticationService;
import com.reebake.mwc.security.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request) {
        String refreshToken = SecurityUtil.extractAuthentication(request);
        AuthResponse authResponse = authenticationService.refreshToken(refreshToken);
        return ResponseEntity.ok(authResponse);
    }

}