package com.kreuterkeule.StudentSorter.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChangeFiveMatesDto {

    public List<String> fiveMates;

    public ChangeFiveMatesDto(String name1, String name2, String name3, String name4, String name5) {
        this.fiveMates = Arrays.asList(
                name1.strip().toLowerCase(),
                name2.strip().toLowerCase(),
                name3.strip().toLowerCase(),
                name4.strip().toLowerCase(),
                name5.strip().toLowerCase());
    }
}
