package com.kreuterkeule.StudentSorter.controller;

import com.kreuterkeule.StudentSorter.dto.CreateUserDto;
import com.kreuterkeule.StudentSorter.model.UserEntity;
import com.kreuterkeule.StudentSorter.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/createUser")
    public ResponseEntity<UserEntity> createUser(@RequestBody CreateUserDto createUserDto) {
        return null;
    }
    @PostMapping("/createAdmin")
    public ResponseEntity<UserEntity> createAdmin(@RequestBody CreateUserDto createUserDto) {
        return null;
    }
    @GetMapping("/getUsers")
    public ResponseEntity<List<UserEntity>> getusers() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

}
