
package com.circles.circlesapp.chatlist;

import com.google.gson.annotations.SerializedName;


public class SharedFrom {

    @SerializedName("full_name")
    private String mFullName;
    @SerializedName("id")
    private Long mId;
    @SerializedName("profile_image")
    private Object mProfileImage;
    @SerializedName("share_id")
    private Long mShareId;
    @SerializedName("username")
    private String mUsername;

    public String getFullName() {
        return mFullName;
    }

    public void setFullName(String fullName) {
        mFullName = fullName;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public Object getProfileImage() {
        return mProfileImage;
    }

    public void setProfileImage(Object profileImage) {
        mProfileImage = profileImage;
    }

    public Long getShareId() {
        return mShareId;
    }

    public void setShareId(Long shareId) {
        mShareId = shareId;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

}
