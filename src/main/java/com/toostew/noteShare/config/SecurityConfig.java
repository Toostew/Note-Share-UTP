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
                                //specifically /upload and /fileReceived is blocked to registered users
                                .requestMatchers("/upload").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/fileReceived").hasAnyRole("USER","ADMIN")
                                .requestMatchers("/file-verify/**").hasAnyRole("ADMIN") //all file-verify pages are admin only
                                .anyRequest().permitAll() //every other page can be freely accessed
                                )
                .formLogin(formLogin ->
                        formLogin.loginPage("/login")
                                .loginProcessingUrl("/authenticate-user")
                                .permitAll()
                                .defaultSuccessUrl("/", true))
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/access-denied"))
                .logout(logout ->logout.permitAll()


                );

        return http.build();
    }

}
