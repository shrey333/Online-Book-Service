package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.web.dto.UserRegisterDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User save(UserRegisterDto userRegisterDto);
}
