package com.reebake.mwcs.security.config;

import cn.hutool.core.io.FileUtil;
import com.reebake.mwcs.security.util.RsaUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class AuthServerCryptoConfig {

    @Bean
    @ConditionalOnMissingBean
    public KeyPair rsaKeyPair() {
        String basePath = System.getProperty("user.home") + "/.mwcs/rsa", keyName="jwt";
        KeyPair keyPair;
        if(!FileUtil.exist(basePath)) {
            try {
                keyPair = RsaUtil.generateKeyPair();
            }catch (Exception e) {
                throw new RuntimeException(e);
            }

            RsaUtil.storeKeyPair(keyPair, basePath, keyName);
        }else {
            keyPair = RsaUtil.loadKeyPair(basePath, keyName);
        }
        return keyPair;
    }

    @Bean
    @ConditionalOnMissingBean
    public RSAPublicKey rsaPublicKey(KeyPair keyPair) {
        return (RSAPublicKey) keyPair.getPublic();
    }

    @Bean
    @ConditionalOnMissingBean
    public RSAPrivateKey rsaPrivateKey(KeyPair keyPair) {
        return (RSAPrivateKey) keyPair.getPrivate();
    }

}