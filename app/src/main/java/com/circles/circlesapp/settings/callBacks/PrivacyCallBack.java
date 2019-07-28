package com.circles.circlesapp.settings.callBacks;



import androidx.navigation.NavController;
import com.circles.circlesapp.settings.ui.PrivacyFragment;

public interface PrivacyCallBack extends BaseCallBack {
    NavController getNavController();
    PrivacyFragment getFragment();
}
