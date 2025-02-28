package com.criminals.plusExponential.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class User {


    @Id
    private Long id;

    @Column
    Role role;

    @Column
    String password;

    @Column
    String username;



//    public Long getId() {
//        return id;
//    }

//    public Role getRole() {
//        return this.role;
//    }
}
