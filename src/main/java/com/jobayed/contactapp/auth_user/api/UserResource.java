package com.jobayed.bloggingapp.auth_user.api;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobayed.bloggingapp.auth_user.domain.Role;
import com.jobayed.bloggingapp.auth_user.domain.User;
import com.jobayed.bloggingapp.auth_user.service.UserService;
import com.jobayed.bloggingapp.auth_user.utils.JwtUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController @RequiredArgsConstructor
public class UserResource {
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getusers(){
        return ResponseEntity.ok(userService.getUsers());
    }

    @PostMapping("/user/store")
    public ResponseEntity<User> storeUser(@RequestBody User user){
        return ResponseEntity.ok(userService.saveUser(user));
    }
    @PostMapping("/role/store")
    public ResponseEntity<Role> storeRole(@RequestBody Role role){
        return ResponseEntity.ok(userService.saveRole(role));
    }
    @PostMapping("/user/assign-role")
    public ResponseEntity<?> assignRole(@RequestBody RoleToUserForm form){
        userService.assignRole(form.getUsername(),form.getRolename());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/token/refresh")
    public void getRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String AUTHORIZATION_HEADER = request.getHeader(AUTHORIZATION);
        String refresh_token = null;
        String username = null;
        String new_access_token = null;

        if (AUTHORIZATION_HEADER!=null && AUTHORIZATION_HEADER.startsWith("Bearer "))
        {
            refresh_token = AUTHORIZATION_HEADER.substring("Bearer ".length());
            try {

                DecodedJWT decodedJWT = jwtUtils.getTokenDecoder(refresh_token);
                username = decodedJWT.getSubject();
                User user = userService.getUser(username);
                List<?> claims = user.getRole().stream()
                        .map(Role::getName)
                        .collect(Collectors.toList());
                String issuer = request.getRequestURL().toString();
                new_access_token = jwtUtils.getAccessToken(username,claims,issuer);

                Map<String,String> tokens = new HashMap<>();
                tokens.put("access_token",new_access_token);
                tokens.put("refresh_token",refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);

            }catch (IllegalArgumentException e) {
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

            if(AUTHORIZATION_HEADER==null)
            {
                log.debug("Refresh Token is missing...");
                throw new RuntimeException("Refresh Token is Missing");
            }
            else log.debug("Refresh Token does not begin with Bearer String");

        }
    }

    @Data
    class RoleToUserForm{
        private String username;
        private String rolename;
    }
}
