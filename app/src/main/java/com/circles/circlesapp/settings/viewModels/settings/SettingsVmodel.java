package com.circles.circlesapp.settings.viewModels.settings;

/*
 * Created By mabrouk on 28/01/19
 * com.circles
 */

import android.view.View;

import com.circles.circlesapp.settings.callBacks.SettingsCallBack;
import com.circles.circlesapp.settings.viewModels.base.BaseVmodel;

public interface SettingsVmodel<v extends SettingsCallBack> extends BaseVmodel<v> {
    void editProfile(View view);
    void privacy(View view);
    void changeLanguage(View view);
    void logout(View view);
    void socialMedia(View view);
    void back(View view);
}
