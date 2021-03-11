package com.example.demo.web.controller;

import com.example.demo.model.Role;
import com.example.demo.service.UserService;
import com.example.demo.web.dto.UserRegisterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Configuration
@RequestMapping("/")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserController(@Lazy UserService userService,
                          @Lazy AuthenticationManager authenticationManager){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new UserRegisterDto());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user")UserRegisterDto userRegisterDto,
                           Errors errors,
                           @RequestParam("type") String type,
                           HttpServletRequest request){
        if(errors.hasErrors() ){
            return "register";
        }
        if(type.equals("0")){
            userRegisterDto.setRole(Role.READER);
        }
        else{
            userRegisterDto.setRole(Role.AUTHOR);
        }
        userService.save(userRegisterDto);
        authenticateUserAndSetSession(userRegisterDto.getEmail(), userRegisterDto.getPassword(), request);
        return "redirect:/index";
    }

    private void authenticateUserAndSetSession(String email, String password, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);

        request.getSession();

        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/")
    public String index(){
        return "index";
    }

}
