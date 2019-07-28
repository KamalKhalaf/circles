package com.circles.circlesapp.addgroup;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MessageEvent implements Parcelable {

    private boolean isComment;
    private boolean isUpdatePost;
    private int reqCode;

    public MessageEvent() {
    }

    public MessageEvent(LatLng latLng) {
        this.latLng = latLng;
    }

    private LatLng latLng;
    private String postText;
    private String text;
    private int postId;
    private int id;
    private String recordpath;
    private ArrayList<String> imagePaths;
    private ArrayList<String> image;

    protected MessageEvent(Parcel in) {
        isComment = in.readByte() != 0;
        isUpdatePost = in.readByte() != 0;
        latLng = in.readParcelable(LatLng.class.getClassLoader());
        postText = in.readString();
        text = in.readString();
        postId = in.readInt();
        id = in.readInt();
        recordpath = in.readString();
        imagePaths = in.createStringArrayList();
        image = in.createStringArrayList();
    }

    public static final Creator<MessageEvent> CREATOR = new Creator<MessageEvent>() {
        @Override
        public MessageEvent createFromParcel(Parcel in) {
            return new MessageEvent(in);
        }

        @Override
        public MessageEvent[] newArray(int size) {
            return new MessageEvent[size];
        }
    };

    public LatLng getLatLng() {
        return latLng;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRecordpath() {
        return (recordpath==null)?"":recordpath;
    }

    public void setRecordpath(String recordpath) {
        Log.d("setRecordpath", recordpath);

        this.recordpath = recordpath;
    }

    public ArrayList<String> getImagePaths() {
        return (imagePaths==null)?new ArrayList<>():imagePaths;
    }

    public void setImagePaths(ArrayList<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public ArrayList<String> getImage() {
        return (image==null)?new ArrayList<>():image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public void setIsComment(boolean isComment) {


        this.isComment = isComment;
    }

    public boolean getIsComment() {
        return isComment;
    }

    public boolean isUpdatePost() {
        return isUpdatePost;
    }

    public void setUpdatePost(boolean updatePost) {
        isUpdatePost = updatePost;
    }

    @Override
    public String toString() {
        return "MessageEvent{" +
                "isComment=" + isComment +
                "isUpdatePost=" + isUpdatePost +
                ", latLng=" + latLng +
                ", postText='" + postText + '\'' +
                ", text='" + text + '\'' +
                ", postId=" + postId +
                ", id=" + id +
                ", recordpath='" + recordpath + '\'' +
                ", imagePaths=" + imagePaths +
                ", image=" + image +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isComment ? 1 : 0));
        parcel.writeByte((byte) (isUpdatePost ? 1 : 0));
        parcel.writeParcelable(latLng, i);
        parcel.writeString(postText);
        parcel.writeString(text);
        parcel.writeInt(postId);
        parcel.writeInt(id);
        parcel.writeString(recordpath);
        parcel.writeStringList(imagePaths);
        parcel.writeStringList(image);
    }

    public void setReqCode(int reqCode) {
        this.reqCode = reqCode;
    }

    public int getReqCode() {
        return reqCode;
    }
}
