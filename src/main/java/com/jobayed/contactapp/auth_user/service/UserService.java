package com.jobayed.contactapp.auth_user.service;

import com.jobayed.contactapp.auth_user.domain.Role;
import com.jobayed.contactapp.auth_user.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void assignRole(String username, String rolename);
    User getUser(String username);
    List<User> getUsers();
}
