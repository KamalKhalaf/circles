package com.circles.circlesapp.messaging.model;

import com.google.gson.annotations.SerializedName;

import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor;

public class MessageSender {
    private boolean isCurrentuser;
    @SerializedName("id")
    private int id;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("profile_image")
    private String profileImage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return (id == MyServiceInterceptor.userId) ? "you" : this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public boolean isCurrentuser() {
        return (id == MyServiceInterceptor.userId);
    }

    public void setCurrentuser(boolean currentuser) {
        isCurrentuser = currentuser;
    }
}
