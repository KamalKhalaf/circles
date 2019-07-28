package com.circles.circlesapp.nearby;

import android.os.Parcel;
import android.os.Parcelable;

public class JoinGroupReqBody implements Parcelable {
    public double longitude;
    public double latitude;
    public String id;
    public int password;
    public transient String groupName;

    public JoinGroupReqBody(Parcel in) {
        longitude = in.readDouble();
        latitude = in.readDouble();
        id = in.readString();
        password = in.readInt();
    }

    public JoinGroupReqBody() {
    }

    public static final Creator<JoinGroupReqBody> CREATOR = new Creator<JoinGroupReqBody>() {
        @Override
        public JoinGroupReqBody createFromParcel(Parcel in) {
            return new JoinGroupReqBody(in);
        }

        @Override
        public JoinGroupReqBody[] newArray(int size) {
            return new JoinGroupReqBody[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(longitude);
        parcel.writeDouble(latitude);
        parcel.writeString(id);
        parcel.writeInt(password);
    }
}
