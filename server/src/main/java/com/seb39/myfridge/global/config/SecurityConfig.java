package com.seb39.myfridge.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seb39.myfridge.domain.auth.filter.GuestJwtAuthenticationFilter;
import com.seb39.myfridge.domain.auth.filter.JwtAuthenticationFilter;
import com.seb39.myfridge.domain.auth.filter.JwtAuthorizationFilter;
import com.seb39.myfridge.domain.auth.filter.JwtExceptionHandlingFilter;
import com.seb39.myfridge.domain.auth.handler.*;
import com.seb39.myfridge.domain.auth.service.AuthenticationTokenService;
import com.seb39.myfridge.domain.auth.service.OAuth2UserService;
import com.seb39.myfridge.global.dto.ErrorResponse;
import com.seb39.myfridge.domain.member.service.MemberService;
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
import org.springframework.security.web.firewall.RequestRejectedHandler;

import javax.servlet.http.HttpServletResponse;

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
    private final AuthenticationTokenService authenticationTokenService;
    private final JwtLogoutHandler logoutHandler;
    private final ObjectMapper om;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable();
        http.headers()
                .frameOptions()
                .disable();
        http.formLogin()
                .disable();
        http.httpBasic()
                .disable();
        http.cors()
                .disable();

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .apply(new CustomJwtConfigurer())
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

        http.logout()
                .permitAll()
                .logoutUrl("/api/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                });

        return http.build();
    }

    public class CustomJwtConfigurer extends AbstractHttpConfigurer<CustomJwtConfigurer, HttpSecurity> {

        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, authenticationTokenService);
            jwtAuthenticationFilter.setFilterProcessesUrl("/api/login");
            jwtAuthenticationFilter.setAuthenticationFailureHandler(jwtAuthenticationFailureHandler);

            GuestJwtAuthenticationFilter guestJwtAuthenticationFilter = new GuestJwtAuthenticationFilter(authenticationManager,authenticationTokenService,memberService);
            guestJwtAuthenticationFilter.setFilterProcessesUrl("/api/login/guest");
            guestJwtAuthenticationFilter.setAuthenticationFailureHandler(jwtAuthenticationFailureHandler);

            JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager, memberService, authenticationTokenService);
            JwtExceptionHandlingFilter jwtExceptionHandlingFilter = new JwtExceptionHandlingFilter(authenticationManager);

            builder
                    .addFilter(guestJwtAuthenticationFilter)
                    .addFilter(jwtAuthenticationFilter)
                    .addFilter(jwtAuthorizationFilter)
                    .addFilterBefore(jwtExceptionHandlingFilter, JwtAuthenticationFilter.class);
        }
    }

    @Bean
    public RequestRejectedHandler requestRejectedHandler(){
        return (request, response, requestRejectedException) -> {
            om.writeValue(response.getWriter(), new ErrorResponse(requestRejectedException.getMessage()));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        };
    }
}
