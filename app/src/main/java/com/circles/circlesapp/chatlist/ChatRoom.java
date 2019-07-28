package com.circles.circlesapp.chatlist;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatRoom implements Parcelable {
    public String title;
    public String channel;
    public String room;
    public String image;
    public LastMessage last_message;
    public boolean is_group;
    public int id;
    public String time;
    public long created_at;
   public double latitude;
   public double longitude;


    protected ChatRoom(Parcel in) {
        title = in.readString();
        channel = in.readString();
        room = in.readString();
        image = in.readString();
        is_group = in.readByte() != 0;
        id = in.readInt();
        time = in.readString();
        created_at = in.readLong();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public ChatRoom(String title, String channel, String room, int id, double latitude, double longitude) {
        this.title = title;
        this.channel = channel;
        this.room = room;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static final Creator<ChatRoom> CREATOR = new Creator<ChatRoom>() {
        @Override
        public ChatRoom createFromParcel(Parcel in) {
            return new ChatRoom(in);
        }

        @Override
        public ChatRoom[] newArray(int size) {
            return new ChatRoom[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(channel);
        parcel.writeString(room);
        parcel.writeString(image);
        parcel.writeByte((byte) (is_group ? 1 : 0));
        parcel.writeInt(id);
        parcel.writeString(time);
        parcel.writeLong(created_at);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }
}
