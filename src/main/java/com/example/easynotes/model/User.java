package com.example.easynotes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    @Getter
    private long id;

    @Setter @Getter private String username;

    @Setter @Getter private String name;

    @Setter @Getter private String password;

    @Setter @Getter private String email;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Setter @Getter private Set<Role> roles;

    @JsonIgnore
    @OneToMany
    @Getter @Setter private List<Orders> orders;

    public User() {

    }

    public User(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.password = user.getPassword();
        this.roles = user.getRoles();
    }


}
