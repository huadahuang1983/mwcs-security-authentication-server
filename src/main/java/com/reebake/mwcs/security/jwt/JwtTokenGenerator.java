package com.reebake.mwcs.security.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.security.PrivateKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

public class JwtTokenGenerator {
    private final JWSSigner signer;
    private final JwtProperties jwtProperties;

    public JwtTokenGenerator(JwtProperties jwtProperties, PrivateKey privateKey) {
        this.jwtProperties = jwtProperties;
        this.signer = createSigner(privateKey);
    }

    private JWSSigner createSigner(PrivateKey privateKey) {
        return new RSASSASigner(privateKey);
    }

    /**
     * 生成jwt token
     * @param subject 主题
     * @param claims 声明
     * @return jwt token
     */
    public String generateToken(String subject, Map<String, Object> claims) {
        try {
            Instant now = Instant.now();
            Instant expiration = now.plus(jwtProperties.getExpirationTimeInMinutes(), ChronoUnit.MINUTES);

            JWTClaimsSet.Builder claimsSetBuilder = new JWTClaimsSet.Builder()
                    .subject(subject)
                    .issuer(jwtProperties.getIssuer())
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(expiration));

            claims.forEach(claimsSetBuilder::claim);

            JWTClaimsSet claimsSet = claimsSetBuilder.build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader.Builder(JWSAlgorithm.RS256).build(),
                    claimsSet
            );

            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new JwtGenerationException("Failed to generate JWT token", e);
        }
    }

}