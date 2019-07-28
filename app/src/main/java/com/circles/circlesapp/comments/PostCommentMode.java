package com.circles.circlesapp.comments;

import com.google.gson.annotations.SerializedName;

public class PostCommentMode {
    @SerializedName("id")
    private int commentId;
    @SerializedName("user_id")
    private int user_id;
    @SerializedName("profile_image")
    public String userImageUrl;
    @SerializedName("image")
    public String commentImageUrl;
    @SerializedName("created_at")
    public long timeStamp;
    @SerializedName("full_name")
    public String fullName;
   // @SerializedName("id")
    public String commentsCount="0";
   // @SerializedName("id")
    public String likesCount="0";
    @SerializedName("text")
    public String commentText;
    @SerializedName("voice_note")
    public String recordUrl;


    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }
}
