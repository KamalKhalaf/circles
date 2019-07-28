package com.circles.circlesapp.settings.ui;

/*
 * Created By mabrouk on 30/01/19
 * com.circles
 */

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.SocialMediaLayoutBinding;
import com.circles.circlesapp.settings.callBacks.SocialMediaCallBack;
import com.circles.circlesapp.settings.socialMedia.SocialMediaViewModel;


public class SocialAccountsFragment extends Fragment implements SocialMediaCallBack {
    private SocialMediaLayoutBinding  layoutBinding;
    private NavController mNavController;
    private SocialMediaViewModel mediaViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding= DataBindingUtil.inflate(inflater, R.layout.social_media_layout,container,false);
        return layoutBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNavController= Navigation.findNavController(getActivity(),R.id.settings_nav_graph);
        mediaViewModel= ViewModelProviders.of(this).get(SocialMediaViewModel.class);
        mediaViewModel.attachView(this);
        layoutBinding.setSocialMedia(mediaViewModel);
        mediaViewModel.reqGetSocialMedia();
    }

    @Override
    public SocialAccountsFragment getFragment() {
        return this;
    }

    @Override
    public NavController getNavController() {
        return mNavController;
    }
}
