package com.kreuterkeule.StudentSorter.dto;

import java.util.Map;

public class UserDto {
    public String username;
    public String password;
    public Long id;

    public UserDto(String username, String password, Long id) {
        this.username = username;
        this.password = password;
        this.id = id;
    }

    public UserDto() {
    }
}
