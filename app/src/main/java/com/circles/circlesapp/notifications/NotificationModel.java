package com.circles.circlesapp.notifications;


import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.circles.circlesapp.R;
import com.google.gson.annotations.SerializedName;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.share.internal.DeviceShareDialogFragment.TAG;

public class NotificationModel extends BaseObservable {

    @SerializedName("created_at")
    private int created_at;
    @SerializedName("post_id")
    private int post_id;
    @SerializedName("action_type")
    private String action_type;
    @SerializedName("full_name")
    private String full_name;
    @SerializedName("id")
    private int id;


    @BindingAdapter("app:notificationImg")
    public static void notificationImg(CircleImageView view,String url){
        Glide.with(view.getContext())
                .load(url)
                 .apply(RequestOptions.placeholderOf(R.drawable.avatar))
                .into(view);
    }

    @BindingAdapter("app:notificationTxt")
    public static void notificationTxt(TextView txtView,String msg){
        txtView.append(getColoredString(msg, ContextCompat.getColor(txtView.getContext(), R.color.colorPrimary)));
        txtView.append(getColoredString("  Post a Voice Comment at Your Post", ContextCompat.getColor(txtView.getContext(),R.color.black)));
    }

    public static Spannable getColoredString(String mString, int colorId) {
        Spannable spannable = new SpannableString(mString);
        spannable.setSpan(new ForegroundColorSpan(colorId), 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public int getCreated_at() {
        return created_at;
    }

    public void setCreated_at(int created_at) {
        this.created_at = created_at;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getAction_type() {
        return action_type;
    }

    public void setAction_type(String action_type) {
        this.action_type = action_type;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
