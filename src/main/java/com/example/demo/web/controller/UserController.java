package com.example.demo.web.controller;

import com.example.demo.model.Book;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.service.BookService;
import com.example.demo.service.UserService;
import com.example.demo.web.dto.UserRegisterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Configuration
@RequestMapping("/")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final BookService bookService;

    @Autowired
    public UserController(@Lazy UserService userService,
                          @Lazy AuthenticationManager authenticationManager,
                          @Lazy BookService bookService){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.bookService = bookService;
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user")User user,
                           Errors errors,
                           @RequestParam("type") String type,
                           HttpServletRequest request){
        if(errors.hasErrors() ){
            return "register";
        }
        if(type.equals("0")){
            user.setRole(Role.READER);
        }
        else{
            user.setRole(Role.AUTHOR);
        }
        userService.save(user);
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String update(Model model,
                         @AuthenticationPrincipal UserDetails user){
        User user1 = userService.findUser(user.getUsername());
        model.addAttribute("user", user1);
        return "userUpdate";
    }

    @PostMapping("/profile")
    public String update(@Valid @ModelAttribute("user")User user,
                           Errors errors){
        if(errors.hasErrors() ){
            return "userUpdate";
        }
        userService.update(user);
        return "userUpdate";
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
    public String index(Model model,
                        @AuthenticationPrincipal UserDetails user,
                        HttpServletRequest request){
        List<Book> books;

        User user1 = userService.findUser(user.getUsername());
        if(user1.getRole() == Role.AUTHOR){

           books = bookService.getBooksByUser(user1);
        }
        else{
            books = bookService.getBooks();
        }
        //List<Book> books = bookService.getBooks();

        model.addAttribute("books", books);
        return "index";
    }

    @PostMapping("/search")
    public String search(Model model, @RequestParam("Search") String title) throws IOException {
        List<Book> books = bookService.getBooksByTitle(title);
        model.addAttribute("books", books);
        return "index";

    }

}
