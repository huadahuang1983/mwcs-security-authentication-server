package com.reebake.mwcs.security.filter;

import lombok.Setter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Setter
public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public LoginAuthenticationFilter(RequestMatcher requestMatcher, AuthenticationConverter authenticationConverter, AuthenticationManager authenticationManager) {
        super(requestMatcher, authenticationManager);
        setAuthenticationConverter(authenticationConverter);
    }

}