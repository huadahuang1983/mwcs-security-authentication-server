package com.reebake.mwc.security.config;

import com.reebake.mwc.security.filter.LoginAuthenticationFilter;
import com.reebake.mwc.security.handler.JwtLogoutSuccessHandler;
import com.reebake.mwc.security.handler.LoginAuthenticationConverter;
import com.reebake.mwc.security.handler.LoginAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class AuthServerFilterConfig {

    @Bean
    public LoginAuthenticationFilter loginAuthenticationFilter(LoginAuthenticationSuccessHandler loginAuthenticationSuccessHandler,
                                                               LoginAuthenticationConverter authenticationConverter, AuthenticationManager authenticationManager) {
        RequestMatcher requestMatcher = PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/api/auth/login");
        LoginAuthenticationFilter loginAuthenticationFilter = new LoginAuthenticationFilter(requestMatcher, authenticationConverter, authenticationManager);
        loginAuthenticationFilter.setAuthenticationSuccessHandler(loginAuthenticationSuccessHandler);

        return loginAuthenticationFilter;
    }

    @Bean
    @Order(10)
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http, LoginAuthenticationFilter loginAuthenticationFilter,
                                                   JwtLogoutSuccessHandler jwtLogoutSuccessHandler) throws Exception {
        log.info("config login authentication filter ...");
        http.securityMatcher("/api/auth/login", "/api/auth/logout", "/api/auth/refresh")
            .csrf(AbstractHttpConfigurer::disable).cors(Customizer.withDefaults())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/api/auth/refresh").permitAll()
                .anyRequest().authenticated()
            ).logout(logout -> logout.logoutUrl("/api/auth/logout").logoutSuccessHandler(jwtLogoutSuccessHandler))
            ;

        http.addFilterAt(loginAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}