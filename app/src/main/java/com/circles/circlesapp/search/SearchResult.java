package com.circles.circlesapp.search;



import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.LatLng;

import com.circles.circlesapp.BR;
import com.circles.circlesapp.R;

public class SearchResult extends BaseObservable implements Parcelable {
    private int id;
    private String name;
    private String image;
    private String latitude;
    private String longitude;
    private String type;
    private String password;

    protected SearchResult(Parcel in) {
        id = in.readInt();
        name = in.readString();
        image = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        type = in.readString();
    }

    public static final Creator<SearchResult> CREATOR = new Creator<SearchResult>() {
        @Override
        public SearchResult createFromParcel(Parcel in) {
            return new SearchResult(in);
        }

        @Override
        public SearchResult[] newArray(int size) {
            return new SearchResult[size];
        }
    };

    @BindingAdapter("searchIMG")
    public static void searchIMG(ImageView view, String image) {
        Glide.with(view.getContext())
                .load(image)
                .apply(RequestOptions.placeholderOf(R.drawable.sign_up_profile))
                .into(view);
    }

    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
        notifyPropertyChanged(BR.image);
    }

    @Bindable
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
        notifyPropertyChanged(BR.latitude);
    }

    @Bindable
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
        notifyPropertyChanged(BR.longitude);
    }

    @Bindable
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        notifyPropertyChanged(BR.type);
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public LatLng getLocation() {
        try {
            double lat = Double.parseDouble(getLatitude());
            double longi = Double.parseDouble(getLongitude());
            return new LatLng(lat, longi);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return new LatLng(0, 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(image);
        parcel.writeString(latitude);
        parcel.writeString(longitude);
        parcel.writeString(type);
    }

    public String getPassword() {
        return (password==null)?"0":password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
