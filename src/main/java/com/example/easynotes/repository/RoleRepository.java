package com.example.easynotes.repository;

import com.example.easynotes.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    public Role findByRole(String role);
}
