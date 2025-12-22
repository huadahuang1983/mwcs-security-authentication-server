package com.reebake.mwcs.security.model;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.HashSet;

/**
 * 用户名登录认证令牌
 */
@Getter
public class UsernameLoginAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;
    private Object credentials;

    public UsernameLoginAuthenticationToken(Object principal, Object credentials) {
        super(new HashSet<>());
        this.principal = principal;
        this.credentials = credentials;
        this.setAuthenticated(false);
    }

    public UsernameLoginAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    public static UsernameLoginAuthenticationToken unauthenticated(Object principal, Object credentials) {
        return new UsernameLoginAuthenticationToken(principal, credentials);
    }

    public static UsernameLoginAuthenticationToken authenticated(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        return new UsernameLoginAuthenticationToken(principal, credentials, authorities);
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated, "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}
