package com.kreuterkeule.StudentSorter.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Set;

@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private String role; // ?? geht das oder muss man granted authorities verwenden?
    private String priorityMate;
    private Set<String> fiveMates;
}
