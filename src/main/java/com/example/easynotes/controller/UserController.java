package com.example.easynotes.controller;

import com.example.easynotes.Services.CustomUserDetailsService;
import com.example.easynotes.model.Role;
import com.example.easynotes.model.User;
import com.example.easynotes.repository.RoleRepository;
import com.example.easynotes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin("*")
@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

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
    @PostMapping("/user/name")
    public List<User> securedHello(@RequestBody User user){
        String Username = user.getUsername();
        String Password =user.getPassword();

        List<User>lol=userRepository.findByUsernameAndPassword(Username,Password);

        return lol ;
    }

    @PostMapping("/register/user")
    public ResponseEntity<Object> registerUser(@RequestBody User user){

        Optional<User> userExists = userRepository.findByUsername(user.getUsername());
        if(!userExists.isPresent()){
            List<Role> roles = new ArrayList<Role>();
            Role role = roleRepository.findByRole("CONSUMER");
            roles.add(role);
            user.setRoles(new HashSet<Role>(roles));
            user = userRepository.save(user);
            return new ResponseEntity<Object>(user, HttpStatus.OK);
        }
        return new ResponseEntity<Object>(null, HttpStatus.NOT_FOUND);
    }
//    @GetMapping("/secured/logout")
//    public UserDetails alternate(HttpServletRequest request, HttpServletResponse response){
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if(auth!=null){
//            new SecurityContextLogoutHandler().logout(request, response, auth);
//        }
//        return customerUserDetailsService.loadUserByUsername("123");
//    }

}