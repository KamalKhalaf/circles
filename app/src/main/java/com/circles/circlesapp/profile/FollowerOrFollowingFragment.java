package com.circles.circlesapp.profile;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.FragmentFollowerOrFollowingBinding;
import com.circles.circlesapp.profile.model.FollowerList;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowerOrFollowingFragment extends Fragment {

    FragmentFollowerOrFollowingBinding mBinding;
    private FollowerList folloList;
    ArrayList<FollowerList.DataBean> dataBeans;
    private FollowAdapter adapter;
    private String type;
    Gson gson;

    public static FollowerOrFollowingFragment follInstance(String data, String type) {
        FollowerOrFollowingFragment fragment = new FollowerOrFollowingFragment();
        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    public FollowerOrFollowingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_follower_or_following, container, false);
        mBinding.setLifecycleOwner(this);
        gson = new Gson();

        dataBeans = new ArrayList<>();
        adapter = new FollowAdapter(getActivity(), dataBeans);
        if (getArguments() != null) {
            if (getArguments().getString("data") != null) {
                folloList = gson.fromJson(getArguments().getString("data"), FollowerList.class);
                type = getArguments().getString("type");
            }
        }

        if (type != null && type.equals("following")) {
            mBinding.tvTittle.setText("following");
        } else if (type != null && type.equals("follower")) {
            mBinding.tvTittle.setText("follower");
        }


        mBinding.followRecycler.setHasFixedSize(true);
        mBinding.followRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false));
        mBinding.followRecycler.setAdapter(adapter);


        if (folloList != null && folloList.getData().size() > 0) {

            adapter = new FollowAdapter(getActivity(), folloList.getData());
            adapter.notifyDataSetChanged();
            mBinding.followRecycler.setAdapter(adapter);
        }


        mBinding.ibCloseDialog.setOnClickListener(view -> {
            FragmentActivity activity = getActivity();
            if (activity == null) return;
            activity.onBackPressed();
        });


        return mBinding.getRoot();

    }
}
