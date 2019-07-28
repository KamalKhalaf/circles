package com.circles.circlesapp.replies;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.List;

import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.ReplyItemBinding;
import com.circles.circlesapp.helpers.MySeekBarCallback;
import com.circles.circlesapp.helpers.SeekBarUtil;
import com.circles.circlesapp.helpers.base.BaseListAdapter;
import com.circles.circlesapp.helpers.base.DataBindingViewHolder;
import com.circles.circlesapp.helpers.utilities.CustomOnClickListener;
import com.circles.circlesapp.helpers.utilities.ElapsedTime;

public class RepliesAdapter extends BaseListAdapter<ReplayModel, ReplyItemBinding> implements MySeekBarCallback {
    private static DiffUtil.ItemCallback<ReplayModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<ReplayModel>() {
                @Override
                public boolean areItemsTheSame(@NonNull ReplayModel old,
                                               @NonNull ReplayModel newOne) {
                    return old.getReplyId() == newOne.getReplyId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull ReplayModel oldOne, @NonNull ReplayModel newOne) {
                    return oldOne.equals(newOne);
                }

            };
    private CustomOnClickListener<ReplayModel> listener;
    private List<ReplayModel> list;
    private SeekBar currentSeekBar;
    private TextView currentSeekTime;
    private ImageView currentPlayBtn;

    public RepliesAdapter(CustomOnClickListener<ReplayModel> listener, List<ReplayModel> list) {
        super(DIFF_CALLBACK);
        this.listener = listener;
        this.list = list;
        submitList(list);
    }

    @Override
    public void onBindViewHolder(@NonNull DataBindingViewHolder<ReplayModel, ReplyItemBinding> holder, int position) {
        super.onBindViewHolder(holder, position);

        ReplayModel commentMode = list.get(position);
        if (commentMode.replyImageUrl == null || commentMode.replyImageUrl.length() < 2) {
            holder.b.commentImage.setVisibility(View.GONE);
        } else {
            holder.b.commentImage.setVisibility(View.VISIBLE);

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
        holder.b.likesCountTv.setText(commentMode.likesCount);
        holder.b.timeAgoTv.setText(ElapsedTime.getFromDate(new Date(commentMode.timeStamp)));
        holder.b.name.setText(commentMode.fullName);
        holder.b.commentText.setText(commentMode.replyText);
        Glide.with(getContext()).asBitmap().load(commentMode.replyImageUrl).into(holder.b.commentImage);
        if (commentMode.userImageUrl != null)
            Glide.with(getContext()).asBitmap().load(commentMode.userImageUrl).into(holder.b.imageProfile);
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

    }

    @Override
    public int getLayoutIdForPosition(int position) {
        return R.layout.reply_item;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public void onItemClicked(View v, ReplayModel item) {
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
