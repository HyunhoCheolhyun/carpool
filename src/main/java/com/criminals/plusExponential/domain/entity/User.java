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

<<<<<<< HEAD
    @OneToMany(mappedBy = "driver")
    List<MatchedPath> matchedPaths = new ArrayList<>();
=======

>>>>>>> d6cbd26 ([Fix] 1. 택시비용 음수값 문제 해결, 2. 파트너 매칭 이후에도 스레드가 두개 도는 문제해결)
}
