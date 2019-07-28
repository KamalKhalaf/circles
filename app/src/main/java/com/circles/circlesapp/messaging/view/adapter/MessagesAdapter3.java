package com.circles.circlesapp.messaging.view.adapter;
/**/

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.circles.circlesapp.R;
import com.circles.circlesapp.helpers.MySeekBarCallback;
import com.circles.circlesapp.helpers.SeekBarUtil;
import com.circles.circlesapp.helpers.SharedPrefHelper;
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

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter3 extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements MySeekBarCallback {

    private static final int INCOME = 1;
    private static final int OUTGOING = 2;

    private List<Message> messageList;
    private CustomOnClickListener<MessageFiles> voteClickCallback;
    private SeekBar currentSeekBar;
    private TextView currentSeekTime;
    private ImageView currentPlayBtn;
    private SeekBarUtil seekBarUtil;
    private SharedPrefHelper prefHelper;
    private Context context;


    public MessagesAdapter3(Context context, List<Message> messages, CustomOnClickListener<MessageFiles> voteClickCallback) {
        this.context = context;
        this.messageList = messages;
        this.voteClickCallback = voteClickCallback;
    }

    /**
     * This method is used to specify which view inflate in onCreateViewHolder() method
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (!this.messageList.get(position).getMessageSender().isCurrentuser()) {
            return INCOME;
        } else {
            return OUTGOING;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater;
        View view = null;
        switch (viewType) {
            case INCOME:
                inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.other_user_message_item2, parent, false);
                break;
            case OUTGOING:
                inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.other_user_message_item_outgoing, parent, false);
                break;
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder b = (ViewHolder) holder;
        if (prefHelper == null) prefHelper = new SharedPrefHelper(context);
        Message m = messageList.get(position);
        if (m.getMessageSender().isCurrentuser()) {

            b.pollCv.setBackground(context.getResources().getDrawable(R.drawable.rounded_primary_color_backg));
            b.name.setTextColor(context.getResources().getColor(R.color.white));
            b.messageText.setTextColor(context.getResources().getColor(R.color.white));
            b.totlaFeesTv.setTextColor(context.getResources().getColor(R.color.white));
            b.otherUserImage.setVisibility(View.GONE);
            b.myImage.setVisibility(View.GONE);
            if (m.getMessageSender().getProfileImage() == null || m.getMessageSender().getProfileImage().length() < 3)
                b.myImage.setImageDrawable(context.getResources().getDrawable(R.drawable.sign_up_profile));
            else
                Glide.with(context).asBitmap().load(m.getMessageSender().getProfileImage()).into(b.myImage);
        } else {

            b.pollCv.setBackground(context.getResources().getDrawable(R.drawable.rounded_white_backg));
            b.name.setTextColor(context.getResources().getColor(R.color.black));
            b.messageText.setTextColor(context.getResources().getColor(R.color.black));
            b.totlaFeesTv.setTextColor(context.getResources().getColor(R.color.black));
            b.otherUserImage.setVisibility(View.VISIBLE);
            b.myImage.setVisibility(View.GONE);
            if (m.getMessageSender().getProfileImage() == null || m.getMessageSender().getProfileImage().length() < 3) {
                b.myImage.setImageDrawable(context.getResources().getDrawable(R.drawable.avatar));
            } else
                Glide.with(context).asBitmap().load(m.getMessageSender().getProfileImage()).into(b.otherUserImage);

        }

        //regionImageNotVoting
        if (m.isImage() && !m.getType().equals("voting")) {

            RequestOptions requestOptions = new RequestOptions();
            RequestOptions myOptions = new RequestOptions()
                    .centerCrop();

            RequestOptions myOptionsList = new RequestOptions()
                    .override(500, 500)
                    .centerCrop();


            b.imageCv.setVisibility(View.VISIBLE);
            b.alyVideo.setVisibility(View.GONE);
            b.seekBarView.setVisibility(View.GONE);
            b.pollCv.setVisibility(View.GONE);
            b.layoutMsgData.setVisibility(View.GONE);

            MessageFiles file = m.getFirstFile();
            b.messageText.setText(m.getMessageBody());

            if (m.getFiles().size() > 1) {
                b.rvPhotos.setVisibility(View.VISIBLE);
                b.singLay.setVisibility(View.GONE);

                b.rvPhotos.setHasFixedSize(true);
                b.rvPhotos.setLayoutManager(new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false));
                PhotoAdapter photoAdapter = new PhotoAdapter(m.getFiles(), context);
                b.rvPhotos.setAdapter(photoAdapter);

            } else {
                b.rvPhotos.setVisibility(View.GONE);
                b.singLay.setVisibility(View.VISIBLE);

                if (m.getFiles().get(0).message_file != null && !m.getFiles().get(0).message_file.equals("")) {
                    Glide.with(context).applyDefaultRequestOptions(requestOptions).asBitmap()
                            .apply(myOptionsList).load(m.getFiles().get(0).message_file)
                            .listener(new RequestListener<Bitmap>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                    b.progressBarload2.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                    b.progressBarload2.setVisibility(View.GONE);
                                    return false;
                                }
                            }).into(b.img);

                } else {
                    b.progressBarload2.setVisibility(View.GONE);
                    Glide.with(context).load(R.drawable.sign_up_profile).into(b.img);
                }


                b.img.setOnClickListener(new View.OnClickListener() {
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

            }


//            Glide.with(getContext()).asBitmap().load(file.message_file).into(b.img);
//

        }
        //endregion

        //regionVideo
        else if (m.isVideo()) {
            b.pollCv.setVisibility(View.GONE);
            b.alyVideo.setVisibility(View.VISIBLE);
            b.seekBarView.setVisibility(View.GONE);
            b.imageCv.setVisibility(View.GONE);
            b.layoutMsgData.setVisibility(View.GONE);


            if (m.getFiles().size() > 0) {

                b.mpwVideoPlayer.setUp(
                        m.getFiles().get(0).message_file,
                        "video", Jzvd.SCREEN_NORMAL);
//                Glide.with(holder.jzvdStd.getContext()).load(VideoConstant.videoThumbs[0][position]).into(holder.jzvdStd.thumbImageView);


//                b.mpwVideoPlayer.autoStartPlay(m.getFiles().get(0).message_file, MxVideoPlayer.SCREEN_LAYOUT_LIST,
//                        "name");
//
//                Glide.with(context)
//                        .load("http://img3.yytcdn.com/video/mv/140108/850708/D81901436FF172396A44128BAC8C3707_240x135.jpeg")
//                        .into(b.mpwVideoPlayer.mThumbImageView);
//
//                b.mpwVideoPlayer.setOnClickListener(v -> {
//
//                    BroadcastHelper.sendInform(context, "play_video", m.getFiles().get(0).message_file);
//
//                });
            }


        }
        //endregion

        //regionIsPoll
        else if (m.isPoll()) {
            b.pollCv.setVisibility(View.VISIBLE);
            b.alyVideo.setVisibility(View.GONE);
            b.seekBarView.setVisibility(View.GONE);
            b.alyVideo.setVisibility(View.GONE);
            b.imageCv.setVisibility(View.GONE);
            b.layoutMsgData.setVisibility(View.GONE);
            b.totlaFeesTv.setText("Total votes :" + m.getTotalVotes() + "");
            b.pollQuestionTv.setText(m.getMessageBody());
            b.percentageLeft.setText(m.getFirstPercentage() + "");
            b.percentageRight.setText(m.getSecondPercentage() + "");
            if (m.getFirstFile().is_liked) {
                b.pollVote.setImageDrawable(context.getResources().getDrawable(R.drawable.poll_liked));
            } else {
                b.pollVote.setImageDrawable(context.getResources().getDrawable(R.drawable.poll_not_liked));

            }
            if (m.getSecondFile().is_liked) {
                b.pollVoteRight.setImageDrawable(context.getResources().getDrawable(R.drawable.poll_liked));
            } else {
                b.pollVoteRight.setImageDrawable(context.getResources().getDrawable(R.drawable.poll_not_liked));
            }
            b.pollVote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                    voteClickCallback.onclick(view, m.getFirstFile());
                    notifyItemChanged(position);

                }
            });
            b.pollVoteRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                    voteClickCallback.onclick(view, m.getSecondFile());
                    notifyItemChanged(position);
                }
            });
            Glide.with(context).asBitmap().load(m.getFirstFile().message_file).into(b.pollImageLeft);
            Glide.with(context).asBitmap().load(m.getSecondFile().message_file).into(b.pollImageRight);

            b.pollImageLeft.setOnClickListener(new View.OnClickListener() {
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

            b.pollImageRight.setOnClickListener(new View.OnClickListener() {
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
                            .load(m.getSecondFile().message_file)
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
        }
        //endregion

        //regionVoice
        else if (m.isVoice()) {
            b.pollCv.setVisibility(View.GONE);
            b.alyVideo.setVisibility(View.GONE);
            b.seekBarView.setVisibility(View.VISIBLE);
            b.imageCv.setVisibility(View.GONE);
            b.layoutMsgData.setVisibility(View.GONE);
            b.startVoiceImageView.setOnClickListener(v -> {
                seekBarUtil = SeekBarUtil.newInstance(b.seekBar.toString(), this);
                currentSeekBar = b.seekBar;
                currentSeekTime = b.seekTime;
                currentPlayBtn = b.startVoiceImageView;
                currentPlayBtn.requestFocus();
                if (seekBarUtil.isPlayin()) {
                    seekBarUtil.pauseMediaPlayer();
                    b.startVoiceImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play_tran));
                } else {
                    b.startVoiceImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pause_trans));
                    seekBarUtil.startmediaPlayer(m.getFirstFile().message_file);
                }
            });
        }
        //endregion

        //regionText
        else if (m.isText()) {
            b.pollCv.setVisibility(View.GONE);
            b.alyVideo.setVisibility(View.GONE);
            b.seekBarView.setVisibility(View.GONE);
            b.imageCv.setVisibility(View.GONE);
            b.layoutMsgData.setVisibility(View.VISIBLE);
            b.messageText.setText(m.getMessageBody());
        }

        //endregion

        b.name.setText(m.getMessageSender().getFullName());
        b.time.setText(ElapsedTime.getFromDate(new Date(m.getCreatedAt())));
        b.otherUserImage.setOnClickListener(v -> {
            if (MyServiceInterceptor.userId == m.getMessageSender().getId()) return;
            if (!prefHelper.shouldShowProfile()) {
                Toast.makeText(context, "profile is private", Toast.LENGTH_SHORT).show();
            } else
                UserProfileActivity.start(context, m.getMessageSender().getId());
        });


    }


    @Override
    public int getItemCount() {
        return messageList == null ? 0 : messageList.size();
    }

    /**
     * Class responsible for binding the elements of rsc_chat_cavans_msg_xxx layout file
     */
    private class ViewHolder extends RecyclerView.ViewHolder {


        private ConstraintLayout pollCv;
        private ConstraintLayout seekBarView;

        private LinearLayout alyVideo;
        private LinearLayout imageCv;
        private LinearLayout layoutMsgData;

        private RelativeLayout singLay;

        private TextView time;
        private TextView name;
        private TextView messageText;
        private TextView totlaFeesTv;
        private TextView pollQuestionTv;
        private TextView percentageLeft;
        private TextView percentageRight;
        private TextView seekTime;

        private CircleImageView otherUserImage;
        private CircleImageView myImage;

        private RecyclerView rvPhotos;

        private ProgressBar progressBarload2;
        private ProgressBar progressBarload;

        private ImageView pollVote;
        private ImageView pollVoteRight;
        private ImageView pollImageLeft;
        private ImageView pollImageRight;
        private ImageView startVoiceImageView;
        private ImageView img;

        JzvdStd mpwVideoPlayer;
        //        private MxVideoPlayerWidget mpwVideoPlayer;
        private SeekBar seekBar;


        public ViewHolder(View v) {
            super(v);
            this.pollCv = (ConstraintLayout) v.findViewById(R.id.pollCv);
            this.seekBarView = (ConstraintLayout) v.findViewById(R.id.seekBarView);

            this.alyVideo = (LinearLayout) v.findViewById(R.id.alyVideo);
            this.imageCv = (LinearLayout) v.findViewById(R.id.imageCv);
            this.layoutMsgData = (LinearLayout) v.findViewById(R.id.layoutMsgData);

            this.singLay = (RelativeLayout) v.findViewById(R.id.singLay);

            this.rvPhotos = (RecyclerView) v.findViewById(R.id.rvPhotos);

            this.progressBarload2 = (ProgressBar) v.findViewById(R.id.progressBarload2);
            this.progressBarload = (ProgressBar) v.findViewById(R.id.progressBarload);

            this.mpwVideoPlayer = v.findViewById(R.id.mpw_video_player);

            this.time = (TextView) v.findViewById(R.id.time);
            this.name = (TextView) v.findViewById(R.id.name);
            this.messageText = (TextView) v.findViewById(R.id.messageText);


            this.pollQuestionTv = (TextView) v.findViewById(R.id.pollQuestionTv);
            this.percentageLeft = (TextView) v.findViewById(R.id.percentageLeft);
            this.percentageRight = (TextView) v.findViewById(R.id.percentageRight);
            this.totlaFeesTv = (TextView) v.findViewById(R.id.totlaFeesTv);
            this.seekTime = (TextView) v.findViewById(R.id.seekTime);

            this.otherUserImage = (CircleImageView) v.findViewById(R.id.other_user_image);
            this.myImage = (CircleImageView) v.findViewById(R.id.my_image);

            this.seekBar = (SeekBar) v.findViewById(R.id.seekBar);

            this.pollVote = (ImageView) v.findViewById(R.id.poll_vote);
            this.startVoiceImageView = (ImageView) v.findViewById(R.id.startVoiceImageView);
            this.pollVoteRight = (ImageView) v.findViewById(R.id.poll_vote_right);
            this.pollImageLeft = (ImageView) v.findViewById(R.id.poll_imageLeft);
            this.img = (ImageView) v.findViewById(R.id.img);
            this.pollImageRight = (ImageView) v.findViewById(R.id.poll_imageRight);
        }
    }

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

        currentPlayBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play_tran));
        currentSeekTime.setText("00:00");
        currentSeekBar.setProgress(0);
        seekBarUtil.flush();
    }

    @Override
    public void callHearVoiceApi() {

    }
}
