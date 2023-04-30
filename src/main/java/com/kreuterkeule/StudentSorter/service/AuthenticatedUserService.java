package com.kreuterkeule.StudentSorter.service;

import com.kreuterkeule.StudentSorter.model.UserEntity;
import com.kreuterkeule.StudentSorter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedUserService {

    @Autowired
    UserRepository userRepository;

    public UserEntity getUserFromSecurityContext(SecurityContext securityContext) {
        String username = securityContext.getAuthentication().getName();
        return userRepository.findByUsername(username);
    }

}
