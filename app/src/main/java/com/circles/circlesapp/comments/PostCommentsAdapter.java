package com.circles.circlesapp.comments;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.ComentItemBinding;
import com.circles.circlesapp.helpers.MySeekBarCallback;
import com.circles.circlesapp.helpers.SeekBarUtil;
import com.circles.circlesapp.helpers.base.BaseListAdapter;
import com.circles.circlesapp.helpers.base.DataBindingViewHolder;
import com.circles.circlesapp.helpers.utilities.CustomOnClickListener;
import com.circles.circlesapp.helpers.utilities.ElapsedTime;
import com.jsibbold.zoomage.ZoomageView;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class PostCommentsAdapter extends BaseListAdapter<PostCommentMode, ComentItemBinding> implements MySeekBarCallback {
    private static DiffUtil.ItemCallback<PostCommentMode> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<PostCommentMode>() {
                @Override
                public boolean areItemsTheSame(@NonNull PostCommentMode old,
                                               @NonNull PostCommentMode newOne) {
                    return old.getCommentId() == newOne.getCommentId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull PostCommentMode oldOne, @NonNull PostCommentMode newOne) {
                    return oldOne.equals(newOne);
                }

            };
    private CustomOnClickListener<PostCommentMode> listener;
    private List<PostCommentMode> list;
    private SeekBar currentSeekBar;
    private TextView currentSeekTime;
    private ImageView currentPlayBtn;

    public PostCommentsAdapter(CustomOnClickListener<PostCommentMode> listener, List<PostCommentMode> list) {
        super(DIFF_CALLBACK);
        this.listener = listener;
        this.list = list;
        submitList(list);
    }

    @Override
    public void onBindViewHolder(@NonNull DataBindingViewHolder<PostCommentMode, ComentItemBinding> holder, int position) {
        super.onBindViewHolder(holder, position);

        PostCommentMode commentMode = list.get(position);
        if (commentMode.commentImageUrl == null || commentMode.commentImageUrl.length() < 2) {
            holder.b.postImage.setVisibility(View.GONE);
        } else {
            holder.b.postImage.setVisibility(View.VISIBLE);

        }
        if (commentMode.recordUrl != null && commentMode.recordUrl.length() > 2) {
            holder.b.seekBar2.setVisibility(View.VISIBLE);
            holder.b.playBtn.setVisibility(View.VISIBLE);
            holder.b.recodStartTv.setVisibility(View.VISIBLE);
        } else {
            holder.b.seekBar2.setVisibility(View.GONE);
            holder.b.playBtn.setVisibility(View.GONE);
            holder.b.recodStartTv.setVisibility(View.GONE);
        }
        //
        holder.b.commentCount.setText(commentMode.commentsCount);
        holder.b.likesCountTv.setText(commentMode.likesCount);
        holder.b.timeAgoTv.setText(ElapsedTime.getFromDate(new Date(commentMode.timeStamp)));
        holder.b.name.setText(commentMode.fullName);
        holder.b.commentText.setText(commentMode.commentText);
        if (commentMode.commentImageUrl != null && !commentMode.commentImageUrl.equals("")) {

            Glide.with(getContext()).load(commentMode.commentImageUrl).addListener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    holder.b.progressBarload.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    holder.b.progressBarload.setVisibility(View.GONE);
                    return false;
                }
            }).into(holder.b.commentImage);


            holder.b.commentImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    final Dialog dialog = new Dialog(getContext(), R.style.coupon_Dialog);
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
                    Glide.with(getContext())
                            .load(commentMode.commentImageUrl)
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
                }
            });

        } else {
            holder.b.postImage.setVisibility(View.GONE);
        }


        if (commentMode.userImageUrl != null)
            Glide.with(getContext()).asBitmap().load(commentMode.userImageUrl).into(holder.b.imageProfil);
        holder.b.playBtn.setOnClickListener(v -> {
            SeekBarUtil barUtil = SeekBarUtil.newInstance(holder.b.seekBar2.toString(), this);
            currentSeekBar = holder.b.seekBar2;
            currentSeekTime = holder.b.recodStartTv;
            currentPlayBtn = holder.b.playBtn;
            if (barUtil.isPlayin()) {
                barUtil.pauseMediaPlayer();
                holder.b.playBtn.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_pause_recorded_audio));
            } else {
                barUtil.startmediaPlayer(commentMode.recordUrl);
                holder.b.playBtn.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_play_recorded_audio));

            }
        });
        holder.b.repliesImg.setOnClickListener(v -> {
            listener.onclick(v, commentMode);
        });

    }

    @Override
    public int getLayoutIdForPosition(int position) {
        return R.layout.coment_item;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public void onItemClicked(View v, PostCommentMode item) {
        listener.onclick(v, item);
    }

    @Override
    public void setSeekMax(int max) {
        currentSeekBar.setMax(max);
    }

    @Override
    public void setprogress(int playerCurrentPosition) {
        currentSeekBar.setProgress(playerCurrentPosition);
    }

    @Override
    public void updateSeekTv(String currentSec) {
        currentSeekTime.setText(currentSec);
    }

    @Override
    public void finishSeekBar() {
        currentPlayBtn.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_play_recorded_audio));
        currentSeekTime.setText("00:00");
        setprogress(0);
    }

    @Override
    public void callHearVoiceApi() {

    }

}
