package com.criminals.plusExponential.domain.entity;

import com.criminals.plusExponential.domain.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@MappedSuperclass
@Getter
public class User extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true)
    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "올브란 이메일 형식이 아닙니다.")
    private String email;


    @Column(nullable = false)
    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @JsonIgnore
    private String password;

    @Column
    private String pgToken;




}
