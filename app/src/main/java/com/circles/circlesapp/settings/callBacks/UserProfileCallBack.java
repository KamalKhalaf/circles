package com.circles.circlesapp.settings.callBacks;



import com.circles.circlesapp.profile.UserProfileFragment;

public interface UserProfileCallBack extends BaseCallBack {
    UserProfileFragment getFragment();
    void onError(String msg);
    void reqPosts();
}
