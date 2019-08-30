package com.circles.circlesapp.phase2.services.model;
/*
 *
 * Created by Kamal Khalaf on 8/29/2019.
 * Contact : kamal.khalaf56@gmail.com
 *
 */

import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor;

public class MessagePhase2 {

    private boolean isOnline;
    private String userName;
    private String nearBy;
    private String date;
    private boolean isCurrentuser;


    public MessagePhase2(boolean isOnline, String userName, String nearBy, String date, boolean isCurrentuser) {
        this.isOnline = isOnline;
        this.userName = userName;
        this.nearBy = nearBy;
        this.date = date;
        this.isCurrentuser = isCurrentuser;
    }

    public boolean isCurrentuser() {
        return isCurrentuser;
    }


    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNearBy() {
        return nearBy;
    }

    public void setNearBy(String nearBy) {
        this.nearBy = nearBy;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCurrentuser(boolean currentuser) {
        isCurrentuser = currentuser;
    }
}
