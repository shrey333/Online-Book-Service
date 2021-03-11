package com.example.demo.web.dto;

import com.example.demo.model.Role;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserRegisterDto {

    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    private Role role;
}
