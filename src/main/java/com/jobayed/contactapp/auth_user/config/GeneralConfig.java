package com.jobayed.bloggingapp.auth_user.config;

import com.jobayed.bloggingapp.auth_user.utils.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class GeneralConfig {
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean JwtUtils jwtUtils(){return new JwtUtils();}
}
