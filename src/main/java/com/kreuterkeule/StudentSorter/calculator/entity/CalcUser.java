package com.kreuterkeule.StudentSorter.calculator.entity;

import java.util.List;

public class CalcUser {

    public String name;
    public String prio;
    public List<CalcUser> five;
    public String wp;
    public boolean bili;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrio() {
        return prio;
    }

    public void setPrio(String prio) {
        this.prio = prio;
    }

    public List<CalcUser> getFive() {
        return five;
    }

    public void setFive(List<CalcUser> five) {
        this.five = five;
    }

    public String getWp() {
        return wp;
    }

    public void setWp(String wp) {
        this.wp = wp;
    }

    public boolean isBili() {
        return bili;
    }

    public void setBili(boolean bili) {
        this.bili = bili;
    }

    public CalcUser() {
    }

    public CalcUser(String name, String prio, List<CalcUser> five, String wp, boolean bili) {
        this.name = name;
        this.prio = prio;
        this.five = five;
        this.wp = wp;
        this.bili = bili;
    }
}
