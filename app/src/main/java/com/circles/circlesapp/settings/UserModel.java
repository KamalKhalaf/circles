package com.circles.circlesapp.settings;

/**/

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.circles.circlesapp.BR;
import com.circles.circlesapp.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserModel extends BaseObservable implements Parcelable {
    private String first_name;
    private String last_name;
    private String email;
    private String phone;
    private String city;
    private String country;
    private Date birthdate;
    private String username;
    private String description;
    private int gender;
    private String password;
    private String password_confirmation;
    private String profile_image;

    @BindingAdapter("profileIMG")
    public static void profileIMG(CircleImageView view,String profile_image){
        Glide.with(view.getContext())
                .load(profile_image)
                .apply(RequestOptions.placeholderOf(R.drawable.man))
                .into(view);
    }

    @Bindable
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
        notifyPropertyChanged(BR.first_name);
    }

    public String dateFormate(Date date){
        if (date==null)return null;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        return simpleDateFormat.format(date);
    }

    @Bindable
    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
        notifyPropertyChanged(BR.last_name);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
    }

    @Bindable
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        notifyPropertyChanged(BR.city);
    }

    @Bindable
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
        notifyPropertyChanged(BR.country);
    }

    @Bindable
    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
        notifyPropertyChanged(BR.birthdate);
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
        notifyPropertyChanged(BR.gender);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public String getPassword_confirmation() {
        return password_confirmation;
    }

    public void setPassword_confirmation(String password_confirmation) {
        this.password_confirmation = password_confirmation;
        notifyPropertyChanged(BR.password_confirmation);
    }

    @Bindable
    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
        notifyPropertyChanged(BR.profile_image);
    }

    public UserModel() {
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", birthdate=" + birthdate +
                ", username='" + username + '\'' +
                ", description='" + description + '\'' +
                ", gender=" + gender +
                ", password='" + password + '\'' +
                ", password_confirmation='" + password_confirmation + '\'' +
                ", profile_image='" + profile_image + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.first_name);
        dest.writeString(this.last_name);
        dest.writeString(this.email);
        dest.writeString(this.phone);
        dest.writeString(this.city);
        dest.writeString(this.country);
        dest.writeLong(this.birthdate != null ? this.birthdate.getTime() : -1);
        dest.writeString(this.username);
        dest.writeString(this.description);
        dest.writeInt(this.gender);
        dest.writeString(this.password);
        dest.writeString(this.password_confirmation);
        dest.writeString(this.profile_image);
    }

    protected UserModel(Parcel in) {
        this.first_name = in.readString();
        this.last_name = in.readString();
        this.email = in.readString();
        this.phone = in.readString();
        this.city = in.readString();
        this.country = in.readString();
        long tmpBirthdate = in.readLong();
        this.birthdate = tmpBirthdate == -1 ? null : new Date(tmpBirthdate);
        this.username = in.readString();
        this.description = in.readString();
        this.gender = in.readInt();
        this.password = in.readString();
        this.password_confirmation = in.readString();
        this.profile_image = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel source) {
            return new UserModel(source);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };
}
