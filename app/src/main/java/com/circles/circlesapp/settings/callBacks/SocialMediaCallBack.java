package com.circles.circlesapp.settings.callBacks;



import com.circles.circlesapp.settings.ui.SocialAccountsFragment;

import androidx.navigation.NavController;

public interface SocialMediaCallBack extends BaseCallBack {
    SocialAccountsFragment getFragment();
    NavController getNavController();
}
