package com.example.easynotes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    @Getter
    private long id;

    @Setter @Getter private String orderStatus;

    @ManyToOne
    @JoinColumn(name="user_id",referencedColumnName = "id")
    @Setter @Getter private User user;

    //@JsonIgnore
    @OneToMany(mappedBy = "orders",fetch = FetchType.EAGER)
    @Getter @Setter
    List<OrderProducts> orderProducts;
}
