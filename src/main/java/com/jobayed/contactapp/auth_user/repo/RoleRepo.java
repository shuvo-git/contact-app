package com.jobayed.bloggingapp.auth_user.repo;

import com.jobayed.bloggingapp.auth_user.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role,Long> {
    Role findByName(String name);
}
