package com.circles.circlesapp.profile;



import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.circles.circlesapp.R;
import com.circles.circlesapp.chatlist.ChatRoom;
import com.circles.circlesapp.databinding.UserProfileLayoutBinding;
import com.circles.circlesapp.helpers.core.ApiResponseCallBack;
import com.circles.circlesapp.helpers.core.Constants;
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor;
import com.circles.circlesapp.messaging.view.MessagingActivity;
import com.circles.circlesapp.profile.model.FollowerList;
import com.circles.circlesapp.retrofit.RetrofitClient;
import com.circles.circlesapp.settings.callBacks.UserProfileCallBack;
import com.circles.circlesapp.settings.viewModels.userProfile.UserProfileViewModel;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class UserProfileFragment extends Fragment implements UserProfileCallBack, View.OnClickListener {
    private static final String USERID = "userId";
    private UserProfileLayoutBinding layoutBinding;
    private UserProfileViewModel viewModel;
    private int followerNum = 0;
    private int follownigNum = 0;
    private ProgressDialog progressDialog;
    private UserPostsFragment fragment;

    public static void start(Context context, int userId) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra(USERID, userId);
        context.startActivity(intent);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = DataBindingUtil.inflate(inflater, R.layout.user_profile_layout, container, false);
        return layoutBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(UserProfileViewModel.class);
        viewModel.attachView(this);
        viewModel.reqUserProfile(getActivity().getIntent().getIntExtra(USERID, 0));
        layoutBinding.setUserProfile(viewModel);
        fragment = (UserPostsFragment) getChildFragmentManager().findFragmentById(R.id.posts_user);
        reqPosts();


        layoutBinding.followers.setOnClickListener(this);
        layoutBinding.following.setOnClickListener(this);

        fragment.getRecyclerView().setPadding(0, 0, 0, 0);
        layoutBinding.sendMsg.setOnClickListener(v -> {
            AndroidNetworking.post(RetrofitClient.BASE_URL + "getUserRoomDetails")
                    .addHeaders("Authorization", MyServiceInterceptor.getAuth())
                    .addHeaders("Accept", "application/json ")
                    .addBodyParameter("user_id", getActivity().getIntent().getIntExtra(USERID, 0) + "")
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject data = new JSONObject(response).getJSONObject("data");
                                ChatRoom chatRoom = new Gson().fromJson(data.toString(), ChatRoom.class);
                                UserInfo o = (UserInfo) viewModel.getUserInfo().get();
                                chatRoom.title = o.getFirst_name() + " " + o.getLast_name();
                                Intent intent = new Intent(getContext(), MessagingActivity.class);
                                intent.putExtra(Constants.CHAT_ROOM_KEY, chatRoom);
                                startActivity(intent);
                            } catch (Exception s) {

                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            //     Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    public void reqPosts() {
        fragment.userPosts(MyServiceInterceptor.getAuth(), getActivity().getIntent().getIntExtra(USERID, 0), new UserPostsFragment.postsCallBack() {

            @Override
            public void start() {
                viewModel.setLoader(true);
                viewModel.setError(null);
            }

            @Override
            public void sucess() {
                viewModel.setLoader(false);
            }

            @Override
            public void failed(@NotNull String msg) {
                viewModel.setLoader(false);
                viewModel.setError(msg);
            }
        });
    }

    @Override
    public UserProfileFragment getFragment() {
        return this;
    }

    @Override
    public void onError(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.followers:

                getFollowerList();
                break;

            case R.id.following:

                getFollowingList();
                break;
        }
    }

    private void getFollowerList() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("loading .....");
        ProfileRepo d = new ProfileRepo();
        d.getUserFollower(new ApiResponseCallBack<FollowerList>() {
            @Override
            public void success(FollowerList s) {
                progressDialog.dismiss();
                if (s != null) {
                    Gson gson = new Gson();
                    String s1 = gson.toJson(s);
                    showFragment(FollowerOrFollowingFragment.follInstance(s1, "follower"));
                }
            }

            @Override
            public void fail(String message) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Failed to get data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getFollowingList() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("loading .....");
        ProfileRepo d = new ProfileRepo();
        d.getUserFollowing(new ApiResponseCallBack<FollowerList>() {
            @Override
            public void success(FollowerList s) {
                progressDialog.dismiss();
                if (s != null) {
                    Gson gson = new Gson();
                    String s1 = gson.toJson(s);
                    showFragment(FollowerOrFollowingFragment.follInstance(s1, "following"));
                }
            }

            @Override
            public void fail(String message) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Failed to get data", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void showFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, fragment, "rageComicList")
                .addToBackStack(null)
                .commit();
    }
}
