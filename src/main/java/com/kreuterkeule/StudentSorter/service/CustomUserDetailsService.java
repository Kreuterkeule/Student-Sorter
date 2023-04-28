package com.kreuterkeule.StudentSorter.service;

import com.kreuterkeule.StudentSorter.model.UserEntity;
import com.kreuterkeule.StudentSorter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${spring.security.user.role}")
    private String adminRole;
    @Value("${spring.security.user.name}")
    private String adminUsername;
    @Value("${spring.security.user.password}")
    private String adminPassword;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals("admin")) {
            System.out.println("admin user");
            return User.builder().username(adminUsername).password(passwordEncoder.encode(adminPassword)).roles(adminRole).build();
        }
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            System.out.println("username not found");
            throw new UsernameNotFoundException(username);
        }
        System.out.println("returning normal user" + user.getUsername());
        return User.builder().username(user.getUsername()).password(user.getPassword()).roles(user.getRole()).build();
    }
}
