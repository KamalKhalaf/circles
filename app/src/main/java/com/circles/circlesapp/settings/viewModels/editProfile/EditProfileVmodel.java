package com.circles.circlesapp.settings.viewModels.editProfile;

/*
 * Created By mabrouk on 29/01/19
 * com.circles
 */

import android.view.View;

import java.text.ParseException;

import com.circles.circlesapp.settings.UserModel;
import com.circles.circlesapp.settings.callBacks.EditProfileCallBack;
import com.circles.circlesapp.settings.viewModels.base.BaseVmodel;

public interface EditProfileVmodel<v extends EditProfileCallBack> extends BaseVmodel<v> {
    void submit(View view);
    void back(View view);
    void male(View view);
    void female(View view);
    void chooseDate(View  view);
    void editIMG(View view);
    void reqUserData(String auth);
    void reqUpdateData(UserModel model) throws ParseException;
}
