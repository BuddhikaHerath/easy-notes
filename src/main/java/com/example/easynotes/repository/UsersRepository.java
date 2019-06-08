package com.example.easynotes.repository;

import com.example.easynotes.model.User;
import com.example.easynotes.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//@Repository
//
//public interface UsersRepository extends JpaRepository<Users, String> {
//    Optional<Users> findByName(String username);
//    //Optional<User> findByUsername(String text);
//    List<Users> findByEmailAndPassword(String text, String textAgain);
//}