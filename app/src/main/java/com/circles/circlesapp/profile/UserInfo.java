package com.circles.circlesapp.profile;



import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.SerializedName;

import com.circles.circlesapp.BR;

public  class UserInfo extends BaseObservable {

    @SerializedName("country")
    private String country;
    @SerializedName("city")
    private String city;
    @SerializedName("description")
    private String description;
    @SerializedName("followers")
    private int followers;
    @SerializedName("following")
    private int following;
    @SerializedName("age")
    private int age;
    @SerializedName("profile_image")
    private String profile_image;
    @SerializedName("username")
    private String username;
    @SerializedName("last_name")
    private String last_name;
    @SerializedName("first_name")
    private String first_name;
    @SerializedName("is_followed")
    private boolean isFollow;

    @Bindable
    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
        notifyPropertyChanged(BR.follow);
    }

    @Bindable
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
        notifyPropertyChanged(BR.country);
    }


    @Bindable
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        notifyPropertyChanged(BR.city);
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
        notifyPropertyChanged(BR.followers);
    }

    @Bindable
    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
        notifyPropertyChanged(BR.following);
    }

    @Bindable
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
        notifyPropertyChanged(BR.age);
    }

    @Bindable
    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
        notifyPropertyChanged(BR.profile_image);
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    @Bindable
    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
        notifyPropertyChanged(BR.last_name);
    }

    @Bindable
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
        notifyPropertyChanged(BR.first_name);
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", description='" + description + '\'' +
                ", followers=" + followers +
                ", following=" + following +
                ", age=" + age +
                ", profile_image='" + profile_image + '\'' +
                ", username='" + username + '\'' +
                ", last_name='" + last_name + '\'' +
                ", first_name='" + first_name + '\'' +
                '}';
    }
}
