package com.example.easynotes.controller;

import com.example.easynotes.exception.ResourceNotFoundException;
import com.example.easynotes.model.User;
import com.example.easynotes.model.Users;
import com.example.easynotes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

//    @Autowired
//    UsersRepository usersRepository;

    @GetMapping("all")
    public String hello(){
        return "Hello";
    }


    //  public String securedHello(){
    //return "Secured Hello";
    //  public List<Users> index(){
    //    return userRepository.findAll( );
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("secured/all")
    public List<User> index(){
        return userRepository.findAll( );
    }
//}


    @GetMapping("/user/name")
    public List<User> securedHello(@RequestBody Map<String, String> body){
        String Email = body.get("email");
        String Password = body.get("password");

        List<User>lol=userRepository.findByEmailAndPassword(Email,Password);
        return lol ;
    }

    @GetMapping("/secured/alternate")
    public String alternate(){
        return "alternate";
    }
}