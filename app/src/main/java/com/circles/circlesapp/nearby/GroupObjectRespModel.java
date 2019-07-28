package com.circles.circlesapp.nearby;

import android.os.Parcel;
import android.os.Parcelable;

public class GroupObjectRespModel implements Parcelable {
    public String id;
    public String name;
    public String type;
    public String users_count;
    public String cover;
    public String distance;
    public int password;
    public boolean is_public;
    public double longitude;
    public double latitude;

    protected GroupObjectRespModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        type = in.readString();
        users_count = in.readString();
        cover = in.readString();
        distance = in.readString();
        is_public = in.readByte() != 0;
        longitude = in.readDouble();
        latitude = in.readDouble();
    }

    public static final Creator<GroupObjectRespModel> CREATOR = new Creator<GroupObjectRespModel>() {
        @Override
        public GroupObjectRespModel createFromParcel(Parcel in) {
            return new GroupObjectRespModel(in);
        }

        @Override
        public GroupObjectRespModel[] newArray(int size) {
            return new GroupObjectRespModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(type);
        parcel.writeString(users_count);
        parcel.writeString(cover);
        parcel.writeString(distance);
        parcel.writeByte((byte) (is_public ? 1 : 0));
        parcel.writeDouble(longitude);
        parcel.writeDouble(latitude);
    }
}
