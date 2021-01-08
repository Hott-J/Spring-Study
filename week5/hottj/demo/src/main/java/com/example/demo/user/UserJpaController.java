package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/jpa") // prefix
public class UserJpaController {

    @Autowired
    private UserRepository userRepository;

    // http://localhost:8080/jpa/users
    @GetMapping("/users")
    public List<User> retrieveAllUsers(){
        return userRepository.findAll();
    }
}