package com.circles.circlesapp.messaging.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.circles.circlesapp.phase2.views.ui.ChatMessagingFragment;
import com.circles.circlesapp.R;
import com.circles.circlesapp.chatlist.ChatRoom;
import com.circles.circlesapp.databinding.ActivityMessagingBinding;
import com.circles.circlesapp.helpers.SharedPrefHelper;
import com.circles.circlesapp.helpers.core.Constants;
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor;
import com.circles.circlesapp.helpers.ui.ActivityUtils;
import com.circles.circlesapp.messaging.MessagingOptionsCallback;
import com.circles.circlesapp.profile.UserProfileActivity;

import java.util.List;

public class MessagingActivity extends AppCompatActivity implements MessagingOptionsCallback {

    private SharedPrefHelper prefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMessagingBinding b = DataBindingUtil.setContentView(this, R.layout.activity_messaging);
        setSupportActionBar(b.toolbar.toolbar);
        b.toolbar.back.setOnClickListener(v -> {
            onBackPressed();
        });
        if (prefHelper == null) prefHelper = new SharedPrefHelper(this);
        if (getIntent() != null) {
            ChatRoom chatRoom = getIntent().getParcelableExtra(Constants.CHAT_ROOM_KEY);
            ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                    ChatMessagingFragment.newInstance(chatRoom),
                    R.id.frameLayout, false);
            b.toolbar.title.setText(chatRoom.title);
            if (chatRoom.image != null && !chatRoom.image.equals("")) {
                Glide.with(this).load(chatRoom.image).addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        b.toolbar.progressBarload.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        b.toolbar.progressBarload.setVisibility(View.GONE);
                        return false;
                    }
                }).into(b.toolbar.userPic);
            } else {
                b.toolbar.progressBarload.setVisibility(View.GONE);
                Glide.with(this).load(R.drawable.sign_up_profile).into(b.toolbar.userPic);
            }
            b.toolbar.userPic.setOnClickListener(v -> {
                if (chatRoom.id != 0 && !chatRoom.is_group) {
                    if (MyServiceInterceptor.userId == chatRoom.id) return;
                    if (!prefHelper.shouldShowProfile()) {
                        Toast.makeText(this, "profile is private", Toast.LENGTH_SHORT).show();
                    } else
                        UserProfileActivity.start(this, chatRoom.id);
                }
            });
        }


    }

    @Override
    public void sendImageOrVideo() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (int i = 0; i < fragments.size(); i++) {
            if (fragments.get(i) instanceof ChatMessagingFragment) {
                ChatMessagingFragment f = (ChatMessagingFragment) fragments.get(i);
                f.sendImageOrVideo();
            }
        }
    }

    @Override
    public void sendFiles() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (int i = 0; i < fragments.size(); i++) {
            if (fragments.get(i) instanceof ChatMessagingFragment) {
                ChatMessagingFragment f = (ChatMessagingFragment) fragments.get(i);
                f.sendFiles();
            }
        }
    }

    @Override
    public void crestePoll() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (int i = 0; i < fragments.size(); i++) {
            if (fragments.get(i) instanceof ChatMessagingFragment) {
                ChatMessagingFragment f = (ChatMessagingFragment) fragments.get(i);
                f.createPoll();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (int i = 0; i < fragments.size(); i++) {
            if (fragments.get(i) instanceof PollFragment) {
                PollFragment f = (PollFragment) fragments.get(i);
                f.onActivityResult(requestCode, resultCode, data);
            } else if (fragments.get(i) instanceof ChatMessagingFragment) {
                ChatMessagingFragment f = (ChatMessagingFragment) fragments.get(i);
                f.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
