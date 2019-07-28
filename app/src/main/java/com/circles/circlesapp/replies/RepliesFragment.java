package com.circles.circlesapp.replies;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import com.circles.circlesapp.R;
import com.circles.circlesapp.addgroup.MessageEvent;
import com.circles.circlesapp.databinding.RepiesFragmentBinding;
import com.circles.circlesapp.helpers.utilities.CustomOnClickListener;

public class RepliesFragment extends Fragment implements CustomOnClickListener<ReplayModel> {
    private int commentId;
    RepiesFragmentBinding b;

    public static RepliesFragment newInstance(int commentId) {
        Bundle args = new Bundle();
        args.putInt("commentId", commentId);
        RepliesFragment fragment = new RepliesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            commentId = getArguments().getInt("commentId", 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.repies_fragment, container, false);
        RepliesViewModel viewModel = ViewModelProviders.of(this).get(RepliesViewModel.class);
        viewModel.getReplies(commentId);
        viewModel.mutableLDResp.observe(this, list -> {
            if (list != null) {
                RepliesAdapter adapter = new RepliesAdapter(this,list);
                b.commentsRv.setLayoutManager(new LinearLayoutManager(getContext()));
                b.commentsRv.setAdapter(adapter);
                adapter.submitList(list);
                adapter.notifyDataSetChanged();
            }
        });
        b.addReplayCv.setOnClickListener(view -> {
            MessageEvent event = new MessageEvent();
            event.setReqCode(1996);
            event.setPostId(commentId);
            EventBus.getDefault().postSticky(event);
        });
        return b.getRoot();
    }

    @Override
    public void onclick(View v, ReplayModel model) {

    }
}
