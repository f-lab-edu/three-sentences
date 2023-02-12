package com.sh.threesentences.auth.config;

import com.sh.threesentences.auth.service.CustomAuthenticationProvider;
import com.sh.threesentences.auth.service.JpaUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class AuthConfig {

    private final CustomAuthenticationProvider authProvider;

    private final JpaUserDetailService jpaUserDetailService;


    @Bean
    public UserDetailsService userDetailsService() {
        return jpaUserDetailService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeHttpRequests()
            .antMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
            .antMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
            .antMatchers(HttpMethod.GET, "/api/v1/users/email/duplicate-check").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().disable()
            .httpBasic();

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
            http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authProvider);
        return authenticationManagerBuilder.build();
    }

}
