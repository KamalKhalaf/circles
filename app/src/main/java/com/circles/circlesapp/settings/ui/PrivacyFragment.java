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
import com.circles.circlesapp.settings.callBacks.PrivacyCallBack;
import com.circles.circlesapp.databinding.PrivacyLayoutBinding;
import com.circles.circlesapp.settings.viewModels.privacy.PrivacyViewModel;

public class PrivacyFragment extends Fragment implements PrivacyCallBack {
    private PrivacyLayoutBinding layoutBinding;
    private PrivacyViewModel  viewModel;
    private NavController mNavController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding= DataBindingUtil.inflate(inflater, R.layout.privacy_layout,container,false);
        return layoutBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel= ViewModelProviders.of(this).get(PrivacyViewModel.class);
        viewModel.attachView(this);
        mNavController= Navigation.findNavController(getActivity(),R.id.settings_nav_graph);
        layoutBinding.setPrivacy(viewModel);
        viewModel.setUpProgress();
        viewModel.reqPrivacy();

    }

    @Override
    public NavController getNavController() {
        return mNavController;
    }

    @Override
    public PrivacyFragment getFragment() {
        return this;
    }
}
