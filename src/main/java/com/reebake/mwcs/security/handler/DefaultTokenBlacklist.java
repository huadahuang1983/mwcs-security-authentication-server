package com.reebake.mwcs.security.handler;

import com.reebake.mwcs.security.jwt.JwtProperties;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultTokenBlacklist implements TokenBlacklist {

    private final Map<String, Instant> blacklist = new ConcurrentHashMap<>();
    private final long tokenExpirationTimeInMinutes;

    public DefaultTokenBlacklist(JwtProperties properties) {
        this.tokenExpirationTimeInMinutes = properties.getExpirationTimeInMinutes();
    }

    public void addToken(String token) {
        blacklist.put(token, Instant.now().plus(tokenExpirationTimeInMinutes, java.time.temporal.ChronoUnit.MINUTES));
    }

    public boolean isTokenBlacklisted(String token) {
        Instant expirationTime = blacklist.get(token);
        if (expirationTime == null) {
            return false;
        }
        if (Instant.now().isAfter(expirationTime)) {
            blacklist.remove(token);
            return false;
        }
        return true;
    }
}