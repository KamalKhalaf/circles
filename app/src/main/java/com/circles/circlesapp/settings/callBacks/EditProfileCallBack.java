package com.circles.circlesapp.settings.callBacks;

/*
 * Created By mabrouk on 29/01/19
 * com.circles
 */

import androidx.navigation.NavController;
import com.circles.circlesapp.settings.ui.EditProfileFragment;

public interface EditProfileCallBack extends BaseCallBack {
  EditProfileFragment getFragment();
  NavController getNavController();

    void changeImage();
}
