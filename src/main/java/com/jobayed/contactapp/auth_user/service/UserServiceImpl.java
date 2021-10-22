package com.jobayed.bloggingapp.auth_user.service;

import com.jobayed.bloggingapp.auth_user.domain.Role;
import com.jobayed.bloggingapp.auth_user.domain.User;
import com.jobayed.bloggingapp.auth_user.repo.RoleRepo;
import com.jobayed.bloggingapp.auth_user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class UserServiceImpl implements UserService, UserDetailsService
{
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if(user==null)
        {
            log.error("User not found!");
            throw new UsernameNotFoundException("User not found!");
        }
        else {
            log.info("Found User: {}",username);

        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRole().forEach(role->{
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core
                .userdetails.User(user.getUsername(),user.getPassword(),authorities);
    }

    private String getCurrentDateTime()
    {
        Date date = new Date();
        String strDateFormat = "d MM, Y hh:mm:ss a";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate= dateFormat.format(date);
        return formattedDate;
    }

    @Override
    public User saveUser(User user) {
        User u = userRepo.save(user);
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        log.info("Saving new User {} at {}",u.getName(),this.getCurrentDateTime());
        return u;
    }

    @Override
    public Role saveRole(Role role) {
        Role r = roleRepo.save(role);
        log.info("Saving new Role {} at {}",r.getName(),this.getCurrentDateTime());
        return r;
    }

    @Override
    public void assignRole(String username, String rolename) {
        User user = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(rolename);

        user.getRole().add(role);
        log.info("Assigned role {} to user {} at {}",rolename,username,this.getCurrentDateTime());
    }

    @Override
    public User getUser(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        return userRepo.findAll();
    }
}
