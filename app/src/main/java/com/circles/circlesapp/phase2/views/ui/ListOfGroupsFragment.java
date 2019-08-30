package com.circles.circlesapp.phase2.views.ui;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.circles.circlesapp.phase2.services.ListOfGroupsObject;
import com.circles.circlesapp.phase2.views.adapter.ListOfGroupsAdapter;
import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.FragmentListOfGroupsBinding;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListOfGroupsFragment extends Fragment {


    FragmentListOfGroupsBinding binding;
    ListOfGroupsAdapter adapter;
    String accessToken;
    String tokenType;
    ArrayList<ListOfGroupsObject> listOfGroupsObjects;

    public ListOfGroupsFragment() {
        // Required empty public constructor
    }

    static public Fragment newInstants(String tokenType, String accsToken) {
        ListOfGroupsFragment fragment = new ListOfGroupsFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_of_groups, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listOfGroupsObjects = new ArrayList<>();
        adapter = new ListOfGroupsAdapter(getActivity(), listOfGroupsObjects);
        binding.rvListOfGroups.setAdapter(adapter);
    }
}
