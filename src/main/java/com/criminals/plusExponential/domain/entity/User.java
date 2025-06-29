package com.criminals.plusExponential.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;


    @Column(nullable = false, unique = true)
    private String email;


    @Column(nullable = false)
    private String password;


    @Enumerated(EnumType.STRING)
    @Column
    private Role role;

    @OneToOne(mappedBy = "user")
    private UnmatchedPath unmatchedPath;


    @OneToMany(mappedBy = "driver")
    List<MatchedPath> matchedPaths = new ArrayList<>();
}
