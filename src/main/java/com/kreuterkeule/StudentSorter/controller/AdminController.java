package com.kreuterkeule.StudentSorter.controller;

import com.kreuterkeule.StudentSorter.dto.CreateUserDto;
import com.kreuterkeule.StudentSorter.dto.UserDto;
import com.kreuterkeule.StudentSorter.model.UserEntity;
import com.kreuterkeule.StudentSorter.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RequestMapping("/api/admin")
@RestController
public class AdminController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/createUser")
    public ResponseEntity<UserEntity> createUser(@RequestBody CreateUserDto createUserDto) {
        if (userRepository.findByUsername(createUserDto.username) != null) { // username exists
            return new ResponseEntity<>(new UserEntity("username", "exists", "USER"), HttpStatus.BAD_REQUEST);
        }
        UserEntity newUser = new UserEntity(
                createUserDto.username,
                passwordEncoder.encode(createUserDto.password) ,
                "USER"
        );
        UserEntity returnUser = userRepository.save(newUser);
        return new ResponseEntity<>(returnUser, HttpStatus.OK);
    }
    @PostMapping("/createAdmin")
    public ResponseEntity<UserEntity> createAdmin(@RequestBody CreateUserDto createUserDto) {
        if (userRepository.findByUsername(createUserDto.username) != null) { // username exists
            return new ResponseEntity<>(new UserEntity("username", "exists", "USER"), HttpStatus.BAD_REQUEST);
        }
        UserEntity newUser = new UserEntity(
                createUserDto.username,
                passwordEncoder.encode(createUserDto.password),
                "ADMIN"
        );
        UserEntity returnUser = userRepository.save(newUser);
        return new ResponseEntity<>(returnUser, HttpStatus.OK);
    }
    @GetMapping("/delete")
    public ResponseEntity<UserDto> deleteUser(@RequestParam("id") Long id) {
        UserEntity deletedUser = userRepository.findById(id).get();
        if (deletedUser == null) {
            return new ResponseEntity<>(new UserDto("", "", 0L), HttpStatus.BAD_REQUEST); // invalid id
        }
        System.out.println(deletedUser);
        userRepository.delete(deletedUser);
        return new ResponseEntity<>(new UserDto(deletedUser.getUsername(),
                deletedUser.getPassword(),
                deletedUser.getId()), HttpStatus.BAD_REQUEST); // return deleted User for debugging
    }
    @GetMapping("/getUsers")
    public ResponseEntity<List<UserEntity>> getUsers() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }
}
