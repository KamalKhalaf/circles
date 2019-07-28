package com.circles.circlesapp.messaging.view;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.FragmentChooseToSendBinding;
import com.circles.circlesapp.messaging.MessagingOptionsCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseToSendFragment extends Fragment {


    private boolean is_group;

    public ChooseToSendFragment() {
        // Required empty public constructor
    }

    public static ChooseToSendFragment newInstance(boolean is_group) {

        Bundle args = new Bundle();
        args.putBoolean("is_group", is_group);
        ChooseToSendFragment fragment = new ChooseToSendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            is_group = getArguments().getBoolean("is_group", true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentChooseToSendBinding b = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_to_send, container, false);
        b.cardViewCancel.setOnClickListener(v -> {
            if (getActivity() == null) return;
            getActivity().getSupportFragmentManager().popBackStack();
        });
        b.sendImgOrVideoTv.setOnClickListener(v -> {
            if (getActivity() == null) return;
            ((MessagingOptionsCallback) getActivity()).sendImageOrVideo();
            getActivity().getSupportFragmentManager().popBackStack();

        });

        b.createAPollTv.setOnClickListener(v -> {
            if (getActivity() == null) return;
            if (!is_group) {
                Toast.makeText(getContext(), "No voting in private messages", Toast.LENGTH_SHORT).show();
                return;
            }
            b.mainConst.setVisibility(View.GONE);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.add(b.fragPoll.getId(), PollFragment.newInstance(), "").commit();

        });
        b.sendFilesTv.setOnClickListener(v -> {
            if (getActivity() == null) return;
            ((MessagingOptionsCallback) getActivity()).sendFiles();
            getActivity().getSupportFragmentManager().popBackStack();

        });
        return b.getRoot();
    }

}
