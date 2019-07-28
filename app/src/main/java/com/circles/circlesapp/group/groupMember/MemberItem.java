package com.circles.circlesapp.group.groupMember;



import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.annotations.SerializedName;

import com.circles.circlesapp.BR;
import com.circles.circlesapp.R;

public class MemberItem extends BaseObservable {
    @SerializedName("id")
    private int memberId;
    @SerializedName("name")

    private String memberName;
    private String city = "nasr city";
    @SerializedName("profile_image")
    private String memberImg;
    private boolean isMute;

    @BindingAdapter("loadIMG")
    public static void loadIMG(ImageView view, String memberImg) {
        if (memberImg == null || memberImg.length() < 3) {
            Glide.with(view.getContext())
                    .load(R.drawable.sign_up_profile)
                    .into(view);
        } else
            Glide.with(view.getContext())
                    .load(memberImg)
                    .into(view);
    }

    @Bindable
    public boolean isMute() {
        return isMute;
    }

    public void setMute(boolean mute) {
        isMute = mute;
        notifyPropertyChanged(BR.mute);
    }

    @Bindable
    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
        notifyPropertyChanged(BR.memberId);
    }

    @Bindable
    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
        notifyPropertyChanged(BR.memberName);
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
    public String getMemberImg() {
        return memberImg;
    }

    public void setMemberImg(String memberImg) {
        this.memberImg = memberImg;
        notifyPropertyChanged(BR.memberImg);
    }
}
