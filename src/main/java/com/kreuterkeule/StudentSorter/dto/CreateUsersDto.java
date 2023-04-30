package com.kreuterkeule.StudentSorter.dto;

import java.util.List;

public class CreateUsersDto {

    public int userCount;
    public List<String> usernames;

    public CreateUsersDto(int userCount, List<String> usernames) {
        this.userCount = userCount;
        this.usernames = usernames;
    }
}
