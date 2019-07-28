package com.circles.circlesapp.settings.viewModels.userProfile;



import android.view.View;

import com.circles.circlesapp.settings.callBacks.UserProfileCallBack;
import com.circles.circlesapp.settings.viewModels.base.BaseVmodel;

public interface UserProfileVmodel<v extends UserProfileCallBack> extends BaseVmodel<v> {
    void reqUserProfile(int userId);
    void follow(View view);
    void retry(View view);
}
