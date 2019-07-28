package com.circles.circlesapp.messaging.view.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.util.DiffUtil;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.OtherUserMessageItemBinding;
import com.circles.circlesapp.helpers.MySeekBarCallback;
import com.circles.circlesapp.helpers.SeekBarUtil;
import com.circles.circlesapp.helpers.SharedPrefHelper;
import com.circles.circlesapp.helpers.base.BaseListAdapter;
import com.circles.circlesapp.helpers.base.DataBindingViewHolder;
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor;
import com.circles.circlesapp.helpers.utilities.CustomOnClickListener;
import com.circles.circlesapp.helpers.utilities.ElapsedTime;
import com.circles.circlesapp.messaging.model.Message;
import com.circles.circlesapp.messaging.model.MessageFiles;
import com.circles.circlesapp.profile.UserProfileActivity;
import com.jsibbold.zoomage.ZoomageView;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MessagesAdapter extends BaseListAdapter<Message, OtherUserMessageItemBinding> implements MySeekBarCallback {

    private List<Message> messageList;
    private CustomOnClickListener<MessageFiles> voteClickCallback;
    private SeekBar currentSeekBar;
    private TextView currentSeekTime;
    private ImageView currentPlayBtn;
    private SeekBarUtil seekBarUtil;
    private SharedPrefHelper prefHelper;
    private Context context;

    public MessagesAdapter(Context context, List<Message> messages, CustomOnClickListener<MessageFiles> voteClickCallback) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.messageList = messages;
        this.voteClickCallback = voteClickCallback;
    }

    @Override
    public int getLayoutIdForPosition(int position) {
        return R.layout.other_user_message_item;
    }

    @Override
    protected Message getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull DataBindingViewHolder<Message, OtherUserMessageItemBinding> holder, int position) {
        super.onBindViewHolder(holder, position);
        Message m = messageList.get(position);
        if (prefHelper == null) prefHelper = new SharedPrefHelper(getContext());

        if (prefHelper.getMutedSet().contains(m.getMessageSender().getId() + "")) {
            holder.b.itemMsg.setVisibility(View.GONE);
            holder.b.itemMsg.setMaxHeight(0);
        } else {
            holder.b.itemMsg.setVisibility(View.VISIBLE);
            holder.b.itemMsg.setMaxHeight(4000);
        }

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.b.mainConstraint.getLayoutParams();
        if (m.getMessageSender().isCurrentuser()) {
            params.horizontalBias = 0.81f;
            holder.b.mainConstraint.setLayoutParams(params);
            holder.b.mainConstraint.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_primary_color_backg));
            holder.b.pollCv.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_primary_color_backg));
            holder.b.time.setTextColor(getContext().getResources().getColor(R.color.white));
            holder.b.name.setTextColor(getContext().getResources().getColor(R.color.white));
            holder.b.messageText.setTextColor(getContext().getResources().getColor(R.color.white));
            holder.b.totlaFeesTv.setTextColor(getContext().getResources().getColor(R.color.white));
            holder.b.otherUserImage.setVisibility(View.GONE);
            holder.b.myImage.setVisibility(View.VISIBLE);
            if (m.getMessageSender().getProfileImage() == null || m.getMessageSender().getProfileImage().length() < 3)
                holder.b.myImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.sign_up_profile));
            else
                Glide.with(getContext()).asBitmap().load(m.getMessageSender().getProfileImage()).into(holder.b.myImage);
        } else {
            params.horizontalBias = 0.21f;
            holder.b.mainConstraint.setLayoutParams(params);
            holder.b.mainConstraint.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_white_backg));
            holder.b.pollCv.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_white_backg));
            holder.b.time.setTextColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
            holder.b.name.setTextColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
            holder.b.messageText.setTextColor(getContext().getResources().getColor(R.color.black));
            holder.b.totlaFeesTv.setTextColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
            holder.b.otherUserImage.setVisibility(View.VISIBLE);
            holder.b.myImage.setVisibility(View.GONE);
            if (m.getMessageSender().getProfileImage() == null || m.getMessageSender().getProfileImage().length() < 3) {
                holder.b.myImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.avatar));
            } else
                Glide.with(getContext()).asBitmap().load(m.getMessageSender().getProfileImage()).into(holder.b.otherUserImage);

        }
        if (m.isPoll()) {
            holder.b.pollCv.setVisibility(View.VISIBLE);
            holder.b.seekBarView.setVisibility(View.GONE);
            holder.b.imageCv.setVisibility(View.GONE);
            holder.b.messageText.setVisibility(View.GONE);
            holder.b.totlaFeesTv.setText("Total votes :" + m.getTotalVotes() + "");
            holder.b.pollQuestionTv.setText(m.getMessageBody());
            holder.b.percentageLeft.setText(m.getFirstPercentage() + "");
            holder.b.percentageRight.setText(m.getSecondPercentage() + "");
            if (m.getFirstFile().is_liked) {
                holder.b.pollVote.setImageDrawable(getContext().getResources().getDrawable(R.drawable.poll_liked));
            } else {
                holder.b.pollVote.setImageDrawable(getContext().getResources().getDrawable(R.drawable.poll_not_liked));

            }
            if (m.getSecondFile().is_liked) {
                holder.b.pollVoteRight.setImageDrawable(getContext().getResources().getDrawable(R.drawable.poll_liked));
            } else {
                holder.b.pollVoteRight.setImageDrawable(getContext().getResources().getDrawable(R.drawable.poll_not_liked));
            }
            holder.b.pollVote.setOnClickListener(v -> {
                if (m.getFirstFile().is_liked || m.getSecondFile().is_liked) return;
                m.getFirstFile().is_liked = !m.getFirstFile().is_liked;
                m.getSecondFile().is_liked = !m.getFirstFile().is_liked;
                if (m.getFirstFile().is_liked) {
                    m.incrementFirstFileVoting();
                    m.decrementSecondFileVoting();
                } else {
                    m.incrementSeconfFileVoting();
                    m.decrementFirstFileVoting();
                }
                m.getFirstFile().itemPostion = position;
                voteClickCallback.onclick(v, m.getFirstFile());
                notifyItemChanged(position);
            });
            holder.b.pollVoteRight.setOnClickListener(v -> {
                if (m.getFirstFile().is_liked || m.getSecondFile().is_liked) return;

                m.getSecondFile().is_liked = !m.getSecondFile().is_liked;
                m.getFirstFile().is_liked = !m.getSecondFile().is_liked;
                m.getSecondFile().itemPostion = position;
                if (m.getSecondFile().is_liked) {
                    m.incrementSeconfFileVoting();
                    m.decrementFirstFileVoting();
                } else {
                    m.incrementFirstFileVoting();
                    m.decrementSecondFileVoting();
                }
                voteClickCallback.onclick(v, m.getSecondFile());
                notifyItemChanged(position);
            });
            Glide.with(getContext()).asBitmap().load(m.getFirstFile().message_file).into(holder.b.pollImageLeft);
            Glide.with(getContext()).asBitmap().load(m.getSecondFile().message_file).into(holder.b.pollImageRight);
        } else if (m.isImage()) {
            holder.b.pollCv.setVisibility(View.GONE);
            holder.b.seekBarView.setVisibility(View.GONE);
            holder.b.imageCv.setVisibility(View.VISIBLE);
            holder.b.messageText.setVisibility(View.VISIBLE);
            MessageFiles file = m.getFirstFile();
            holder.b.messageText.setText(m.getMessageBody());
            Glide.with(getContext()).asBitmap().load(file.message_file).into(holder.b.img);

            holder.b.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                            .load(m.getFirstFile().message_file)
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
        } else if (m.isVoice()) {
            holder.b.pollCv.setVisibility(View.GONE);
            holder.b.seekBarView.setVisibility(View.VISIBLE);
            holder.b.imageCv.setVisibility(View.GONE);
            holder.b.messageText.setVisibility(View.GONE);
            holder.b.startVoiceImageView.setOnClickListener(v -> {
                seekBarUtil = SeekBarUtil.newInstance(holder.b.seekBar.toString(), this);
                currentSeekBar = holder.b.seekBar;
                currentSeekTime = holder.b.seekTime;
                currentPlayBtn = holder.b.startVoiceImageView;
                currentPlayBtn.requestFocus();
                if (seekBarUtil.isPlayin()) {
                    seekBarUtil.pauseMediaPlayer();
                    holder.b.startVoiceImageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_play_tran));
                } else {
                    holder.b.startVoiceImageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_pause_trans));
                    seekBarUtil.startmediaPlayer(m.getFirstFile().message_file);
                }
            });
        } else if (m.isText()) {
            holder.b.pollCv.setVisibility(View.GONE);
            holder.b.seekBarView.setVisibility(View.GONE);
            holder.b.imageCv.setVisibility(View.GONE);
            holder.b.messageText.setVisibility(View.VISIBLE);
            holder.b.messageText.setText(m.getMessageBody());
        }

        holder.b.name.setText(m.getMessageSender().getFullName());
        holder.b.time.setText(ElapsedTime.getFromDate(new Date(m.getCreatedAt())));
        holder.b.otherUserImage.setOnClickListener(v -> {
            if (MyServiceInterceptor.userId == m.getMessageSender().getId()) return;
            if (!prefHelper.shouldShowProfile()) {
                Toast.makeText(getContext(), "profile is private", Toast.LENGTH_SHORT).show();
            } else
                UserProfileActivity.start(getContext(), m.getMessageSender().getId());
        });

    }

    @Override
    public int getItemCount() {
        return messageList == null ? 0 : messageList.size();
    }

    private static DiffUtil.ItemCallback<Message> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Message>() {
                @Override
                public boolean areItemsTheSame(@NonNull Message oldMessage,
                                               @NonNull Message newMessage) {
                    return oldMessage.getId() == newMessage.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Message oldMessage,
                                                  @NonNull Message newMessage) {
                    return oldMessage.equals(newMessage);
                }
            };

    @Override
    public void setSeekMax(int max) {
        currentSeekBar.setMax(max);
    }

    @Override
    public void setprogress(int playerCurrentPosition) {
        currentSeekBar.setProgress(playerCurrentPosition);
        Log.d("MessageAdapter", "setprogress: " + playerCurrentPosition);

    }

    @Override
    public void updateSeekTv(String currentSec) {
        currentSeekTime.setText(currentSec);
        Log.d("MessageAdapter", "updateSeekTv: " + currentSec);
    }

    @Override
    public void finishSeekBar() {
        Log.d("MessageAdapter", "finishSeekBar: ");

        currentPlayBtn.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_play_tran));
        currentSeekTime.setText("00:00");
        currentSeekBar.setProgress(0);
        seekBarUtil.flush();
    }

    @Override
    public void callHearVoiceApi() {

    }
}
