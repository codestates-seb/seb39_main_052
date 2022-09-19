package com.seb39.myfridge.auth;

import com.seb39.myfridge.auth.filter.JwtAuthenticationFilter;
import com.seb39.myfridge.auth.filter.JwtAuthorizationFilter;
import com.seb39.myfridge.auth.filter.JwtExceptionHandlingFilter;
import com.seb39.myfridge.auth.handler.*;
import com.seb39.myfridge.auth.service.JwtService;
import com.seb39.myfridge.auth.service.OAuth2UserService;
import com.seb39.myfridge.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberService memberService;
    private final JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;
    private final AuthenticationExceptionEntryPoint authenticationExceptionEntryPoint;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2UserService oAuth2UserService;
    private final JwtService jwtService;
    private final JwtLogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers()
                .frameOptions()
                .disable();
        http.formLogin()
                .disable();
        http.httpBasic()
                .disable();
        http.cors().disable();

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
                .apply(new CustomJwtConfigurer())
                .and()
                .logout()
                .logoutUrl("/api/logout")
                .logoutSuccessUrl("/")
                .addLogoutHandler(logoutHandler)
                .and()
                .authorizeRequests().anyRequest().permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationExceptionEntryPoint);

        http.oauth2Login()
                .userInfoEndpoint().userService(oAuth2UserService)
                .and()
                .successHandler(oAuth2SuccessHandler)
                .failureHandler(new OAuth2FailureHandler());

        return http.build();
    }

    public class CustomJwtConfigurer extends AbstractHttpConfigurer<CustomJwtConfigurer, HttpSecurity> {

        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtService);
            jwtAuthenticationFilter.setFilterProcessesUrl("/api/login");
            jwtAuthenticationFilter.setAuthenticationFailureHandler(jwtAuthenticationFailureHandler);

            JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager, memberService, jwtService);
            JwtExceptionHandlingFilter jwtExceptionHandlingFilter = new JwtExceptionHandlingFilter(authenticationManager);
            builder
                    .addFilter(jwtAuthenticationFilter)
                    .addFilter(jwtAuthorizationFilter)
                    .addFilterBefore(jwtExceptionHandlingFilter, JwtAuthenticationFilter.class);
        }
    }
}
