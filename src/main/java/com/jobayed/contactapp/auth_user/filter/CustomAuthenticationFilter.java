package com.jobayed.contactapp.auth_user.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobayed.contactapp.auth_user.utils.JwtUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private boolean postOnly = true;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager,JwtUtils jwtUtils)
    {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            try {
                UsernamePassword authRequest = new ObjectMapper()
                        .readValue(request.getInputStream(), UsernamePassword.class);
                String username = authRequest.getUsername();
                String password = authRequest.getPassword();
                log.info("Username is {}", username);

                UsernamePasswordAuthenticationToken authenticationToken
                        = new UsernamePasswordAuthenticationToken(username, password);
                return authenticationManager.authenticate(authenticationToken);
            } catch (IOException e) {
                log.error("Unreadable Value Exception: " + e.getMessage());
                return null;

            }
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication)
            throws IOException, ServletException {
        User user = (User)authentication.getPrincipal();
        List<?> claims = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        String issuer = request.getRequestURL().toString();

        String access_token = null;
        String refresh_token = null;
        try {
            access_token = jwtUtils.getAccessToken(user.getUsername(),claims,issuer);
            refresh_token = jwtUtils.getRefreshToken(user.getUsername(),issuer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String,String> tokens = new HashMap<>();
        tokens.put("access_token",access_token);
        tokens.put("refresh_token",refresh_token);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),tokens);
    }

    @Data
    static class UsernamePassword{
        private String username;
        private String password;
    }


}
