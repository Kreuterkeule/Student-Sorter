package com.kreuterkeule.StudentSorter.dto;

import com.kreuterkeule.StudentSorter.model.CreateUserErrorType;

import java.util.List;
import java.util.Map;

public class CreateUsersResponseDto {

    public List<ResponseUser> createdUsers;
    public Map<ResponseUser, CreateUserErrorType> errorUsers;

    public CreateUsersResponseDto(List<ResponseUser> createdUsers, Map<ResponseUser, CreateUserErrorType> errorUsers) {
        this.createdUsers = createdUsers;
        this.errorUsers = errorUsers;
    }
}
