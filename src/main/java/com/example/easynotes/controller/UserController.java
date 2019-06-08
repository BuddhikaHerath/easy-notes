package com.example.easynotes.controller;

import com.example.easynotes.Services.CustomUserDetailsService;
import com.example.easynotes.model.User;
import com.example.easynotes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@CrossOrigin("*")
@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CustomUserDetailsService customerUserDetailsService;
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

    @PreAuthorize("hasAnyRole('CONSUMER') or hasAnyRole('ADMIN') ")
    @GetMapping("/user/name")
    public List<User> securedHello(User user){
        String Email = user.getEmail();
        String Password =user.getPassword();

        List<User>lol=userRepository.findByEmailAndPassword(Email,Password);

        return lol ;
    }

    @GetMapping("/secured/logout")
    public UserDetails alternate(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth!=null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return customerUserDetailsService.loadUserByUsername("123");
    }
}