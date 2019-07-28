package com.circles.circlesapp.settings.viewModels.settings;

/*
 * Created By mabrouk on 28/01/19
 * com.circles
 */

import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.Date;

import com.circles.circlesapp.R;
import com.circles.circlesapp.settings.callBacks.SettingsCallBack;
import com.circles.circlesapp.settings.ui.EditProfileFragment;
import com.circles.circlesapp.settings.ui.LogOutFragment;
import com.circles.circlesapp.settings.UserModel;
import com.circles.circlesapp.settings.viewModels.base.BaseViewModel;

public class SettingsViewModel<v extends SettingsCallBack> extends BaseViewModel<v> implements SettingsVmodel<v> {

    public SettingsViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void editProfile(View view) {
        Bundle bundle=new Bundle();
        bundle.putParcelable(EditProfileFragment.USERMODEL,getUserModel());
        getView().getNavController().navigate(R.id.action_settingsFragment_to_editProfileFragment,bundle);
    }

    private UserModel getUserModel(){
        UserModel userModel=new UserModel();
        userModel.setFirst_name("kamal");
        userModel.setLast_name("khalaf");
        userModel.setUsername("kamall");
        userModel.setPassword("m1234567");
        userModel.setPassword_confirmation("m1234567");
        userModel.setCity("Giza");
        userModel.setCountry("egypt");
        userModel.setDescription("description");
        userModel.setEmail("kamal.khalaf56@gmail.com");
        userModel.setBirthdate(new Date());
        userModel.setGender(1);
        userModel.setPhone("01091008811");
        userModel.setProfile_image("https://firebasestorage.googleapis.com/v0/b/letschat2-79338.appspot.com/o/Users%2FH1iMdOsgqgO1TPhkbSYo8Me8aRD3%2Fprofile_image?alt=media&token=8e02e849-1ecc-4853-bff5-c31a9148e8ba");
        return userModel;
    }

    @Override
    public void privacy(View view) {
     getView().getNavController().navigate(R.id.action_settingsFragment_to_privacyFragment);
    }

    @Override
    public void changeLanguage(View view) {
    }

    @Override
    public void logout(View view) {
        LogOutFragment fragment=LogOutFragment.getInstance();
        fragment.show(getView().getFragment().getChildFragmentManager(),"LogOutFragment");
    }

    @Override
    public void socialMedia(View view) {
      getView().getNavController().navigate(R.id.action_settingsFragment_to_socialAccountsFragment);
    }

    @Override
    public void back(View view) {
      getView().getFragment().getActivity().finish();
    }
}
