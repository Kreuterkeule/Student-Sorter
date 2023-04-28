package com.kreuterkeule.StudentSorter.controller;

import com.kreuterkeule.StudentSorter.model.UserEntity;
import com.kreuterkeule.StudentSorter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class TestController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public String test() {
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(username);
        System.out.println(authorities);
        return "test";
    }

}
