package com.example.activitidemo.bean;

import org.springframework.stereotype.Component;

@Component
public class genderBean {
    private  String name = "张三";
    private String genderString;
    private String gender ;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenderString() {
        return genderString;
    }
    public String getGenderString(String gender) {
        return gender;
    }

    public void setGenderString(String genderString) {
        this.genderString = genderString;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
