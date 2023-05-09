package com.kreuterkeule.StudentSorter.calculator;

import com.kreuterkeule.StudentSorter.calculator.entity.CalcClass;
import com.kreuterkeule.StudentSorter.calculator.entity.CalcUser;
import com.kreuterkeule.StudentSorter.model.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CalcRunner {

    private static final int MAX_OFFSET = 5;
    private static final int CLASS_SIZE = 25;
    private List<CalcUser> calcUsers;

    public CalcRunner(List<UserEntity> users /* class count made by length */) {
        this.calcUsers = this.ConvertToCaclList(users); // converting to in memory type, cause database is too slow for this calculation
    }

    public List<CalcUser> ConvertToCaclList(List<UserEntity> users) {
        List<CalcUser> retList = new ArrayList<>();
        for (UserEntity user : users) {
            CalcUser calcUser = new CalcUser();
            calcUser.setWp(user.getWP());
            calcUser.setPrio(user.getPriorityMate());
            calcUser.setName(user.getUsername());
            calcUser.setBili(user.isBilingual());
            retList.add(calcUser);
        }
        for (UserEntity user : users) {

        }
        System.out.println(retList);
        return new ArrayList<>();
    }

    public Map<CalcClass, List<CalcUser>> run() {
        return null;
    }

}
