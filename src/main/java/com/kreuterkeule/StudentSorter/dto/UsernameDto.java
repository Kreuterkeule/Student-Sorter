package com.kreuterkeule.StudentSorter.dto;

public class UsernameDto {

    public String username;

    public UsernameDto(String username) {
        this.username = username.strip().toLowerCase();
    }
}
