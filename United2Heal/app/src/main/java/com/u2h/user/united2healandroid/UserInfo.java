package com.u2h.user.united2healandroid;

import android.app.Application;

public class UserInfo extends Application {
    private String groupName="B";

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
