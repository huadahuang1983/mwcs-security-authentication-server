package com.reebake.mwcs.security.handler;

public interface TokenBlacklist {

    void addToken(String token);

    boolean isTokenBlacklisted(String token);
}