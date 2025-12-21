package com.reebake.mwc.security.model;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.HashSet;

/**
 * 短信验证码登录认证令牌
 */
@Getter
public class SmsLoginAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;
    private Object credentials;

    public SmsLoginAuthenticationToken(Object principal, Object credentials) {
        super(new HashSet<>());
        this.principal = principal;
        this.credentials = credentials;
        this.setAuthenticated(false);
    }

    public SmsLoginAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    public static SmsLoginAuthenticationToken unauthenticated(Object principal, Object credentials) {
        return new SmsLoginAuthenticationToken(principal, credentials);
    }

    public static SmsLoginAuthenticationToken authenticated(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        return new SmsLoginAuthenticationToken(principal, credentials, authorities);
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated, "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}