package com.circles.circlesapp.messaging.view.adapter;


import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.ListItemPhotoBinding;
import com.circles.circlesapp.messaging.model.MessageFiles;
import com.jsibbold.zoomage.ZoomageView;

import java.util.List;
import java.util.Objects;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
    private List<MessageFiles> messageFiles;
    private Context context;

    public PhotoAdapter(List<MessageFiles> messageFiles, Context context) {
        this.messageFiles = messageFiles;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ListItemPhotoBinding b =
                DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                        R.layout.list_item_photo, viewGroup, false);


        return new ViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RequestOptions requestOptions = new RequestOptions();
        RequestOptions myOptions = new RequestOptions()
                .centerCrop();

        RequestOptions myOptionsList = new RequestOptions()
                .override(500, 500)
                .centerCrop();

        MessageFiles mesFile = messageFiles.get(position);
        if(messageFiles.size() > 1) {
            holder.b.multLay.setVisibility(View.VISIBLE);

            if (mesFile.message_file != null && !mesFile.message_file.equals("")) {
                Glide.with(context).applyDefaultRequestOptions(requestOptions).asBitmap()
                        .apply(myOptionsList).load(mesFile.message_file)
                        .listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                holder.b.progressBarload.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                holder.b.progressBarload.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(holder.b.ivImage);

            } else {
                holder.b.progressBarload.setVisibility(View.GONE);
                Glide.with(context).load(R.drawable.sign_up_profile).into(holder.b.ivImage);
            }




            holder.b.ivImage.setOnClickListener(v -> {
                final Dialog dialog = new Dialog(context, R.style.coupon_Dialog);
                dialog.setContentView(R.layout.dialog_zoom_in);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                if (!dialog.isShowing()) {
                    dialog.show();
                }

                ZoomageView imageZoom = (ZoomageView) dialog.findViewById(R.id.myZoomageView);
                ProgressBar progressBarload = (ProgressBar) dialog.findViewById(R.id.progressBarload);
                TextView textView = dialog.findViewById(R.id.close);
                Glide.with(context)
                        .load(mesFile.message_file)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                progressBarload.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressBarload.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(imageZoom);


                textView.setOnClickListener(v1 -> {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                });
            });

        }
    }

    @Override
    public int getItemCount() {
        return messageFiles == null ? 0 : messageFiles.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ListItemPhotoBinding b;

        public ViewHolder(ListItemPhotoBinding binding) {
            super(binding.getRoot());
            this.b = binding;
        }
    }
}
