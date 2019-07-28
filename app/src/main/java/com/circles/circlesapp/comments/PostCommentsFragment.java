package com.circles.circlesapp.comments;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import com.circles.circlesapp.R;
import com.circles.circlesapp.addgroup.MessageEvent;
import com.circles.circlesapp.databinding.CommentsFragmentBinding;
import com.circles.circlesapp.helpers.utilities.CustomOnClickListener;
import com.circles.circlesapp.replies.RepliesFragment;

public class PostCommentsFragment extends Fragment implements CustomOnClickListener<PostCommentMode> {
    private int postId;
    CommentsFragmentBinding b;

    public static PostCommentsFragment newInstance(int postId,String sharesCount,String hearedcount) {
        Bundle args = new Bundle();
        args.putInt("postId", postId);
        args.putString("sharesCount", sharesCount);
        args.putString("hearedcount", hearedcount);
        PostCommentsFragment fragment = new PostCommentsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postId = getArguments().getInt("postId", 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.comments_fragment, container, false);
        CommentsViewModel viewModel = ViewModelProviders.of(this).get(CommentsViewModel.class);
        viewModel.getPostComments(postId);
        b.commentsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        Bundle bundle = getArguments();
        if(bundle!=null){
            String hearedcount = bundle.getString("hearedcount");
            String sharesCount = bundle.getString("sharesCount");
            b.hearedTv.setText("Heared :"+hearedcount);
            b.sharesTv.setText("shared :"+sharesCount);
        }
        viewModel.mutableLDResp.observe(this, list -> {
            if (list != null) {
                PostCommentsAdapter adapter = new PostCommentsAdapter(this, list);
                b.commentsRv.setAdapter(adapter);
                b.commentsTv.setText("Comments:" + list.size());

                adapter.notifyDataSetChanged();
            }else {
                b.commentsTv.setText("Comments:" + 0);
            }

        });
        b.backBtn.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        b.addCommentTv.setOnClickListener(v -> {
            MessageEvent event = new MessageEvent();
            event.setReqCode(1995);
            event.setPostId(postId);
            EventBus.getDefault().postSticky(event);
        });
        return b.getRoot();
    }

    @Override
    public void onclick(View v, PostCommentMode model) {
        if (getActivity() != null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.add(android.R.id.content, RepliesFragment.newInstance(model.getCommentId()), "")
                    .addToBackStack("RepliesFragment").commit();
        }
    }
}
