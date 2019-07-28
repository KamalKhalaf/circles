package com.circles.circlesapp.group;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.circles.circlesapp.R;
import com.circles.circlesapp.chatlist.ChatRoom;
import com.circles.circlesapp.databinding.ActivityGroupBinding;
import com.circles.circlesapp.group.groupMember.GroupMemeberFragment;
import com.circles.circlesapp.helpers.core.ApiResponseCallBack;
import com.circles.circlesapp.helpers.ui.ActivityUtils;
import com.circles.circlesapp.messaging.MessagingOptionsCallback;
import com.circles.circlesapp.messaging.view.MessagingFragment2;
import com.circles.circlesapp.messaging.view.PollFragment;
import com.circles.circlesapp.nearby.JoinGroupResponse;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;

public class GroupActivity extends AppCompatActivity implements MessagingOptionsCallback, ApiResponseCallBack<GroupDetail> {
    ActivityGroupBinding b;
    private Repo repo;
    private ChatRoom chatRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_group);
        repo = new Repo();
        setSupportActionBar(b.toolbar.toolbar);
        b.toolbar.toolbar.setNavigationIcon(R.drawable.back);
        b.toolbar.toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        if (getIntent() != null) {
            JoinGroupResponse extra = getIntent().getParcelableExtra("data");
            chatRoom = new ChatRoom(extra.groupName, extra.channel, extra.room, extra.id, extra.latitude, extra.longitude);
            chatRoom.is_group=true;
            b.toolbar.title.setText(extra.groupName);
            ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                    MessagingFragment2.newInstance(chatRoom),
                    R.id.frameLayout, false);
            b.groupNameTv.setText(chatRoom.title);
            repo.getGroupChatDetail(this, extra.id);
        }
        b.groupDesc.setOnClickListener(v -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.frameLayout, GroupMemeberFragment.getInstance(chatRoom.title,chatRoom.room), "").addToBackStack(null).commit();
        });
    }

    @Override
    public void sendImageOrVideo() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (int i = 0; i < fragments.size(); i++) {
            if (fragments.get(i) instanceof MessagingFragment2) {
                MessagingFragment2 f = (MessagingFragment2) fragments.get(i);
                f.sendImageOrVideo();
            }
        }
    }

    @Override
    public void sendFiles() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (int i = 0; i < fragments.size(); i++) {
            if (fragments.get(i) instanceof MessagingFragment2) {
                MessagingFragment2 f = (MessagingFragment2) fragments.get(i);
                f.sendFiles();
            }
        }
    }

    @Override
    public void crestePoll() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (int i = 0; i < fragments.size(); i++) {
            if (fragments.get(i) instanceof MessagingFragment2) {
                MessagingFragment2 f = (MessagingFragment2) fragments.get(i);
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
            } else if (fragments.get(i) instanceof MessagingFragment2) {
                MessagingFragment2 f = (MessagingFragment2) fragments.get(i);
                f.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void success(GroupDetail groupDetail) {
        b.groupNameTv.setText(groupDetail.name);
        b.groupSlider.setIndicatorAnimation(SliderLayout.Animations.FILL); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        b.groupSlider.setScrollTimeInSec(3); //set scroll delay in seconds
        for (int i = 0; i < groupDetail.getCovers().size(); i++) {
            SliderView sliderView = new SliderView(this);
            sliderView.setImageUrl(groupDetail.getCovers().get(i));
            b.groupSlider.addSliderView(sliderView);
        }

    }

    @Override
    public void fail(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
