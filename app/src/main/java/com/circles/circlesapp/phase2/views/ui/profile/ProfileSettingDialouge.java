package com.circles.circlesapp.phase2.views.ui.profile;
/*
 *
 * Created by Kamal Khalaf on 9/2/2019.
 * Contact : kamal.khalaf56@gmail.com
 *
 */

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.ProfileSettingDialougeBinding;

public class ProfileSettingDialouge extends Fragment {


    ProfileSettingDialougeBinding mBinding;

    public ProfileSettingDialouge() {
    }

    public static ProfileSettingDialouge newInstance() {
        ProfileSettingDialouge fragment = new ProfileSettingDialouge();
        Bundle bundle = new Bundle();
//        bundle.putBoolean(FEEDS_INSTANCE_KEY, false);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.profile_setting_dialouge, container, false);
        mBinding.setLifecycleOwner(this);
        ProfileViewModel  viewModel = ViewModelProviders.of(requireActivity()).get(ProfileViewModel.class);

        mBinding.ivClose.setOnClickListener(view -> {
            FragmentActivity activity = getActivity();
            if (activity == null) return;
            activity.onBackPressed();
        });
        mBinding.llSettings.setOnClickListener(v ->{
            viewModel.showAccountSettings.postValue(true);

        });
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {

                try {
                    FragmentActivity activity = getActivity();
                    activity.onBackPressed();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        });
    }
}
