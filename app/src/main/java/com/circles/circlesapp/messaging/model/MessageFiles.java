package com.circles.circlesapp.messaging.model;

import com.google.gson.annotations.SerializedName;

public class MessageFiles {
    public String message_file;
    public String message_file_type;
    public int voting_count;
    @SerializedName("isVoted")
    public boolean is_liked;
    @SerializedName("id")
    public int message_file_id;
    public transient int itemPostion;// used to notify this position after voting
}
