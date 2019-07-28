package com.circles.circlesapp.settings.viewModels.privacy;



import android.view.View;

import com.circles.circlesapp.settings.callBacks.PrivacyCallBack;
import com.circles.circlesapp.settings.viewModels.base.BaseVmodel;

public interface PrivacyVmodel<v extends PrivacyCallBack> extends BaseVmodel<v> {
    void age_location(View view);
    void following(View view);
    void privateAccount(View view);
    void confirmChanges(View view);
    void back(View view);
    void reqPrivacy();
    void reqUpdatePrivacy();
    void setUpProgress();
}
