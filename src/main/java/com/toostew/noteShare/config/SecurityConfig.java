package com.toostew.noteShare.config;

import com.toostew.noteShare.service.auth.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/upload").hasAnyRole("USER")
                                .requestMatchers("/fileReceived").hasAnyRole("USER")
                                .anyRequest().permitAll()
                                )
                .formLogin(formLogin ->
                        formLogin.loginPage("/login")
                                .loginProcessingUrl("/authenticate-user")
                                .permitAll()
                                .defaultSuccessUrl("/", true))
                .logout(logout ->logout.permitAll()


                );

        return http.build();
    }

}
