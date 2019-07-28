package com.circles.circlesapp.settings;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.SerializedName;

import com.circles.circlesapp.BR;

/**/
public  class PrivacyModel extends BaseObservable  {

    @SerializedName("profile_privacy")
    private boolean profile_privacy;
    @SerializedName("following_followers_privacy")
    private boolean following_followers_privacy;
    @SerializedName("age_location_privacy")
    private boolean age_location_privacy;

    @Bindable
    public boolean getProfile_privacy() {
        return profile_privacy;
    }

    public void setProfile_privacy(boolean profile_privacy) {
        this.profile_privacy = profile_privacy;
        notifyPropertyChanged(BR.profile_privacy);
    }

    @Bindable
    public boolean getFollowing_followers_privacy() {
        return following_followers_privacy;
    }

    public void setFollowing_followers_privacy(boolean following_followers_privacy) {
        this.following_followers_privacy = following_followers_privacy;
        notifyPropertyChanged(BR.following_followers_privacy);
    }

    @Bindable
    public boolean getAge_location_privacy() {
        return age_location_privacy;
    }

    public void setAge_location_privacy(boolean age_location_privacy) {
        this.age_location_privacy = age_location_privacy;
        notifyPropertyChanged(BR.age_location_privacy);
    }
}
