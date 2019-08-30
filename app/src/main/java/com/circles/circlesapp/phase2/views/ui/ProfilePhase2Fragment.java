package com.circles.circlesapp.phase2.views.ui;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.FragmentProfilePhase2Binding;
import com.circles.circlesapp.phase2.views.adapter.ListOfGroupsAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilePhase2Fragment extends Fragment {


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
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
