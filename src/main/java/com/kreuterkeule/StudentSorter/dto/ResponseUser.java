package com.kreuterkeule.StudentSorter.dto;

public class ResponseUser {

    public String username;
    public String password;

    @Override
    public String toString() {
        return "{\"" +
                "username\":\"" + username + '"' +
                ", \"password\":\"" + password + '"' +
                ", \"role\":\"" + role + '"' +
                '}';
    }

    public String role;

    public ResponseUser(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public ResponseUser() {
    }
}
