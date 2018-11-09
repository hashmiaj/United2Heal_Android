package com.u2h.user.united2healandroid;

import android.app.Application;

public class UserInfo extends Application {
    private String groupName="B";
<<<<<<< HEAD
=======
    private String password="";
    public Boolean allowAsync=true;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
>>>>>>> 36296f2... Bug Fixes

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
