package com.jobayed.bloggingapp.auth_user.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobayed.bloggingapp.auth_user.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static java.util.stream.StreamSupport.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if(request.getServletPath().equals("/login") ||
                request.getServletPath().equals("/token/refresh")
        )
        {
            filterChain.doFilter(request,response);
        }
        else {
            final String AUTHORIZATION_HEADER = request.getHeader(AUTHORIZATION);
            String token = null;
            String username = null;
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

            if (AUTHORIZATION_HEADER!=null && AUTHORIZATION_HEADER.startsWith("Bearer "))
            {
                token = AUTHORIZATION_HEADER.substring("Bearer ".length());

                try {

                    DecodedJWT decodedJWT = jwtUtils.getTokenDecoder(token);
                    username = decodedJWT.getSubject();
                    Collection<String> roles = decodedJWT.getClaim("Roles").asList(String.class);
                    roles.stream().forEach(role-> {
                        authorities.add(new SimpleGrantedAuthority(role));
                    });
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                new UsernamePasswordAuthenticationToken(
                                        username, null, authorities
                                );
                        usernamePasswordAuthenticationToken
                                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                    }

                } catch (IllegalArgumentException e) {
                    log.debug("Unable to get JWT Token");
                    throw new IllegalArgumentException("Unable to get JWT Token");
                } catch (Exception e){
                    log.debug("Error Logging In: {}",e.getMessage());

                    response.setStatus(FORBIDDEN.value());
                    Map<String,String> errors = new HashMap<>();
                    errors.put("error_messages",e.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(),errors);
                }

            } else {
                log.debug("JWT Token does not begin with Bearer String");
            }


            filterChain.doFilter(request, response);
        }
    }
}
