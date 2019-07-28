
package com.circles.circlesapp.chatlist;
import com.google.gson.annotations.SerializedName;


public class ChatObject {

    @SerializedName("created_at")
    private Long mCreatedAt;
    @SerializedName("full_name")
    private String mFullName;
    @SerializedName("id")
    private Long mId;
    @SerializedName("image")
    private Object mImage;
    @SerializedName("is_like")
    private Boolean mIsLike;
    @SerializedName("is_shared")
    private Boolean mIsShared;
    @SerializedName("likes")
    private Long mLikes;
    @SerializedName("profile_image")
    private Object mProfileImage;
    @SerializedName("shared_at")
    private Long mSharedAt;
    @SerializedName("shared_from")
    private SharedFrom mSharedFrom;
    @SerializedName("text")
    private Object mText;
    @SerializedName("title")
    private Object mTitle;
    @SerializedName("user_id")
    private Long mUserId;
    @SerializedName("voice_note")
    private String mVoiceNote;

    public Long getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Long createdAt) {
        mCreatedAt = createdAt;
    }

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

    public Object getImage() {
        return mImage;
    }

    public void setImage(Object image) {
        mImage = image;
    }

    public Boolean getIsLike() {
        return mIsLike;
    }

    public void setIsLike(Boolean isLike) {
        mIsLike = isLike;
    }

    public Boolean getIsShared() {
        return mIsShared;
    }

    public void setIsShared(Boolean isShared) {
        mIsShared = isShared;
    }

    public Long getLikes() {
        return mLikes;
    }

    public void setLikes(Long likes) {
        mLikes = likes;
    }

    public Object getProfileImage() {
        return mProfileImage;
    }

    public void setProfileImage(Object profileImage) {
        mProfileImage = profileImage;
    }

    public Long getSharedAt() {
        return mSharedAt;
    }

    public void setSharedAt(Long sharedAt) {
        mSharedAt = sharedAt;
    }

    public SharedFrom getSharedFrom() {
        return mSharedFrom;
    }

    public void setSharedFrom(SharedFrom sharedFrom) {
        mSharedFrom = sharedFrom;
    }

    public Object getText() {
        return mText;
    }

    public void setText(Object text) {
        mText = text;
    }

    public Object getTitle() {
        return mTitle;
    }

    public void setTitle(Object title) {
        mTitle = title;
    }

    public Long getUserId() {
        return mUserId;
    }

    public void setUserId(Long userId) {
        mUserId = userId;
    }

    public String getVoiceNote() {
        return mVoiceNote;
    }

    public void setVoiceNote(String voiceNote) {
        mVoiceNote = voiceNote;
    }

}
