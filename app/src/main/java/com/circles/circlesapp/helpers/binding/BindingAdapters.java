package com.circles.circlesapp.helpers.binding;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.circles.circlesapp.R;

public class BindingAdapters {
    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext()).load(imageUrl).into(view);
    }

    @BindingAdapter("userProfileImage")
    public static void loadProfileImage(ImageView view, String imageURL) {
        Glide.with(view.getContext())
                .load(imageURL)
                .apply(RequestOptions.placeholderOf(R.drawable.avatar))
                .apply(RequestOptions.circleCropTransform())
                .into(view);
    }

    @BindingAdapter("ImageUrlCircular")
    public static void loadCircularImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .apply(RequestOptions.circleCropTransform().placeholder(R.drawable.avatar))
                .into(view);
    }

    @BindingAdapter("imageSource")
    public static void setImageDrawableRes(ImageView view, Drawable drawable) {
        view.setImageDrawable(drawable);
    }

}
