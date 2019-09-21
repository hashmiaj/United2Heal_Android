package com.u2h.user.united2healandroid;

import android.app.Application;

public class UserInfo extends Application {
    private String groupName="";

    private String schoolName="";
    private String password="";
    public Boolean allowAsync=true;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

}
