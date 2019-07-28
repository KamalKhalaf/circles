package com.circles.circlesapp.settings.ui;

/*
 * Created By mabrouk on 28/01/19
 * com.circles
 */

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import com.circles.circlesapp.settings.callBacks.SettingsCallBack;
import com.circles.circlesapp.databinding.SettingsLayoutBinding;
import com.circles.circlesapp.settings.viewModels.settings.SettingsViewModel;

public class SettingsFragment extends Fragment implements SettingsCallBack {
    private SettingsLayoutBinding layoutBinding;
    private SettingsViewModel viewModel;
    private NavController mNavController;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding= DataBindingUtil.inflate(inflater, R.layout.settings_layout,container,false);
        return layoutBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel= ViewModelProviders.of(this).get(SettingsViewModel.class);
        viewModel.attachView(this);
        mNavController= Navigation.findNavController(getActivity(),R.id.settings_nav_graph);
        layoutBinding.setSettings(viewModel);
    }

    @Override
    public NavController getNavController() {
        return mNavController;
    }

    @Override
    public SettingsFragment getFragment() {
        return this;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
