package com.circles.circlesapp.messaging.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Message {
    public static final String pollType = "voting";
    public static final String voiceType = "voice";
    public static final String imageType = "image";
    public static final String videoType = "video";
    private boolean isPoll;
    private boolean isVoice;
    private boolean isText;
    private boolean isImage;
    @SerializedName("id")
    private int id;

    @SerializedName("message_body")
    private String messageBody;

    @SerializedName("created_at")
    private long createdAt;

    @SerializedName("type")
    private String type;

    @SerializedName("sender")
    private MessageSender messageSender;
    @SerializedName("files")
    private List<MessageFiles> files;

    public Message() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return getId() == message.getId();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<MessageFiles> getFiles() {
        return (files == null) ? new ArrayList<>() : files;
    }

    public MessageFiles getFirstFile() {
        if (getFiles().size() > 0) {
            return getFiles().get(0);
        }
        return new MessageFiles();
    }

    public MessageFiles getSecondFile() {
        if (getFiles().size() > 1) {
            return getFiles().get(1);
        }
        return new MessageFiles();
    }

    public void setFiles(List<MessageFiles> files) {
        this.files = files;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }


    public MessageSender getMessageSender() {
        return messageSender;
    }

    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public boolean isPoll() {
        return type.equals(pollType);
    }



    public void setPoll(boolean poll) {
        isPoll = type.equals(pollType);
    }

    public boolean isVoice() {
        if (files != null && files.size() > 0) {
            if (files.size() > 1) return false;
            else return files.get(0).message_file_type.equals(voiceType);
        }
        return false;
    }

    public void setVoice(boolean voice) {
        isVoice = voice;
    }

    public boolean isText() {
        if (files != null) {
            return files.isEmpty();
        }
        return false;
    }

    public void setText(boolean text) {
        isText = text;
    }

    public boolean isImage() {
        if (files != null && files.size() > 0) {
//            if (files.size() > 1) return false;
//            else
                return files.get(0).message_file_type.equals(imageType);


        }
        return false;
    }

    public boolean isVideo() {
        if (files != null && files.size() > 0) {
//            if (files.size() > 1) return false;
//            else
                return files.get(0).message_file_type.equals(videoType);


        }
        return false;
    }

    public void setImage(boolean image) {
        isImage = image;
    }

    public int getTotalVotes() {
        return getFirstFile().voting_count + getSecondFile().voting_count;
    }


    public float getFirstPercentage() {
        if (getTotalVotes() == 0) return 0;
        return (float) (getFirstFile().voting_count / getTotalVotes()) * 100;
    }

    public float getSecondPercentage() {
        if (getTotalVotes() == 0) return 0;
        return (float) (getSecondFile().voting_count / getTotalVotes()) * 100;
    }

    public void incrementFirstFileVoting() {
        getFirstFile().voting_count = getFirstFile().voting_count + 1;
    }

    public void incrementSeconfFileVoting() {
        getSecondFile().voting_count = getSecondFile().voting_count + 1;

    }

    public void decrementFirstFileVoting() {
        if(getFirstFile().voting_count==0)return;
        getFirstFile().voting_count = getFirstFile().voting_count - 1;

    }

    public void decrementSecondFileVoting() {
        if(getSecondFile().voting_count==0)return;
        getSecondFile().voting_count = getSecondFile().voting_count - 1;
    }
}
