package com.example.easynotes.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
    @Table(name = "role")
    public class Role {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "role_id")
        private int roleId;

        @Column(name = "role")
        private String role;

//        @ManyToMany(mappedBy = "userRoles")
//        @Setter @Getter private Set<User> user;

        public Role() {
        }

        public int getRoleId() {
            return roleId;
        }

        public void setRoleId(int roleId) {
            this.roleId = roleId;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }

