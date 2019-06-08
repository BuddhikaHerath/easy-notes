package com.example.easynotes.Services;

import com.example.easynotes.model.CustomUserDetails;
import com.example.easynotes.model.User;
import com.example.easynotes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> optionalUsers=userRepository.findByUsername(username);

        optionalUsers
                .orElseThrow(()-> new UsernameNotFoundException("Username Not Found"));
        return optionalUsers
                .map( CustomUserDetails::new).get();
    }
}


