package com.circles.circlesapp.messaging.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SendMessageBody {
    private String messageBody;
    private int id;
    private double latitude;
    private double longitude;
    private String messageType;
    private String MessageFileType;
    private List<File> fileList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getMessageBody() {
        return (messageBody==null)?"":messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageFileType() {
        return MessageFileType;
    }

    public void setMessageFileType(String messageFileType) {
        MessageFileType = messageFileType;
    }

    public List<File> getFileList() {
        return (fileList==null)?new ArrayList<>():fileList;
    }

    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
    }

    public void setFileList(String path) {
        File f=new File(path);
        List<File> fs=new ArrayList<>();
        fs.add(f);
        this.fileList = fs;
    }

    @Override
    public String toString() {
        return "SendMessageBody{" +
                "messageBody='" + messageBody + '\'' +
                ", id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", messageType='" + messageType + '\'' +
                ", MessageFileType='" + MessageFileType + '\'' +
                ", fileList=" + fileList +
                '}';
    }
}
