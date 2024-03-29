package com.circles.circlesapp.phase2.views.ui.profile;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.FragmentProfilePhase2Binding;
import com.circles.circlesapp.phase2.views.adapter.ListOfGroupsAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilePhase2Fragment extends Fragment implements View.OnClickListener {


    FragmentProfilePhase2Binding binding;
    ListOfGroupsAdapter adapter;
    String accessToken;
    String tokenType;

    public ProfilePhase2Fragment() {
        // Required empty public constructor
    }

    static public Fragment newInstants(String tokenType, String accsToken) {
        ProfilePhase2Fragment fragment = new ProfilePhase2Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("ACCESS_TOKEN", accsToken);
        bundle.putString("TOKEN_TYPE", tokenType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            accessToken = getArguments().getString("ACCESS_TOKEN");
            tokenType = getArguments().getString("TOKEN_TYPE");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_phase2, container, false);
        ProfileViewModel viewModel = ViewModelProviders.of(requireActivity()).get(ProfileViewModel.class);
        viewModel.showAccountSettings.observe(getViewLifecycleOwner(), aBoolean -> {
                    if (aBoolean != null && aBoolean) {
                        FragmentActivity activity = getActivity();
                        if (activity == null) return;
                        activity.onBackPressed();
                        showInnerFragment(new AccountSettingsFragment());
                    }
                }
        );
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        binding.ivShowProfileDialouge.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ivShowProfileDialouge:
                showFragment(ProfileSettingDialouge.newInstance());
                break;
        }
    }


    private void showFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, fragment, "rageComicList")
                .addToBackStack(null)
                .commit();
    }

    private void showInnerFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, fragment, "")
                .addToBackStack(null)
                .commit();
    }
}
