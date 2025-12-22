package com.reebake.mwcs.security.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;

public class JwtTokenValidator {
    private final JWSVerifier verifier;
    private final String issuer;

    public JwtTokenValidator(JwtProperties jwtProperties, PublicKey publicKey) {
        this.issuer = jwtProperties.getIssuer();
        this.verifier = createVerifier(publicKey);
    }

    private JWSVerifier createVerifier(PublicKey publicKey) {
        return new RSASSAVerifier((RSAPublicKey) publicKey);
    }

    /**
     * 验证jwt token
     * @param token token
     * @return true 通过 false 失败
     */
    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            // Verify the signature
            boolean signatureValid = signedJWT.verify(verifier);
            if (!signatureValid) {
                return false;
            }

            // Check if token is expired
            if (signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date())) {
                return false;
            }

            // Check issuer
            if (!issuer.equals(signedJWT.getJWTClaimsSet().getIssuer())) {
                return false;
            }

            return true;
        } catch (ParseException | JOSEException e) {
            throw new JwtValidationException("Failed to validate JWT token", e);
        }
    }

    public String getSubject(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new JwtValidationException("Failed to parse JWT token", e);
        }
    }

    public <T> T getClaim(String token, String claimName, Class<T> claimType) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return claimType.cast(signedJWT.getJWTClaimsSet().getClaim(claimName));
        } catch (ParseException e) {
            throw new JwtValidationException("Failed to parse JWT token", e);
        }
    }

}