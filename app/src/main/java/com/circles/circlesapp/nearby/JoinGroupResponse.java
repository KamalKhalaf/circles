package com.circles.circlesapp.nearby;

import android.os.Parcel;
import android.os.Parcelable;

public class JoinGroupResponse implements Parcelable {
    public int id;
    public String channel;
    public String room;
    public String groupName;
    public double latitude;
    public double longitude;


    protected JoinGroupResponse(Parcel in) {
        id = in.readInt();
        channel = in.readString();
        room = in.readString();
        groupName = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<JoinGroupResponse> CREATOR = new Creator<JoinGroupResponse>() {
        @Override
        public JoinGroupResponse createFromParcel(Parcel in) {
            return new JoinGroupResponse(in);
        }

        @Override
        public JoinGroupResponse[] newArray(int size) {
            return new JoinGroupResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(channel);
        parcel.writeString(room);
        parcel.writeString(groupName);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }
}
