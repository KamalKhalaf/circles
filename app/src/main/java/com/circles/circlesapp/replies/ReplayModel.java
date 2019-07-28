package com.circles.circlesapp.replies;

public class ReplayModel {
    private boolean replyId;
    public String userImageUrl;
    public String replyImageUrl;
    public long timeStamp;
    public String fullName;
    public String likesCount;
    public String replyText;
    public String recordUrl;

    public boolean getReplyId() {

        return replyId;
    }

    public void setReplyId(boolean replyId) {
        this.replyId = replyId;
    }
}
